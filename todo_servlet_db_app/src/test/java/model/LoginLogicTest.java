package model;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import bean.User;

class LoginLogicTest {

  private UserDAO mockDao;
  private LoginLogic loginLogic;

  @BeforeEach
  void setUp() {
    // テストごとにモックを初期化
    mockDao = mock(UserDAO.class);
    loginLogic = new LoginLogic(mockDao);
  }

  @Test
  @DisplayName("正常なユーザー名とパスワードでユーザーが返されること")
  void testSearchSuccess() {
    // 準備
    User expectedUser = new User(1, "admin", "pass123");
    when(mockDao.select("admin", "pass123")).thenReturn(expectedUser);

    // 実行
    User result = loginLogic.search("admin", "pass123");

    // 検証 (AssertJ)
    assertThat(result).isNotNull().isEqualTo(expectedUser);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "   "})
  @DisplayName("名前が空文字や空白の場合にnullを返すこと")
  void testSearchEmptyName(String emptyName) {
    User result = loginLogic.search(emptyName, "password");
    assertThat(result).isNull();
    verify(mockDao, never()).select(anyString(), anyString());
  }

  @Test
  @DisplayName("名前が50文字を超える場合にnullを返すこと")
  void testSearchLongName() {
    String longName = "a".repeat(51);
    User result = loginLogic.search(longName, "password");
    assertThat(result).isNull();

    verify(mockDao, never()).select(anyString(), anyString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "   "})
  @DisplayName("パスワードが空文字や空白の場合にnullを返すこと")
  void testSearchEmptyPass(String emptyPass) {
    User result = loginLogic.search("admin", emptyPass);
    assertThat(result).isNull();
    verify(mockDao, never()).select(anyString(), anyString());
  }

  @Test
  @DisplayName("パスワードがnullの場合にnullを返すこと")
  void testSearchNullPass() {
    User result = loginLogic.search("admin", null);
    assertThat(result).isNull();
    verify(mockDao, never()).select(anyString(), anyString());
  }

  @Test
  @DisplayName("パスワードが100文字を超える場合にnullを返すこと")
  void testSearchLongPass() {
    String longPass = "a".repeat(101);
    User result = loginLogic.search("admin", longPass);
    assertThat(result).isNull();
    verify(mockDao, never()).select(anyString(), anyString());

  }

  @Test
  @DisplayName("名前は正常だがパスワードがnullの場合にnullを返すこと")
  void testSearchNameOkPassIsNull() {
    User result = loginLogic.search("admin", null);
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("名前は正常だがパスワードが空白のみの場合にnullを返すこと")
  void testSearchNameOkPassIsBlank() {
    User result = loginLogic.search("admin", "   ");
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("名前は正常だがパスワードが100文字を超える場合にnullを返すこと")
  void testSearchNameOkPassTooLong() {
    String normalName = "admin";
    String longPass = "a".repeat(101);

    User result = loginLogic.search(normalName, longPass);

    assertThat(result).isNull();
    verify(mockDao, never()).select(anyString(), anyString());
  }

  @Test
  @DisplayName("名前とパスワードが両方制限ギリギリ（境界値）で成功すること")
  void testSearchBothBoundaries() {
    String maxName = "a".repeat(50);
    String maxPass = "b".repeat(100);

    User expectedUser = new User(1, maxName, maxPass);
    when(mockDao.select(maxName, maxPass)).thenReturn(expectedUser);

    User result = loginLogic.search(maxName, maxPass);

    assertThat(result).isEqualTo(expectedUser);
  }

  @Test
  @DisplayName("すべてのバリデーションをギリギリで通過してDAOが呼ばれること")
  void testSearchCompleteFlow() {
    String validName = "a";
    String validPass = "b";

    User expected = new User(1, validName, validPass);
    when(mockDao.select(validName, validPass)).thenReturn(expected);

    User result = loginLogic.search(validName, validPass);

    assertThat(result).isEqualTo(expected);
  }

}
