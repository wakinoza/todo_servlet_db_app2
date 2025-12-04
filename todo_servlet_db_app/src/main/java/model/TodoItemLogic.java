package model;

import java.util.List;
import bean.TodoItem;
import dao.TodoItemDAO;

public class TodoItemLogic {

  public TodoItem create(String text) {
    if (text != null && !text.isEmpty()) {
      TodoItem todoItem = new TodoItem(text);
      return todoItem;

    } else {
      return null;
    }
  }

  public boolean add(TodoItem todoItem) {
    TodoItemDAO todoItemDAO = new TodoItemDAO();
    return todoItemDAO.insert(todoItem);
  }

  public boolean updateProgress(String id) {
    TodoItemDAO todoItemDAO = new TodoItemDAO();
    return todoItemDAO.updateProgress(id);
  }

  public List<TodoItem> getAllTodoItem() {
    TodoItemDAO todoItemDAO = new TodoItemDAO();
    return todoItemDAO.selectAll();
  }
}
