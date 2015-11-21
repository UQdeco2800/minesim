CREATE TABLE chunks (
  pos_x INT NOT NULL,
  pos_y INT NOT NULL,
  data BLOB(256),
  PRIMARY KEY (pos_x, pos_y)
)