package dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import bean.User;
import util.PasswordUtil;

class UserDAOTest {
  private UserDAO userDAO;
  private DataSource mockDataSource;
  private Connection mockConnection;
  private PreparedStatement mockStatement;
  private ResultSet mockResultSet;

  @BeforeEach
  void setUp() throws Exception {
    userDAO = new UserDAO();
    mockDataSource = mock(DataSource.class);
    mockConnection = mock(Connection.class);
    mockStatement = mock(PreparedStatement.class);
    mockResultSet = mock(ResultSet.class);

    Field field = DAO.class.getDeclaredField("ds");
    field.setAccessible(true);
    field.set(null, mockDataSource);

    when(mockDataSource.getConnection()).thenReturn(mockConnection);
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    when(mockStatement.executeQuery()).thenReturn(mockResultSet);
  }

  @Nested
  @DisplayName("select メソッドのテスト")
  class SelectTests {

    @Test
    @DisplayName("正常系：正しいユーザー名とパスワードで認証に成功すること")
    void testSelectSuccess() throws Exception {
      String plainPass = "password123";
      String hashedPass = PasswordUtil.hash(plainPass);

      when(mockResultSet.next()).thenReturn(true);
      when(mockResultSet.getString("password")).thenReturn(hashedPass);
      when(mockResultSet.getInt("id")).thenReturn(1);
      when(mockResultSet.getString("name")).thenReturn("Alice");

      User result = userDAO.select("Alice", plainPass);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1);
      assertThat(result.getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("異常系：ユーザーが存在しない場合に null を返すこと")
    void testSelectUserNotFound() throws Exception {
      when(mockResultSet.next()).thenReturn(false);

      User result = userDAO.select("unknown", "pass");

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("異常系：パスワードが一致しない場合に null を返すこと")
    void testSelectInvalidPassword() throws Exception {
      String hashedPass = PasswordUtil.hash("correct_pass");

      when(mockResultSet.next()).thenReturn(true);
      when(mockResultSet.getString("password")).thenReturn(hashedPass);

      User result = userDAO.select("Alice", "wrong_pass");

      assertThat(result).isNull();
    }

    @Test
    @DisplayName("異常系：SQLExceptionが発生した際に例外をキャッチして null を返すこと")
    void testSelectWithException() throws Exception {
      when(mockDataSource.getConnection()).thenThrow(new RuntimeException("DB Error"));

      User result = userDAO.select("any", "any");

      assertThat(result).isNull();
    }
  }
}
