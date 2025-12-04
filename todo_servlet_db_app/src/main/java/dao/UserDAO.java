package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import bean.User;

public class UserDAO extends DAO {
  public User select(String name, String password) {
    User user = null;

    try (Connection con = getConnection()) {
      PreparedStatement st =
          con.prepareStatement("SELECT * FROM todo_users WHERE name=? AND password=?");

      st.setString(1, name);
      st.setString(2, password);

      try (ResultSet rs = st.executeQuery()) {
        if (rs.next()) {
          user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
        }
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    return user;

  }

}
