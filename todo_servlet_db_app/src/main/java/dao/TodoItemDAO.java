package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.TodoItem;

/** . todoItemsテーブルの操作を行うDAOクラス */
public class TodoItemDAO extends DAO {

  /**
   * . テーブルにTodoItemインスタンスの情報を挿入するメソッド
   *
   * @param todoItem テーブルに挿入するTodoItemインスタンス
   * @return 挿入操作が完了したがどうかを示す真偽値
   */
  public boolean insert(TodoItem todoItem) {
    try (Connection con = getWriteConnection()) {
      String sql = "INSERT INTO todoItems (text, progress) VALUES (?, ?)";
      try (PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, todoItem.getText());
        ps.setString(2, todoItem.getProgress());

        return ps.executeUpdate() == 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * . 指定されたTodoItemインスタンスの進捗情報を変更するメソッド
   *
   * @param id 進捗情報を更新するTodoItemインスタンスのID
   * @return 変更操作が完了したがどうかを示す真偽値
   */
  public boolean updateProgress(String id) {
    try (Connection con = getWriteConnection()) {
      String sql = "SELECT progress FROM todoItems WHERE id = ?";
      String nextProgress;

      try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, id);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            String result = rs.getString("progress");
            if (result.equals("未実施")) {
              nextProgress = "実施中";
            } else if (result.equals("実施中")) {
              nextProgress = "完了済";
            } else {
              return delete(id);
            }
          } else {
            return false;
          }
        }
      }

      String sql2 = "UPDATE todoItems SET progress = ? WHERE id = ?";
      try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
        ps2.setString(1, nextProgress);
        ps2.setString(2, id);
        return ps2.executeUpdate() == 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * . テーブルの全情報をListに変換するメソッド
   *
   * @return テーブルの全情報を格納したＬｉｓｔ
   */
  public List<TodoItem> selectAll() {
    List<TodoItem> todoItemList = new ArrayList<>();

    try (Connection con = getReadConnection()) {
      String sql = "SELECT * FROM todoItems";
      PreparedStatement ps = con.prepareStatement(sql);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          TodoItem todoItem = new TodoItem();
          todoItem.setId(rs.getString("id"));
          todoItem.setText(rs.getString("text"));
          todoItem.setProgress(rs.getString("progress"));
          todoItemList.add(todoItem);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return todoItemList;
  }

  /**
   * . 指定されたTodoItemインスタンスの情報を削除するメソッド
   *
   * @param id 削除するTodoItemインスタンスのID
   * @return 削除操作が完了したがどうかを示す真偽値
   */
  public boolean delete(String id) {
    try (Connection con = getWriteConnection()) {
      String sql = "DELETE FROM todoItems WHERE id = ?";
      PreparedStatement ps = con.prepareStatement(sql);

      ps.setString(1, id);

      int result = ps.executeUpdate();
      if (result != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

}
