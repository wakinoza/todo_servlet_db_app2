package servlet;



import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import bean.User;
import dao.UserDAO;;


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
    String name = request.getParameter("name");
    String pass = request.getParameter("pass");
    UserDAO dao = new UserDAO();
    User loginUser = null;
    try {
      loginUser = dao.serch(name, pass);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (loginUser != null) {
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", loginUser);
    }
    request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
  }
}

