CREATE TABLE TRADE_LIST
(
    LIST_ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CREATION_TIME DATETIME NOT NULL,
    STATE VARCHAR(255) NOT NULL
);

ALTER TABLE TRADE_ITEM ADD TRADE_LIST_ID BIGINT;
ALTER TABLE TRADE_ITEM ADD CONSTRAINT FK_TRADE_LIST_ID FOREIGN KEY (TRADE_LIST_ID) REFERENCES TRADE_LIST(LIST_ID);