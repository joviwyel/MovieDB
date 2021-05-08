CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'testpass'
GRANT ALL ON *.* TO 'testuser'@'localhost';
FLUSH PRIVILEGES;

CREATE USER 'classta'@'localhost' IDENTIFIED BY 'classta';
GRANT ALL ON *.* TO 'classta'@'localhost';
FLUSH PRIVILEGES;
