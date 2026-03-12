package bean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TodoItemTest {

  @Test
  @DisplayName("引数なしコンストラクタと初期値のテスト")
  void testNoArgsConstructor() {
    TodoItem item = new TodoItem();

    assertAll(() -> assertThat(item.getId()).isEqualTo(0),
        () -> assertThat(item.getText()).isNull(),
        () -> assertThat(item.getProgress()).isEqualTo("未実施"));
  }

  @Test
  @DisplayName("引数ありコンストラクタのテスト")
  void testStringConstructor() {
    TodoItem item = new TodoItem("牛乳を買う");

    // 検証
    assertAll(() -> assertThat(item.getText()).isEqualTo("牛乳を買う"),
        () -> assertThat(item.getProgress()).isEqualTo("未実施"));
  }

  @Test
  @DisplayName("Setter/Getterのテスト")
  void testAccessors() {
    TodoItem item = new TodoItem();

    item.setId(5);
    item.setText("部屋の掃除");
    item.setProgress("完了済");

    assertAll(() -> assertThat(item.getId()).isEqualTo(5),
        () -> assertThat(item.getText()).isEqualTo("部屋の掃除"),
        () -> assertThat(item.getProgress()).isEqualTo("完了済"));
  }
}
