DROP DATABASE IF EXISTS  SPP;
DROP USER IF EXISTS  capstone;
create user capstone identified WITH mysql_native_password  by 'capstone';
create database SPP;
grant all privileges on SPP.* to capstone with grant option;
commit;

USE SPP;

CREATE TABLE CARD (
	card VARCHAR(12) PRIMARY KEY,
    expiration_month INTEGER,
    expiration_year INTEGER,
    cvc INTEGER
);

CREATE TABLE SPP_USER (
  user_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id VARCHAR(20),
  pw VARCHAR(20),
  name VARCHAR(20),
  phone VARCHAR(11),
  card VARCHAR(12),
  car VARCHAR(10),
  FOREIGN KEY (card) REFERENCES CARD(card)
);

CREATE TABLE PAYMENT_HISTORY (
	id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	pay_date DATETIME,
    payment INTEGER,
    user_id INTEGER,
    card VARCHAR(12),
    car VARCHAR(10),
    place VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES SPP_USER(user_id),
    FOREIGN KEY (card) REFERENCES CARD(card)
);


commit;