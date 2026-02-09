package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import model.LoginLogic;
import bean.User;

/**
 * . ログイン処理するサーブレットクラス
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * . @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Content-Security-Policy",
        "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline';");

    String name = request.getParameter("name");
    String pass = request.getParameter("pass");
    LoginLogic loginLogic = new LoginLogic();
    User loginUser = null;
    loginUser = loginLogic.serch(name, pass);

    if (loginUser != null) {
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", loginUser);

      SecureRandom random = new SecureRandom();
      byte[] bytes = new byte[32];
      random.nextBytes(bytes);
      String csrfToken = Base64.getEncoder().encodeToString(bytes);
      session.setAttribute("csrfToken", csrfToken);

      String sessionId = session.getId();
      String contextPath = request.getContextPath();
      String cookieHeader = String.format("JSESSIONID=%s; Path=%s; HttpOnly; SameSite=Lax",
          sessionId, (contextPath.isEmpty() ? "/" : contextPath));
      response.setHeader("Set-Cookie", cookieHeader);
    }
    request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
  }
}

