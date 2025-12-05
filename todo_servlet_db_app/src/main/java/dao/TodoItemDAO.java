package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bean.TodoItem;

public class TodoItemDAO extends DAO {

  public boolean insert(TodoItem todoItem) {
    try (Connection con = getConnection()) {
      String sql = "INSERT INTO todoItems (text, progress) VALUES (?, ?)";
      PreparedStatement pStmt = con.prepareStatement(sql);

      pStmt.setString(1, todoItem.getText());
      pStmt.setString(2, todoItem.getProgress());

      int result = pStmt.executeUpdate();
      if (result != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean updateProgress(String id) {
    try (Connection con = getConnection()) {
      String sql = "SELECT progress FROM todoItems WHERE id = ?";
      PreparedStatement pStmt = con.prepareStatement(sql);

      pStmt.setString(1, id);

      ResultSet rs = pStmt.executeQuery();

      String nextProgress;

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

      String sql2 = "UPDATE todoItems SET progress = ? WHERE id = ?";
      PreparedStatement pStmt2 = con.prepareStatement(sql2);

      pStmt2.setString(1, nextProgress);
      pStmt2.setString(2, id);

      int result2 = pStmt2.executeUpdate();

      if (result2 != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public List<TodoItem> selectAll() {
    List<TodoItem> todoItemList = new ArrayList<>();

    try (Connection con = getConnection()) {
      String sql = "SELECT * FROM todoItems";
      PreparedStatement pStmt = con.prepareStatement(sql);

      try (ResultSet rs = pStmt.executeQuery()) {
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

  public boolean delete(String id) {
    try (Connection con = getConnection()) {
      String sql = "DELETE FROM todoItems WHERE id = ?";
      PreparedStatement pStmt = con.prepareStatement(sql);

      pStmt.setString(1, id);

      int result = pStmt.executeUpdate();
      if (result != 1) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }



}
