use moviedb;

CREATE TABLE customers_backup(
id INT NOT NULL AUTO_INCREMENT,
firstName VARCHAR(50) DEFAULT '',
lastName VARCHAR(50) DEFAULT '',
ccId VARCHAR(20) DEFAULT '',
address VARCHAR(200) DEFAULT '',
email VARCHAR(50) DEFAULT '',
password VARCHAR(20) DEFAULT '',
PRIMARY KEY(id),
FOREIGN KEY(ccId) REFERENCES creditcards(id)
);

insert into customers_backup select * from customers;