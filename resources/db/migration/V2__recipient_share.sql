CREATE TABLE IF NOT EXISTS recipient_share
(
   id                 VARCHAR(50)    PRIMARY KEY    NOT NULL,
   share_id           VARCHAR(50)                   NOT NULL,
   recipient_email    VARCHAR(100)                  NOT NULL,
   CONSTRAINT FK_SHARE FOREIGN KEY (share_id) REFERENCES shares(id)
);