package util;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordUtilTest {

  @ParameterizedTest
  @ValueSource(strings = {"password123", "admin_pass", "!", "   "})
  @DisplayName("hashとcheckが連携して正しくパスワードを照合できること")
  void testHashAndCheck(String rawPassword) {
    String hashed = PasswordUtil.hash(rawPassword);

    assertThat(hashed).isNotEqualTo(rawPassword);
    assertThat(PasswordUtil.check(rawPassword, hashed)).isTrue();
  }

  @Test
  @DisplayName("間違ったパスワードを入力した場合にfalseを返すこと")
  void testCheckInvalidPassword() {
    String rawPassword = "correct_password";
    String hashed = PasswordUtil.hash(rawPassword);
    assertThat(PasswordUtil.check("wrong_password", hashed)).isFalse();
  }
}
