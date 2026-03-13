package dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DAOTest {
  private DAO dao;
  private static DataSource mockDs;

  @BeforeEach
  void setUp() throws Exception {
    dao = new DAO();
    mockDs = mock(DataSource.class);

    Field field = DAO.class.getDeclaredField("ds");
    field.setAccessible(true);
    field.set(null, null);


    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockFactory.class.getName());
  }

  @Test
  @DisplayName("100%網羅：JNDIルックアップと接続取得の確認")
  void testGetConnectionSuccess() throws Exception {
    Connection mockConn = mock(Connection.class);
    when(mockDs.getConnection()).thenReturn(mockConn);

    Connection result = dao.getConnection();

    assertThat(result).isEqualTo(mockConn);
    verify(mockDs).getConnection();
  }


  public static class MockFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> env) {
      Context context = mock(Context.class);
      try {

        when(context.lookup(anyString())).thenReturn(mockDs);
      } catch (Exception e) {
      }
      return context;
    }
  }
}
