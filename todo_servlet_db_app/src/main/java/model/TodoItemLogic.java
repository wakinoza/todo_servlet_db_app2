package model;

import dao.TodoItemDAO;
import java.util.List;
import bean.TodoItem;

/**
 * . toditemインスタンスの処理を行うビジネスクラス
 */
public class TodoItemLogic {
  // バリデーション用の定数
  private static final int MAX_TEXT_LENGTH = 100;

  /**
   * . todoItemインスタンスを作成するめそっど
   *
   * @param text テキスト入力欄の文字列情報
   * @return 作成に成功すればTodoItemインスタンスを、失敗すればnullを返す
   */
  public TodoItem create(String text) {

    if (text == null || text.trim().isEmpty()) {
      return null;
    }

    if (text.length() > MAX_TEXT_LENGTH) {
      return null;
    }

    return new TodoItem(text);
  }

  /**
   * . 指定されたTodoItemインスタンスをDBに挿入するメソッド
   *
   * @param todoItem 追加するTodoItemインスタンス
   * @return 挿入が成功したかを示す真偽値
   */
  public boolean add(TodoItem todoItem) {
    if (todoItem == null || todoItem.getText() == null) {
      return false;
    }
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
    if (id == null || !id.matches("^[0-9]+$")) {
      return false;
    }
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
