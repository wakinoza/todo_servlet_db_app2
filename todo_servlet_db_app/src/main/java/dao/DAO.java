package dao;

import java.sql.Connection;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/** . DB接続をつかさどる共通クラス. */
public class DAO {
  static DataSource readDs; // 検索専用
  static DataSource writeDs; // 更新用

  /**
   * 検索専用（SELECTのみ）のConnectionを取得.
   */
  public Connection getReadConnection() throws Exception {
    if (readDs == null) {
      InitialContext ic = new InitialContext();
      // context.xmlで定義した検索用リソース名を指定
      readDs = (DataSource) ic.lookup("java:comp/env/jdbc/test_db_read");
    }
    return readDs.getConnection();
  }

  /**
   * 更新用（INSERT/UPDATE/DELETE）のConnectionを取得.
   */
  public Connection getWriteConnection() throws Exception {
    if (writeDs == null) {
      InitialContext ic = new InitialContext();
      // context.xmlで定義した更新用リソース名を指定
      writeDs = (DataSource) ic.lookup("java:comp/env/jdbc/test_db_write");
    }
    return writeDs.getConnection();
  }
}
