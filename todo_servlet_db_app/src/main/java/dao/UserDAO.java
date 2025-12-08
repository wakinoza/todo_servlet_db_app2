package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import bean.User;

/** . todo_usersテーブルの操作を行うDAOクラス */
public class UserDAO extends DAO {
  /**
   * . テーブルに指定されたUserが存在するかを確認するメソッド
   *
   * @param name ユーザー名
   * @param pass パスワード
   * @return 指定されたユーザーがいればそのUserインスタンスを、いなければnullを返す
   */
  public User select(String name, String pass) {
    User user = null;

    try (Connection con = getConnection()) {
      PreparedStatement st =
          con.prepareStatement("SELECT id, name FROM todo_users WHERE name=? AND password=?");

      st.setString(1, name);
      st.setString(2, pass);

      try (ResultSet rs = st.executeQuery()) {
        if (rs.next()) {
          user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return user;

  }

}
