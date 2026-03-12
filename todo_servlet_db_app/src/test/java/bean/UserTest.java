package bean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  @DisplayName("引数なしコンストラクタとSetter/Getterのテスト")
  void testNoArgsConstructorAndAccessors() {
    User user = new User();

    user.setId(1);
    user.setName("テストユーザー");
    user.setPass("password123");

    assertAll(() -> assertThat(user.getId()).isEqualTo(1),
        () -> assertThat(user.getName()).isEqualTo("テストユーザー"),
        () -> assertThat(user.getPass()).isEqualTo("password123"));
  }

  @Test
  @DisplayName("引数ありコンストラクタのテスト")
  void testAllArgsConstructor() {
    User user = new User(99, "テストユーザー", "testpass");

    assertAll(() -> assertThat(user.getId()).isEqualTo(99),
        () -> assertThat(user.getName()).isEqualTo("テストユーザー"),
        () -> assertThat(user.getPass()).isEqualTo("testpass"));
  }
}
