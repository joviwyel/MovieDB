
DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;

USE moviedb;

 CREATE TABLE IF NOT EXISTS movies (
    id          INTEGER         NOT NULL    AUTO_INCREMENT,
    title       VARCHAR(100)    NOT NULL,
    year        INTEGER         NOT NULL,
    director    VARCHAR(100)    NOT NULL,
    banner_url  VARCHAR(200)    NULL,
    trailer_url VARCHAR(200)    NULL,
    FULLTEXT KEY title (title),
    PRIMARY KEY (id)
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS stars (
    id          INTEGER         NOT NULL    AUTO_INCREMENT,
    first_name  VARCHAR(50)     NOT NULL,
    last_name   VARCHAR(50)     NOT NULL,
    dob         DATE            NULL,
    photo_url   VARCHAR(200)    NULL,
    PRIMARY KEY (id)
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS stars_in_movies (
    star_id     INTEGER     NOT NULL,
    movie_id    INTEGER     NOT NULL,
    FOREIGN KEY (star_id)       REFERENCES stars(id)    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (movie_id)      REFERENCES movies(id)   ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS genres (
    id          INTEGER     NOT NULL    AUTO_INCREMENT,
    name        VARCHAR(32)     NOT NULL,
    PRIMARY KEY(id)
) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS genres_in_movies (
    genre_id    INTEGER     NOT NULL,
    movie_id    INTEGER     NOT NULL,
    FOREIGN KEY (genre_id)      REFERENCES genres(id)   ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (movie_id)      REFERENCES movies(id)   ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(genre_id),
    INDEX(movie_id)
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS creditcards (
    id          VARCHAR(20)     NOT NULL,
    first_name  VARCHAR(50)     NOT NULL,
    last_name   VARCHAR(50)     NOT NULL,
    expiration  DATE            NOT NULL,
    PRIMARY KEY (id)
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS customers (
    id          INTEGER         NOT NULL    AUTO_INCREMENT,
    first_name  VARCHAR(50)     NOT NULL,
    last_name   VARCHAR(50)     NOT NULL,
    cc_id       VARCHAR(20)     NOT NULL,
    address     VARCHAR(200)    NOT NULL,
    email       VARCHAR(50)     NOT NULL,
    password    VARCHAR(20)     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cc_id)         REFERENCES creditcards(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(cc_id)
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS sales (
    id          INTEGER         NOT NULL    AUTO_INCREMENT,
    customer_id INTEGER         NOT NULL,
    movie_id    INTEGER         NOT NULL,
    sale_date   DATE            NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id)   REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (movie_id)      REFERENCES movies(id)   ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(customer_id),
    INDEX(movie_id)
 ) ENGINE = MYISAM;

 CREATE TABLE IF NOT EXISTS employees (
    email       VARCHAR(50),
    password    VARCHAR(20)     NOT NULL,
    fullname    VARCHAR(100),
    PRIMARY KEY(email)
 ) ENGINE = MYISAM;

INSERT INTO employees VALUES ('classta@course.edu', 'classta', 'TA CS122B');
INSERT INTO employees VALUES ('a@email.com', 'a2', 'USER A');

source data.sql;


DROP PROCEDURE IF EXISTS add_movie;

DELIMITER $$
-- The out parameters can be used to display the changes made in the database
CREATE PROCEDURE add_movie (
    IN new_movie_title VARCHAR(100),
    IN new_movie_year INTEGER,
    IN new_movie_director VARCHAR(100),
    IN new_movie_banner_url VARCHAR(200),
    IN new_movie_trailer_url VARCHAR(200),
    IN new_genre_name VARCHAR(32),
    IN new_star_first_name VARCHAR(50),
    IN new_star_last_name VARCHAR(50),
    IN new_star_dob DATE,
    IN new_star_photo_url VARCHAR(200)
    -- OUT movie_count INTEGER,
    -- OUT genre_count INTEGER,
    -- OUT star_count INTEGER,
    -- OUT ref_movie_id INTEGER,
    -- OUT ref_genre_id INTEGER,
    -- OUT ref_star_id INTEGER,
    -- OUT genres_in_movies_count INTEGER,
    -- OUT stars_in_movies_count INTEGER
)
BEGIN

    -- Variables, can be used as out parameters.
    DECLARE movie_count INTEGER DEFAULT 0;
    DECLARE genre_count INTEGER DEFAULT 0;
    DECLARE star_count INTEGER DEFAULT 0;
    DECLARE ref_movie_id INTEGER DEFAULT 0;
    DECLARE ref_genre_id INTEGER DEFAULT 0;
    DECLARE ref_star_id INTEGER DEFAULT 0;
    DECLARE genres_in_movies_count INTEGER DEFAULT 0;
    DECLARE stars_in_movies_count INTEGER DEFAULT 0;

    -- Exit handlers for transaction.
    DECLARE exit handler for sqlexception
      BEGIN
        -- ERROR
      ROLLBACK;
    END;

    DECLARE exit handler for sqlwarning
     BEGIN
        -- WARNING
     ROLLBACK;
    END;

    START TRANSACTION;

        -- USE the count method to determine if a movie, genre, or star is already in the database.
        CASE
            WHEN (new_movie_banner_url IS NULL AND new_movie_trailer_url IS NULL) THEN
                SET movie_count = (
                    SELECT count(*) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url IS NULL AND
                        m.trailer_url IS NULL);
            WHEN (new_movie_banner_url IS NULL AND new_movie_trailer_url IS NOT NULL) THEN
                SET movie_count = (
                    SELECT count(*) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url IS NULL AND
                        m.trailer_url = new_movie_trailer_url);
            WHEN (new_movie_banner_url IS NOT NULL AND new_movie_trailer_url IS NULL) THEN
                SET movie_count = (
                    SELECT count(*) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url = new_movie_banner_url AND
                        m.trailer_url IS NULL);
            WHEN (new_movie_banner_url IS NOT NULL AND new_movie_trailer_url IS NOT NULL) THEN
                SET movie_count = (
                    SELECT count(*) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url = new_movie_banner_url AND
                        m.trailer_url = new_movie_trailer_url);
        END CASE;

        SET genre_count = (
            SELECT count(*) FROM genres AS g WHERE
            g.name = new_genre_name);

        -- Both star's DOB and star's photo_url are possibly NULL.
        CASE
            WHEN (new_star_dob IS NULL AND new_star_photo_url IS NULL) THEN
                SET star_count = (
                    SELECT count(*) FROM stars AS s WHERE
                    s.first_name = new_star_first_name AND
                    s.last_name = new_star_last_name AND
                    s.dob IS NULL AND
                    s.photo_url IS NULL);
            WHEN (new_star_dob IS NULL AND new_star_photo_url IS NOT NULL) THEN
                SET star_count = (
                    SELECT count(*) FROM stars AS s WHERE
                    s.first_name = new_star_first_name AND
                    s.last_name = new_star_last_name AND
                    s.dob IS NULL AND
                    s.photo_url = new_star_photo_url);
            WHEN (new_star_dob IS NOT NULL AND new_star_photo_url IS NULL) THEN
                SET star_count = (
                    SELECT count(*) FROM stars AS s WHERE
                    s.first_name = new_star_first_name AND
                    s.last_name = new_star_last_name AND
                    s.dob = new_star_dob AND
                    s.photo_url IS NULL);
            WHEN (new_star_dob IS NOT NULL AND new_star_photo_url IS NOT NULL) THEN
                SET star_count = (
                    SELECT count(*) FROM stars AS s WHERE
                    s.first_name = new_star_first_name AND
                    s.last_name = new_star_last_name AND
                    s.dob = new_star_dob AND
                    s.photo_url = new_star_photo_url);
        END CASE;
        
        --  If we do not have a matching film, we must add the film to the movies table
        IF movie_count = 0 THEN
            INSERT INTO movies (title, year, director, banner_url, trailer_url) 
                VALUES (new_movie_title, new_movie_year, new_movie_director, new_movie_banner_url, new_movie_trailer_url);
        END IF;
        
        --  If we do not have a matching genre, we must add the genre to the genres table
        IF genre_count = 0 THEN
            INSERT INTO genres (name) 
                VALUES (new_genre_name);
        END IF;
        
        --  If we do not have a matching star, we must add the star to the stars table
        IF star_count = 0 THEN
            INSERT INTO stars (first_name, last_name, dob, photo_url) 
                VALUES (new_star_first_name, new_star_last_name, new_star_dob, new_star_photo_url);
        END IF;
        
        -- Now to create links in the stars_in_movies, and genres_in_movies we must create references
        -- to the movie, star, and genre. We use max on the id, to get the latest addition to the tables
        CASE
            WHEN (new_movie_banner_url IS NULL AND new_movie_trailer_url IS NULL) THEN
                SET ref_movie_id = (
                    SELECT max(id) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url IS NULL AND
                        m.trailer_url IS NULL);
            WHEN (new_movie_banner_url IS NULL AND new_movie_trailer_url IS NOT NULL) THEN
                SET ref_movie_id = (
                    SELECT max(id) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url IS NULL AND
                        m.trailer_url = new_movie_trailer_url);
            WHEN (new_movie_banner_url IS NOT NULL AND new_movie_trailer_url IS NULL) THEN
                SET ref_movie_id = (
                    SELECT max(id) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url = new_movie_banner_url AND
                        m.trailer_url IS NULL);
            WHEN (new_movie_banner_url IS NOT NULL AND new_movie_trailer_url IS NOT NULL) THEN
                SET ref_movie_id = (
                    SELECT max(id) FROM movies AS m WHERE
                        m.title = new_movie_title AND
                        m.year = new_movie_year AND
                        m.director = new_movie_director AND
                        m.banner_url = new_movie_banner_url AND
                        m.trailer_url = new_movie_trailer_url);
        END CASE;
        
        SET ref_genre_id = (SELECT max(id) FROM genres AS g WHERE
            g.name = new_genre_name);
        
        CASE
            WHEN (new_star_dob IS NULL AND new_star_photo_url IS NULL) THEN
                SET ref_star_id = (
                    SELECT max(id) FROM stars AS s WHERE
                        s.first_name = new_star_first_name AND
                        s.last_name = new_star_last_name AND
                        s.dob IS NULL AND
                        s.photo_url IS NULL);
            WHEN (new_star_dob IS NULL AND new_star_photo_url IS NOT NULL) THEN
                SET ref_star_id = (
                    SELECT max(id) FROM stars AS s WHERE
                        s.first_name = new_star_first_name AND
                        s.last_name = new_star_last_name AND
                        s.dob IS NULL AND
                        s.photo_url = new_star_photo_url);
            WHEN (new_star_dob IS NOT NULL AND new_star_photo_url IS NULL) THEN
                SET ref_star_id = (
                    SELECT max(id) FROM stars AS s WHERE
                        s.first_name = new_star_first_name AND
                        s.last_name = new_star_last_name AND
                        s.dob = new_star_dob AND
                        s.photo_url IS NULL);
            WHEN (new_star_dob IS NOT NULL AND new_star_photo_url IS NOT NULL) THEN
                SET ref_star_id = (
                    SELECT max(id) FROM stars AS s WHERE
                        s.first_name = new_star_first_name AND
                        s.last_name = new_star_last_name AND
                        s.dob = new_star_dob AND
                        s.photo_url = new_star_photo_url);
        END CASE;

        -- Now we use the count method to see if the links already exist for stars and genres with movies,
        -- if they do not exist we create them
        SET genres_in_movies_count = (
            SELECT count(*) FROM genres_in_movies AS gmc WHERE 
                gmc.genre_id = ref_genre_id AND 
                gmc.movie_id = ref_movie_id);
        
        IF genres_in_movies_count = 0 THEN
            INSERT INTO genres_in_movies (genre_id, movie_id) VALUES (ref_genre_id, ref_movie_id);
        END IF;

        SET stars_in_movies_count = (
            SELECT count(*) FROM stars_in_movies AS smc WHERE 
                smc.star_id = ref_star_id AND
                smc.movie_id = ref_movie_id);

        IF stars_in_movies_count = 0 THEN 
            INSERT INTO stars_in_movies (star_id, movie_id) VALUES (ref_star_id, ref_movie_id);
        END IF;

    COMMIT;
END
$$

DELIMITER ;





