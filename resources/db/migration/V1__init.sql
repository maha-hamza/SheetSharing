CREATE TABLE IF NOT EXISTS shares
(
   id            VARCHAR(50)    PRIMARY KEY    NOT NULL,
   sheet         VARCHAR(50)                   NOT NULL,
   selection     VARCHAR(100)                  NOT NULL,
   created_at    DATE                          Not NULL
);