package servlet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import bean.TodoItem;
import bean.User;
import dao.TodoItemDAO;

/**
 * . メイン画面の処理を司るサーブレット
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * . @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ServletContext application = this.getServletContext();
    List<TodoItem> todoItemList = (List<TodoItem>) application.getAttribute("todoItemList");

    if (todoItemList == null) {
      todoItemList = new ArrayList<>();
      application.setAttribute("todoItemList", todoItemList);
    }
    HttpSession session = request.getSession();
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      response.sendRedirect("index.jsp");

    } else {
      request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);
    }

  }

  /**
   * . @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    ServletContext application = this.getServletContext();
    List<TodoItem> todoItemList;
    String text = request.getParameter("text");
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    TodoItemDAO todoItemDAO = new TodoItemDAO();

    try {
      if ("make".equals(action)) {
        if (text != null && !text.isEmpty()) {
          TodoItem todoItem = new TodoItem(text);
          todoItemDAO.add(todoItem);
        } else {
          request.setAttribute("errorMsg", "Todoを入力してください。");
        }
      } else {
        todoItemDAO.updateProgress(id);

      }
      todoItemList = todoItemDAO.getAllTodoItem();
      application.setAttribute("todoItemList", todoItemList);

    } catch (Exception e) {
      e.printStackTrace();
    }

    request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);
  }

}
