-- 1. テーブルの作成 (todo_users)
CREATE TABLE IF NOT EXISTS todo_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL UNIQUE
);

-- 2. テーブルの作成 (todoItems)
CREATE TABLE IF NOT EXISTS todoItems (
    id INT AUTO_INCREMENT PRIMARY KEY,
    text TEXT NOT NULL,
    progress VARCHAR(5) NOT NULL
);

-- 3. 初期ユーザーデータの投入 
-- パスワードはハッシュ化済み
INSERT INTO todo_users (name, password) 
VALUES ('yamada','\$2a\$12\$N/k4HeW.r1u3NLg9Q8piYOecakaLtmAdOVOIm7xJlHdcZ.tA2qMMW')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 4. 参照用ユーザーの作成と権限付与
CREATE USER IF NOT EXISTS '${DB_USER_READONLY}'@'%' IDENTIFIED BY '${DB_PASSWORD_READONLY}';
GRANT SELECT ON test_db.* TO '${DB_USER_READONLY}'@'%';

-- 5. 更新用ユーザーの作成と権限付与
CREATE USER IF NOT EXISTS '${DB_USER_WRITEONLY}'@'%' IDENTIFIED BY '${DB_PASSWORD_WRITEONLY}';
GRANT SELECT, INSERT, UPDATE, DELETE ON test_db.* TO '${DB_USER_WRITEONLY}'@'%';

FLUSH PRIVILEGES;