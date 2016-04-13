DELETE FROM ITEM_GROUP;
DROP TABLE ITEM_GROUP;

ALTER TABLE TRADE_ITEM MODIFY OWNER_ID BIGINT NULL;

CREATE TABLE GROUP_ITEM
(
  ID BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  GROUP_ITEM_ID BIGINT NOT NULL,
  TRADE_ITEM_ID BIGINT NOT NULL,
  FOREIGN KEY (GROUP_ITEM_ID) REFERENCES TRADE_ITEM(ID),
  FOREIGN KEY (TRADE_ITEM_ID) REFERENCES TRADE_ITEM(ID)
);
