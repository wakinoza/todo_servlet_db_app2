package dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import bean.TodoItem;

@DisplayName("TodoItemDAO の単体テスト")
class TodoItemDAOTest {
  private TodoItemDAO todoItemDAO;
  private DataSource mockDs;
  private Connection mockCon;
  private PreparedStatement mockPs;
  private ResultSet mockRs;

  @BeforeEach
  void setUp() throws Exception {
    todoItemDAO = new TodoItemDAO();
    mockDs = mock(DataSource.class);
    mockCon = mock(Connection.class);
    mockPs = mock(PreparedStatement.class);
    mockRs = mock(ResultSet.class);

    // JNDIのDataSourceをモックに差し替え
    Field field = DAO.class.getDeclaredField("ds");
    field.setAccessible(true);
    field.set(null, mockDs);

    when(mockDs.getConnection()).thenReturn(mockCon);
    when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
    when(mockPs.executeQuery()).thenReturn(mockRs);
  }

  // -------------------------------------------------------------------------
  // insert メソッドのテスト
  // -------------------------------------------------------------------------
  @Nested
  @DisplayName("insert：新規登録のテスト")
  class InsertTests {
    @Test
    @DisplayName("【正常系】1件挿入が成功し、trueを返すこと")
    void testInsertSuccess() throws Exception {
      when(mockPs.executeUpdate()).thenReturn(1);
      assertThat(todoItemDAO.insert(new TodoItem("テスト"))).isTrue();
    }

    @Test
    @DisplayName("【異常系】SQL例外発生時にfalseを返すこと")
    void testInsertException() throws Exception {
      when(mockCon.prepareStatement(anyString())).thenThrow(new RuntimeException());
      assertThat(todoItemDAO.insert(new TodoItem())).isFalse();
    }

    @Test
    @DisplayName("【網羅系】更新行数が0の場合にfalseを返すこと")
    void testInsertZeroRows() throws Exception {
      when(mockPs.executeUpdate()).thenReturn(0);
      assertThat(todoItemDAO.insert(new TodoItem("test"))).isFalse();
    }
  }

  // -------------------------------------------------------------------------
  // selectAll メソッドのテスト
  // -------------------------------------------------------------------------
  @Nested
  @DisplayName("selectAll：全件取得のテスト")
  class SelectAllTests {
    @Test
    @DisplayName("【正常系】データが存在する場合、リストで返されること")
    void testSelectAllSuccess() throws Exception {
      when(mockRs.next()).thenReturn(true, true, false);
      when(mockRs.getInt("id")).thenReturn(1, 2);
      when(mockRs.getString("text")).thenReturn("A", "B");
      List<TodoItem> list = todoItemDAO.selectAll();
      assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("【正常系】データが0件の場合、空のリストを返すこと")
    void testSelectAllEmpty() throws Exception {
      when(mockRs.next()).thenReturn(false);
      assertThat(todoItemDAO.selectAll()).isEmpty();
    }

    @Test
    @DisplayName("【異常系】例外発生時に空のリストを返すこと")
    void testSelectAllException() throws Exception {
      when(mockCon.prepareStatement(anyString()))
          .thenThrow(new RuntimeException("SelectAll Error"));
      assertThat(todoItemDAO.selectAll()).isEmpty();
    }
  }

  // -------------------------------------------------------------------------
  // delete メソッドのテスト
  // -------------------------------------------------------------------------
  @Nested
  @DisplayName("delete：削除のテスト")
  class DeleteTests {
    @Test
    @DisplayName("【正常系】指定IDの削除が成功し、trueを返すこと")
    void testDeleteSuccess() throws Exception {
      when(mockPs.executeUpdate()).thenReturn(1);
      assertThat(todoItemDAO.delete(10)).isTrue();
    }

    @Test
    @DisplayName("【異常系】例外発生時にfalseを返すこと")
    void testDeleteException() throws Exception {
      when(mockCon.prepareStatement(contains("DELETE")))
          .thenThrow(new RuntimeException("Delete Error"));
      assertThat(todoItemDAO.delete(1)).isFalse();
    }

    @Test
    @DisplayName("【網羅系】更新行数が0の場合、および実行時例外のケース")
    void testDeleteEdgeCases() throws Exception {
      // 行数0
      when(mockPs.executeUpdate()).thenReturn(0);
      assertThat(todoItemDAO.delete(1)).isFalse();

      // executeUpdateでの例外
      when(mockPs.executeUpdate()).thenThrow(new RuntimeException("execute error"));
      assertThat(todoItemDAO.delete(1)).isFalse();
    }
  }

  // -------------------------------------------------------------------------
  // updateProgress メソッドのテスト
  // -------------------------------------------------------------------------
  @Nested
  @DisplayName("updateProgress：進捗更新のテスト")
  class UpdateProgressTests {

    @Nested
    @DisplayName("正常系・状態遷移")
    class PositiveTests {
      @ParameterizedTest
      @CsvSource({"未実施, 実施中", "実施中, 完了済"})
      @DisplayName("ステータスが正しく遷移すること")
      void testUpdateStatus(String current, String expectedNext) throws Exception {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("progress")).thenReturn(current);
        when(mockPs.executeUpdate()).thenReturn(1);

        assertThat(todoItemDAO.updateProgress(1)).isTrue();
        verify(mockPs).setString(1, expectedNext);
      }

      @Test
      @DisplayName("完了済の場合は内部でdeleteが呼ばれること")
      void testUpdateDoneToDelete() throws Exception {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("progress")).thenReturn("完了済");
        when(mockPs.executeUpdate()).thenReturn(1);
        assertThat(todoItemDAO.updateProgress(1)).isTrue();
      }
    }

    @Nested
    @DisplayName("異常系・エラーハンドリング")
    class NegativeTests {
      @Test
      @DisplayName("指定IDが存在しない場合はfalse")
      void testUpdateIdNotFound() throws Exception {
        when(mockRs.next()).thenReturn(false);
        assertThat(todoItemDAO.updateProgress(99)).isFalse();
      }

      @Test
      @DisplayName("SQL発行時に例外が発生した場合はfalse")
      void testUpdateException() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenThrow(new RuntimeException("SQL Error"));
        assertThat(todoItemDAO.updateProgress(1)).isFalse();
      }
    }

    @Nested
    @DisplayName("網羅用（複雑な例外パス）")
    class CoverageTests {
      @Test
      @DisplayName("内部のdelete処理が失敗した場合にfalse")
      void testUpdateProgressDeleteFalse() throws Exception {
        PreparedStatement psSelect = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(mockCon.prepareStatement(contains("SELECT"))).thenReturn(psSelect);
        when(psSelect.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("progress")).thenReturn("完了済");

        PreparedStatement psDelete = mock(PreparedStatement.class);
        when(mockCon.prepareStatement(contains("DELETE"))).thenReturn(psDelete);
        when(psDelete.executeUpdate()).thenReturn(0);

        assertThat(todoItemDAO.updateProgress(1)).isFalse();
      }

      @Test
      @DisplayName("網羅：updateProgressの2番目のUPDATEで更新行数が0だった場合にfalseを返すこと")
      void testUpdateProgressSecondPsZeroRows() throws Exception {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("progress")).thenReturn("未実施");

        when(mockPs.executeUpdate()).thenReturn(0);

        boolean result = todoItemDAO.updateProgress(1);
        assertThat(result).isFalse();
      }

      @Test
      @DisplayName("各tryブロックおよびclose処理での例外網羅")
      void testTryWithResourcesEdges() throws Exception {
        // 1つ目のtry付近
        when(mockCon.prepareStatement(contains("SELECT")))
            .thenThrow(new RuntimeException("First Try"));
        assertThat(todoItemDAO.updateProgress(1)).isFalse();

        // close時の例外
        reset(mockCon, mockRs, mockPs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString("progress")).thenReturn("未実施");
        doThrow(new RuntimeException("close error")).when(mockRs).close();
        assertThat(todoItemDAO.updateProgress(1)).isFalse();
      }

      @Test
      @DisplayName("抑制された例外（Suppressed Exception）の網羅")
      void testSuppressedException() throws Exception {
        when(mockCon.prepareStatement(anyString()))
            .thenThrow(new RuntimeException("Primary Error"));
        doThrow(new RuntimeException("Close Error")).when(mockCon).close();
        assertThat(todoItemDAO.updateProgress(1)).isFalse();
      }
    }
  }
}
