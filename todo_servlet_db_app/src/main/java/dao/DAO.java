package dao;

import java.sql.Connection;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/** . DB接続をつかさどる共通クラス. */
public class DAO {
  static DataSource ds;

  /**
   * Connectionを取得.
   */
  public Connection getConnection() throws Exception {
    if (ds == null) {
      InitialContext ic = new InitialContext();
      ds = (DataSource) ic.lookup("java:comp/env/jdbc/todo_db");
    }
    return ds.getConnection();
  }

}
