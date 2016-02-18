DROP TABLE IF EXISTS web_user;
CREATE TABLE web_user(
    email           VARCHAR(128) PRIMARY KEY,
    password        VARCHAR(128)
--    password_hash   INTEGER,
--    password_salt   INTEGER
);
