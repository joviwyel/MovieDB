-- DASHBOARD: generate new star id
DELIMITER $$
CREATE PROCEDURE get_id(IN last_id VARCHAR(10))
BEGIN
    SELECT CONCAT(SUBSTR(last_id, 1, 2), (CAST(SUBSTR((last_id), 3) AS UNSIGNED)+1)) AS id;
END $$
DELIMITER ;

-- DASHBOARD: generate new genre id 
DELIMITER $$
CREATE PROCEDURE get_genre_id(IN last_id VARCHAR(10))
BEGIN
    SELECT CONCAT(CAST(SUBSTR((last_id), 1) AS UNSIGNED)+1) AS id;
END $$
DELIMITER ;
