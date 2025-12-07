package bean;

import java.io.Serializable;

/**
 * . ユーザー情報を保持するクラス
 */
public class User implements Serializable {

  /** . ユーザーID */
  private int id;

  /** . ユーザー名 */
  private String name;

  /** . パスワード */
  private String pass;

  /**
   * . コンストラクタ
   */
  public User() {}

  /**
   * . 引数ありのコンストラクタ
   */
  public User(int id, String name, String pass) {
    this.id = id;
    this.name = name;
    this.pass = pass;
  }

  /**
   * . getterメソッド
   */
  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getPass() {
    return this.pass;
  }

  /**
   * . setterメソッド
   */
  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }
}
