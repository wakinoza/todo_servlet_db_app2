package bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Progress;

/**
 * . todo情報を保持するクラス
 */
public class TodoItem {
  /** . JSONファイルのファイル名 */
  private String fileName;

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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    LocalDateTime now = LocalDateTime.now();
    this.fileName = formatter.format(now);
    this.text = text;
    this.progress = Progress.PENDING;
  }

  /** . getterメソッド */
  public String getFileName() {
    return this.fileName;
  }

  public String getText() {
    return this.text;
  }

  public Progress getProgress() {
    return this.progress;
  }

  /** . setterメソット */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setProgress(Progress progress) {
    this.progress = progress;
  }
}

