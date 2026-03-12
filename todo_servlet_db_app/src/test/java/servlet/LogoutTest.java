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

class LogoutTest {
  private HttpServletRequest request;
  private HttpServletResponse response;
  private HttpSession session;
  private RequestDispatcher dispatcher;
  private Logout logoutServlet;

  @BeforeEach
  void setUp() {
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    session = mock(HttpSession.class);
    dispatcher = mock(RequestDispatcher.class);
    logoutServlet = new Logout();
  }

  @Test
  @DisplayName("ログアウト実行：セッションが破棄され、ログアウト画面に遷移すること")
  void testDoGet() throws Exception {
    when(request.getSession()).thenReturn(session);
    when(request.getRequestDispatcher("WEB-INF/jsp/logout.jsp")).thenReturn(dispatcher);

    logoutServlet.doGet(request, response);

    verify(session).invalidate();
    verify(dispatcher).forward(request, response);
    verify(response).setHeader(eq("Content-Security-Policy"), anyString());
  }
}
