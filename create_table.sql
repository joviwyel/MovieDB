CREATE database moviedb;
use moviedb;

CREATE TABLE movies(
id VARCHAR(10) DEFAULT '',
title VARCHAR(100) DEFAULT '',
year INT NOT NULL,
director VARCHAR(100) DEFAULT '',
PRIMARY KEY(id)
);

CREATE TABLE stars(
id VARCHAR(10) DEFAULT '',
name VARCHAR(100) DEFAULT '',
birthYear INT,
PRIMARY KEY(id)
);

CREATE TABLE stars_in_movies(
starId VARCHAR(10) DEFAULT '',
movieId VARCHAR(10) DEFAULT '',
FOREIGN KEY(starId) REFERENCES stars(id),
FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE genres(
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(32) DEFAULT '',
PRIMARY KEY(id)
);

CREATE TABLE genres_in_movies(
genreId INT NOT NULL,
movieId VARCHAR(10) DEFAULT '',
FOREIGN KEY(genreId) REFERENCES genres(id),
FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE creditcards(
id VARCHAR(20) DEFAULT '',
firstName VARCHAR(50) DEFAULT '',
lastName VARCHAR(50) DEFAULT '',
expiration DATE NOT NULL,
PRIMARY KEY(id)
);
CREATE TABLE customers(
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


CREATE TABLE sales(
id INT NOT NULL AUTO_INCREMENT,
customerId INT NOT NULL,
movieId VARCHAR(10) DEFAULT '',
saleDate DATE NOT NULL,
PRIMARY KEY(id),
FOREIGN KEY(customerId) REFERENCES customers(id),
FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE ratings(
movieId VARCHAR(10) DEFAULT '',
rating FLOAT NOT NULL,
numVotes INT NOT NULL,
FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE employees(
email varchar(50) primary key, 
password varchar(20) NOT NULL, 
fullname varchar(100)
);
INSERT INTO employees VALUES ('classta@email.edu', 'classta', 'TA CS122B');

ALTER TABLE sales ADD COLUMN qty INT default null
ALTER TABLE movies add fulltext(title)

