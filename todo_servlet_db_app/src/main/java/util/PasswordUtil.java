package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードをハッシュ化するユーティリティクラス.
 */
public class PasswordUtil {
  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  /**
   * . プライベートコンストラクタ
   */
  private PasswordUtil() {
    throw new AssertionError("Utility class should not be instantiated.");
  }

  /**
   * パスワードをハッシュ化するメソッド.
   * 
   * @param rawPassword ユーザー入力のパスワード
   * @return ハッシュ値
   */
  public static String hash(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  /**
   * 入力したパスワードと、DBのハッシュ値とを比較するメソッド.
   * 
   * @param rawPassword ユーザー入力のパスワード
   * @param hashed DBに格納されたハッシュ値
   * @return 照合結果
   */
  public static boolean check(String rawPassword, String hashed) {
    return encoder.matches(rawPassword, hashed);
  }

  // /**
  // * ハッシュ値を出力するメソッド.
  // * @param args
  // */
  // public static void main(String[] args) {
  // String myPass = "yamada_password";
  // System.out.println("生成されたハッシュ値: " + hash(myPass));
  // }

}
