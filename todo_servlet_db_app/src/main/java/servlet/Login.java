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
import factory.LogicFactory;

/**
 * . ログイン処理するサーブレットクラス
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private LoginLogic loginLogic;

  /**
   * . Tomcat用のデフォルトコンストラクタ
   */
  public Login() {
    this.loginLogic = null;
  }

  /**
   * . テスト用のコンストラクタ
   *
   * @param logic モック化された LoginLogic
   */
  public Login(LoginLogic logic) {
    this.loginLogic = logic;
  }

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
    LoginLogic logic =
        (this.loginLogic != null) ? this.loginLogic : LogicFactory.createLoginLogic();
    logic.search(name, pass);
    User loginUser = null;
    loginUser = logic.search(name, pass);

    if (loginUser != null) {
      HttpSession oldSession = request.getSession(false);
      if (oldSession != null) {
        oldSession.invalidate();
      }
      HttpSession newSession = request.getSession(true);
      newSession.setAttribute("loginUser", loginUser);

      SecureRandom random = new SecureRandom();
      byte[] bytes = new byte[32];
      random.nextBytes(bytes);
      String csrfToken = Base64.getEncoder().encodeToString(bytes);
      newSession.setAttribute("csrfToken", csrfToken);

      String sessionId = newSession.getId();
      String contextPath = request.getContextPath();
      String cookieHeader = String.format("JSESSIONID=%s; Path=%s; HttpOnly; SameSite=Lax",
          sessionId, (contextPath.isEmpty() ? "/" : contextPath));
      response.setHeader("Set-Cookie", cookieHeader);
    }
    request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
  }
}

