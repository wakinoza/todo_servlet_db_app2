package model;

import bean.User;
import dao.UserDAO;

/**
 * . ログイン処理を行うビジネスクラス
 */
public class LoginLogic {
  /**
   * . 指定されたUserがDBテーブルにあるかを確認するメソッド
   *
   * @param name ユーザー名
   * @param pass パスワード
   * @return 指定されたUserがDBテーブルにあればUseインスタンスを、なければnullを返す
   */
  public User serch(String name, String pass) {
    UserDAO userDao = new UserDAO();
    return userDao.select(name, pass);
  }
}
