package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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
    String name = request.getParameter("name");
    String pass = request.getParameter("pass");
    LoginLogic loginLogic = new LoginLogic();
    User loginUser = null;
    loginUser = loginLogic.serch(name, pass);

    if (loginUser != null) {
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", loginUser);
    }
    request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
  }
}

