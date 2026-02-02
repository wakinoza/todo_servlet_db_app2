package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  // ハッシュ化用
  public static String hash(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  // 照合用
  public static boolean check(String rawPassword, String hashed) {
    return encoder.matches(rawPassword, hashed);
  }

}
