-- DASHBOARD: generate new star id
DELIMITER $$
CREATE PROCEDURE get_star_id()
BEGIN
    SELECT CONCAT(SUBSTR((SELECT MAX(id) FROM stars), 1, 2), (CAST(SUBSTR((SELECT MAX(id) FROM stars), 3) AS UNSIGNED)+1)) AS id;
END $$
DELIMITER ;

-- DASHBOARD: generate new movie id
DELIMITER $$
CREATE PROCEDURE get_movie_id()
BEGIN
    SELECT CONCAT(SUBSTR((SELECT MAX(id) FROM movies), 1, 2), (CAST(SUBSTR((SELECT MAX(id) FROM movies), 3) AS UNSIGNED)+1)) AS id;
END $$
DELIMITER ;

-- DASHBOARD: generate new genre id 
DELIMITER $$
CREATE PROCEDURE get_genre_id()
BEGIN
    SELECT CONCAT(CAST(SUBSTR((SELECT MAX(id) FROM genres), 1) AS UNSIGNED)+1) AS id;
END $$
DELIMITER ;

-- DASHBOARD: insert new movie
DELIMITER $$
CREATE PROCEDURE insert_movie(IN movie_id VARCHAR(10), title VARCHAR(100),  year int, director VARCHAR(100))
BEGIN
    INSERT movies VALUES (movie_id, title, year, director);
END $$
DELIMITER ;
