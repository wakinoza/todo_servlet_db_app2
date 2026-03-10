package factory;

import dao.TodoItemDAO;
import dao.UserDAO;
import model.LoginLogic;
import model.TodoItemLogic;

/**
 * . Logicクラスのインスタンス生成を管理するFactory
 */
public class LogicFactory {

  /**
   * . LoginLogicのインスタンスを生成して返す
   * 
   * @return LoginLogicのインスタンス
   */
  public static LoginLogic createLoginLogic() {
    return new LoginLogic(new UserDAO());
  }

  /**
   * . TodoItemLogicのインスタンスを生成して返す
   *
   * @return TodoItemLogicのインスタン
   */
  public static TodoItemLogic createTodoItemLogic() {
    return new TodoItemLogic(new TodoItemDAO());
  }
}
