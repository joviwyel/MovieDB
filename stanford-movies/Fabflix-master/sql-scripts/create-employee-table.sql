
use moviedb;

CREATE TABLE `moviedb`.`employees` (
  `email` varchar(50),
  `password` VARCHAR(20) NOT NULL,
  `fullname` varchar(100),
  PRIMARY KEY (`email`)
);

INSERT INTO employees VALUES ('classta@course.edu', 'classta', 'TA CS122B');
INSERT INTO employees VALUES ('a@email.com', 'a2', 'USER A');

