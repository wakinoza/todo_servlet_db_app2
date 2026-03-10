package model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import dao.TodoItemDAO;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import bean.TodoItem;

class TodoItemLogicTest {

  private TodoItemDAO mockDao;
  private static TodoItemLogic todoLogic;

  @BeforeEach
  void setUp() {
    mockDao = mock(TodoItemDAO.class);
    todoLogic = new TodoItemLogic(mockDao);
  }

  // --- 正常系 ---
  @Test
  @DisplayName("全ての正常系メソッドが正しく動作すること")
  void testNormalOperations() {
    // 1. getAllTodoItem
    List<TodoItem> mockList = List.of(new TodoItem("Javaの勉強"));
    when(mockDao.selectAll()).thenReturn(mockList);
    assertThat(todoLogic.getAllTodoItem()).extracting(TodoItem::getText).containsExactly("Javaの勉強");

    // 2. create
    TodoItem created = todoLogic.create("買い物に行く");
    assertThat(created).isNotNull().extracting(TodoItem::getText).isEqualTo("買い物に行く");

    // 3. add
    TodoItem item = new TodoItem("テスト");
    when(mockDao.insert(item)).thenReturn(true);
    assertThat(todoLogic.add(item)).isTrue();

    // 4. updateProgress
    when(mockDao.updateProgress(1)).thenReturn(true);
    assertThat(todoLogic.updateProgress(1)).isTrue();
  }

  // --- 異常系・バリデーションの集約 ---
  @ParameterizedTest
  @MethodSource("provideInvalidInputs")
  @DisplayName("不正な入力に対してバリデーションが正しく機能すること")
  void testValidations(Runnable action, String description) {
    action.run();
    verify(mockDao, never()).insert(any());
    verify(mockDao, never()).updateProgress(anyInt());
  }

  private static Stream<Arguments> provideInvalidInputs() {
    return Stream.of(
        // create メソッドの異常系
        Arguments.of((Runnable) () -> assertThat(todoLogic.create(null)).isNull(),
            "create: null入力"),
        Arguments.of((Runnable) () -> assertThat(todoLogic.create("")).isNull(), "create: 空文字"),
        Arguments.of((Runnable) () -> assertThat(todoLogic.create("   ")).isNull(), "create: 空白"),
        Arguments.of((Runnable) () -> assertThat(todoLogic.create("a".repeat(101))).isNull(),
            "create: 101文字以上"),

        // add メソッドの異常系
        Arguments.of((Runnable) () -> assertThat(todoLogic.add(null)).isFalse(),
            "add: TodoItemがnull"),
        Arguments.of((Runnable) () -> assertThat(todoLogic.add(new TodoItem(null))).isFalse(),
            "add: textがnull"),

        // updateProgress メソッドの異常系
        Arguments.of((Runnable) () -> assertThat(todoLogic.updateProgress(-1)).isFalse(),
            "updateProgress: 負数ID"));
  }
}
