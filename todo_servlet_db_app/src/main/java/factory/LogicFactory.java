package factory;

import dao.UserDAO;
import model.LoginLogic;

/**
 * . Logicクラスのインスタンス生成を管理するFactory
 */
public class LogicFactory {

  /**
   * . LoginLogicのインスタンスを生成して返す
   */
  public static LoginLogic createLoginLogic() {
    return new LoginLogic(new UserDAO());
  }
}
