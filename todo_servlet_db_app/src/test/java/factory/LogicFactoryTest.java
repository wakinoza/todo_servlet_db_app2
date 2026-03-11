package factory;

import static org.assertj.core.api.Assertions.*;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.LoginLogic;
import model.TodoItemLogic;

class LogicFactoryTest {

  @Test
  @DisplayName("createLoginLogicが正しい型を生成すること")
  void testCreateLoginLogic() {
    Object result = LogicFactory.createLoginLogic();
    assertThat(result).isExactlyInstanceOf(LoginLogic.class);
  }

  @Test
  @DisplayName("createTodoItemLogicが正しい型を生成すること")
  void testCreateTodoItemLogic() {
    Object result = LogicFactory.createTodoItemLogic();
    assertThat(result).isExactlyInstanceOf(TodoItemLogic.class);
  }

  @Test
  @DisplayName("privateコンストラクタの網羅（カバレッジ100%用）")
  void testPrivateConstructor() throws Exception {
    Constructor<LogicFactory> constructor = LogicFactory.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
    } catch (Exception e) {
      assertThat(e.getCause()).isInstanceOf(AssertionError.class);
    }
  }

}
