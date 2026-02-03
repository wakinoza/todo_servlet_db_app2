package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import bean.User;
import util.PasswordUtil;

/** todo_usersテーブルの操作を行うDAOクラス. */
public class UserDAO extends DAO {

  /**
   * テーブルに指定されたUserが存在するかを確認するメソッド.
   *
   * @param name ユーザー名
   * @param pass 入力された平文パスワード
   * @return 認証成功ならUserインスタンスを、失敗ならnullを返す
   */
  public User select(String name, String pass) {
    User user = null;

    String sql = "SELECT id, name, password FROM todo_users WHERE name = ?";

    try (Connection con = getReadConnection()) {
      try (PreparedStatement st = con.prepareStatement(sql)) {
        st.setString(1, name);

        try (ResultSet rs = st.executeQuery()) {
          if (rs.next()) {

            String dbHash = rs.getString("password");

            if (PasswordUtil.check(pass, dbHash)) {
              user = new User();
              user.setId(rs.getInt("id"));
              user.setName(rs.getString("name"));
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return user;
  }
}
