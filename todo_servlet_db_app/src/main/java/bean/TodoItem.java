package bean;


/**
 * . todo情報を保持するクラス
 */
public class TodoItem {
  /** . id */
  private String id;

  /** . テキスト入力欄の文字列情報 */
  private String text;

  /** . 進捗情報を文字する列挙子 */
  private String progress = "未実施";

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
  }

  /** . getterメソッド */
  public String getId() {
    return this.id;
  }

  public String getText() {
    return this.text;
  }

  public String getProgress() {
    return this.progress;
  }

  /** . setterメソット */
  public void setId(String id) {
    this.id = id;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setProgress(String progress) {
    this.progress = progress;
  }
}

