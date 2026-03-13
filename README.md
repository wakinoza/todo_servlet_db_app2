# MySQL連携 Todo アプリケーション - Unit Test Update Edition

本プロジェクトは、以前公開した「Security Update Edition」をベースに、JUnit 5 および AssertJ を導入し、単体テストを実装したアップデート版です。
ロジックの正当性を担保するため、命令網羅（Instruction Coverage）100%を達成しました。

## 🧪 アップデートの概要：テスト駆動による品質改善
実務で求められる「壊れにくいコード」を目指し、以下のテスト実装およびリファクタリングを行いました。

1. 単体テストの徹底実装
JUnit 5 / AssertJ の採用: 可読性の高いアサーションを用いたテストコードの実装。

Mockito による擬似環境の構築: DataSource や Connection をモック化し、DBの状態に依存しない高速なテストを実行。

カバレッジの追求: JaCoCo を用いて計測を行い、TodoItemDAO における全命令の実行を検証（Instruction Coverage 100%達成）。

2. テスト容易性を意識したリファクタリング
Nested構造の採用: JUnit 5 の @Nested を活用し、正常系・異常系・網羅系を構造的に整理。

リフレクションの活用: static フィールドで保持されている DataSource をテスト時に動的に差し替え、モックオブジェクトの注入を実現。

## 🛠️ 使用技術

言語： Java SE 21

フレームワーク： Servlet / JSP / JSTL

テストフレームワーク：JUnit 5 / AssertJ / Mockito / JaCoCo

DB：MySQL 8.0

コンテナ：Docker / Docker Compose

開発環境： Eclipse 2025-09 / Apache Tomcat 10 / Maven

## 🚀 起動手順 (Docker)
Dockerがインストールされた環境であれば、以下の手順で動作確認が可能です。

1. リポジトリをクローン
```bash
git clone https://github.com/wakinoza/todo_servlet_db_app2
cd todo_servlet_db_app2
```

2. 環境変数の準備 .env.example をコピーして .env を作成し、必要な値を設定してください。
```bash
cp .env.example .env
```

3. コンテナの起動
```bash
docker compose up -d --build
```

4. アクセス ブラウザで http://localhost:8080/todo/Main にアクセスしてください。
以下の情報を入力すると、ログイン可能です。

- ユーザー名：yamada
- パスワード：yamada_password

5. テストの実行
Mavenがインストールされた環境であれば、以下のコマンドでテストとレポート生成が可能です。

```bash
mvn test
```
## 🧠 苦戦した点と学んだこと

<難しかった点>
- リフレクションによるテストの実装: static フィールドへのモック注入を初めて経験し、Javaの実行時の振る舞いをより深く理解する必要がありました。

- 条件網羅（Branch Coverage）100%への挑戦:
特に DAO クラスの updateProgress メソッドにおいて、条件網羅率 100% を目指し、Mock データを駆使してあらゆるパターンを試行しました。
しかし、try-with-resources がコンパイル時に展開する「リソースが null の場合の挙動」や「例外抑制（Suppressed Exception）」など、Java の言語仕様に起因する目に見えない分岐が壁となりました。
最終的に分岐網羅は 70% 程度に留まりましたが、この過程で Java が内部でどのようにリソースを管理し、例外をハンドリングしているのか について考える良い機会になりました。

<得られた知見>
- テスト容易性（Testability）とクリーンコード: 「テストが書きにくいコードは、設計に問題がある」という事実に直面しました。テストを意識することで、メソッドの責務がより明確になり、結果としてコードがクリーンになることを実感しました。

- 内部構造への深い洞察: カバレッジ100%を目指す過程で、例外処理の伝播やリソースのクローズ処理など、Javaの言語仕様に対する理解が深まりました。

## 🔭 今後の展望
- 水平展開: 同規模の別アプリケーションに対しても、今回培ったノウハウを用いてテスト実装を施し、品質向上を図ります。

- Spring Framework への移行: Servlet/JSP で培った基礎を活かし、Spring DI/AOP 等を用いたよりモダンな開発へステップアップします。
