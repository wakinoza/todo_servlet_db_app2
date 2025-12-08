package model;

import java.util.List;
import bean.TodoItem;
import dao.TodoItemDAO;

/**
 * . toditemインスタンスの処理を行うビジネスクラス
 */
public class TodoItemLogic {

  /**
   * . todoItemインスタンスを作成するめそっど
   *
   * @param text テキスト入力欄の文字列情報
   * @return 作成に成功すればTodoItemインスタンスを、失敗すればnullを返す
   */
  public TodoItem create(String text) {
    if (text != null && !text.isEmpty()) {
      TodoItem todoItem = new TodoItem(text);
      return todoItem;

    } else {
      return null;
    }
  }

  /**
   * . 指定されたTodoItemインスタンスをDBに挿入するメソッド
   *
   * @param todoItem 追加するTodoItemインスタンス
   * @return 挿入が成功したかを示す真偽値
   */
  public boolean add(TodoItem todoItem) {
    TodoItemDAO todoItemDao = new TodoItemDAO();
    return todoItemDao.insert(todoItem);
  }

  /**
   * . 指定されたTodoItemインスタンスの進捗情報を更新するメソッド
   *
   * @param id TodoItemインスタンスのID
   * @return 更新が成功したかを示す真偽値
   */
  public boolean updateProgress(String id) {
    TodoItemDAO todoItemDao = new TodoItemDAO();
    return todoItemDao.updateProgress(id);
  }

  /**
   * . すべてのTodoItemインスタンスを取得するメソッド
   *
   * @return TodoItemインスタンスを格納したList
   */
  public List<TodoItem> getAllTodoItem() {
    TodoItemDAO todoItemDao = new TodoItemDAO();
    return todoItemDao.selectAll();
  }
}
