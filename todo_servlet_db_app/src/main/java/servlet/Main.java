package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.TodoItemLogic;
import bean.TodoItem;
import bean.User;

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
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Content-Security-Policy",
        "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline';");

    HttpSession session = request.getSession();
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      response.sendRedirect("index.jsp");
      return;
    }

    TodoItemLogic todoItemLogic = new TodoItemLogic();
    List<TodoItem> todoItemList = todoItemLogic.getAllTodoItem();

    if (todoItemList == null) {
      todoItemList = new ArrayList<>();
    }
    request.setAttribute("todoItemList", todoItemList);
    request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);


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

    String text = request.getParameter("text");
    String action = request.getParameter("action");

    TodoItemLogic todoItemLogic = new TodoItemLogic();

    if ("create".equals(action)) {
      TodoItem todoItem = todoItemLogic.create(text);
      if (todoItem == null) {
        request.setAttribute("errorMsg", "Todoを入力してください。");
      } else {
        if (!todoItemLogic.add(todoItem)) {
          request.setAttribute("errorMsg", "Todoの追加に失敗しました。");
        }
      }

    } else {
      String idString = request.getParameter("id");
      int id = Integer.parseInt(idString);
      if (!todoItemLogic.updateProgress(id)) {
        request.setAttribute("errorMsg", "進捗の更新に失敗しました。");
      }

    }

    List<TodoItem> todoItemList = todoItemLogic.getAllTodoItem();
    request.setAttribute("todoItemList", todoItemList);
    request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);
  }

}
