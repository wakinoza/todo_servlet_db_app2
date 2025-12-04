package model;

import bean.User;
import dao.UserDAO;

public class LoginLogic {
  public User serch(String name, String pass) {
    UserDAO userDAO = new UserDAO();
    return userDAO.select(name, pass);
  }
}
