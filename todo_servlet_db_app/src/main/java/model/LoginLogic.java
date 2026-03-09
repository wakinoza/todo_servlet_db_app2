package model;

import dao.UserDAO;
import bean.User;

/**
 * . ログイン処理を行うビジネスクラス
 */
public class LoginLogic {

  private static final int MAX_NAME_LENGTH = 50;
  private static final int MAX_PASS_LENGTH = 100;
  private final UserDAO userDao;

  // コンストラクタでDAOを受け取る
  /**
   * . コンストラクタ
   *
   * @param userDao UserDAOのインスタンス
   */
  public LoginLogic(UserDAO userDao) {
    this.userDao = userDao;
  }

  /**
   * . 指定されたUserがDBテーブルにあるかを確認するメソッド
   *
   * @param name ユーザー名
   * @param pass パスワード
   * @return 指定されたUserがDBテーブルにあればUseインスタンスを、なければnullを返す
   */
  public User search(String name, String pass) {
    if (name == null || name.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
      return null;
    }

    if (name.length() > MAX_NAME_LENGTH || pass.length() > MAX_PASS_LENGTH) {
      return null;
    }

    return userDao.select(name, pass);
  }
}
