package dao;

import java.sql.Connection;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/** . DB接続をつかさどるクラス */
public class DAO {
  static DataSource ds;

  /**
   * . DBと接続するメソッド
   *
   * @return Connectionインスタンス
   * @throws Exception DBと接続できなかった場合
   */
  public Connection getConnection() throws Exception {
    if (ds == null) {
      InitialContext ic = new InitialContext();
      ds = (DataSource) ic.lookup("java:comp/env/jdbc/test_db");
    }

    return ds.getConnection();
  }

}
