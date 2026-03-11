package servlet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.LoginLogic;
import bean.User;

class LoginTest {
  private HttpServletRequest request;
  private HttpServletResponse response;
  private HttpSession session;
  private RequestDispatcher dispatcher;
  private Login loginServlet;
  private LoginLogic mockLogic;

  @BeforeEach
  void setUp() {
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    session = mock(HttpSession.class);
    dispatcher = mock(RequestDispatcher.class);
    mockLogic = mock(LoginLogic.class);

    // 共通のスタブ設定（デフォルトの振る舞い）
    setupDefaultMocks();
  }

  private void setupDefaultMocks() {
    try {
      when(request.getSession(true)).thenReturn(session);
      when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
      when(request.getContextPath()).thenReturn("");
      when(session.getId()).thenReturn("test-session-id");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setLoginParams(String name, String pass) {
    when(request.getParameter("name")).thenReturn(name);
    when(request.getParameter("pass")).thenReturn(pass);
  }

  @Test
  @DisplayName("正常系：正しい資格情報でログインし、セッションとトークンが生成されること")
  void testDoPostSuccess() throws Exception {

    User mockUser = new User(1, "admin", "pass");
    when(mockLogic.search("admin", "pass")).thenReturn(mockUser);
    setLoginParams("admin", "pass");
    loginServlet = new Login(mockLogic);

    loginServlet.doPost(request, response);

    verify(session).setAttribute("loginUser", mockUser);
    verify(session).setAttribute(eq("csrfToken"), anyString());
    verify(dispatcher).forward(request, response);
  }

  @Test
  @DisplayName("セキュリティ：既存セッションがある場合、無効化されてから再生成されること")
  void testDoPostWithExistingSession() throws Exception {

    HttpSession oldSession = mock(HttpSession.class);
    when(request.getSession(false)).thenReturn(oldSession);
    when(mockLogic.search(any(), any())).thenReturn(new User(1, "u", "p"));
    setLoginParams("admin", "pass");
    loginServlet = new Login(mockLogic);

    loginServlet.doPost(request, response);

    verify(oldSession).invalidate();
    verify(session).setAttribute(eq("loginUser"), any());
  }

  @Test
  @DisplayName("異常系：認証失敗時にセッション保存が行われないこと")
  void testDoPostFailure() throws Exception {

    when(mockLogic.search(any(), any())).thenReturn(null);
    setLoginParams("wrong", "wrong");
    loginServlet = new Login(mockLogic);

    loginServlet.doPost(request, response);

    verify(session, never()).setAttribute(eq("loginUser"), any());
    verify(dispatcher).forward(request, response);
  }

  @Test
  @DisplayName("境界条件：ContextPathがある場合にCookieのPathが正しく設定されること")
  void testDoPostWithContextPath() throws Exception {

    when(request.getContextPath()).thenReturn("/app");
    when(mockLogic.search(any(), any())).thenReturn(new User(1, "u", "p"));
    setLoginParams("u", "p");
    loginServlet = new Login(mockLogic);

    loginServlet.doPost(request, response);

    verify(response).setHeader(eq("Set-Cookie"), contains("Path=/app"));
  }

  @Test
  @DisplayName("網羅：デフォルトコンストラクタ経由でFactoryルートを通過すること")
  void testDoPostWithDefaultConstructor() throws Exception {
    loginServlet = new Login();
    setLoginParams("any", "any");

    // Factory経由でDAOが動く可能性があるため例外は許容)
    try {
      loginServlet.doPost(request, response);
    } catch (Exception ignored) {
    }

    verify(dispatcher).forward(request, response);
  }
}
