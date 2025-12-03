package bean;

import model.Progress;

/**
 * . todo情報を保持するクラス
 */
public class TodoItem {
  /** . id */
  private String id;

  /** . テキスト入力欄の文字列情報 */
  private String text;

  /** . 進捗情報を文字する列挙子 */
  private Progress progress;

  /**
   * . 引数なしのコンストラクタ
   *
   */
  public TodoItem() {}

  /**
   * . コンストラクタ
   *
   * @param text テキスト入力欄の文字列情報
   */
  public TodoItem(String text) {
    this.text = text;
    this.progress = Progress.PENDING;
  }

  /** . getterメソッド */
  public String getId() {
    return this.id;
  }

  public String getText() {
    return this.text;
  }

  public Progress getProgress() {
    return this.progress;
  }

  /** . setterメソット */
  public void setId(String id) {
    this.id = id;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setProgress(Progress progress) {
    this.progress = progress;
  }
}

