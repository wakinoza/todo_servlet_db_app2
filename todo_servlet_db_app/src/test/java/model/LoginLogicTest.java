package model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import dao.UserDAO;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import bean.User;

class LoginLogicTest {

  private UserDAO mockDao;
  private LoginLogic loginLogic;

  @BeforeEach
  void setUp() {
    mockDao = mock(UserDAO.class);
    loginLogic = new LoginLogic(mockDao);
  }

  // --- 正常系 ---
  @ParameterizedTest
  @MethodSource("provideSuccessPatterns")
  @DisplayName("正常な入力値でログインに成功すること")
  void testSearchSuccess(String name, String pass) {
    User expected = new User(1, name, pass);
    when(mockDao.select(name, pass)).thenReturn(expected);

    User result = loginLogic.search(name, pass);

    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> provideSuccessPatterns() {
    return Stream.of(Arguments.of("admin", "pass123"), Arguments.of("a", "b"),
        Arguments.of("a".repeat(50), "b".repeat(100)));
  }

  // --- 異常系 (バリデーション) ---
  @ParameterizedTest
  @MethodSource("provideInvalidPatterns")
  @DisplayName("不正な入力値でnullを返すこと（バリデーション網羅）")
  void testSearchValidation(String name, String pass, String description) {
    User result = loginLogic.search(name, pass);

    assertThat(result).as(description).isNull();
    verify(mockDao, never()).select(anyString(), anyString());
  }

  private static Stream<Arguments> provideInvalidPatterns() {
    return Stream.of(Arguments.of(null, "pass", "nameがnull"), Arguments.of("", "pass", "nameが空文字"),
        Arguments.of("   ", "pass", "nameが空白"), Arguments.of("a".repeat(51), "pass", "nameが長すぎる"),
        Arguments.of("admin", null, "passがnull"), Arguments.of("admin", "", "passが空文字"),
        Arguments.of("admin", "  ", "passが空白"),
        Arguments.of("admin", "a".repeat(101), "passが長すぎる"));
  }
}
