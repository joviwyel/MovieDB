STOP SLAVE;
-- MySQL dump 10.13  Distrib 5.5.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: 
-- ------------------------------------------------------
-- Server version	5.5.49-0ubuntu0.14.04.1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `moviedb`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `moviedb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `moviedb`;

--
-- Dumping routines for database 'moviedb'
--
/*!50003 DROP PROCEDURE IF EXISTS `add_movie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_movie`(
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
	
	
	
	
	
	
	
	
)
BEGIN

	
	DECLARE movie_count INTEGER DEFAULT 0;
	DECLARE genre_count INTEGER DEFAULT 0;
	DECLARE star_count INTEGER DEFAULT 0;
	DECLARE ref_movie_id INTEGER DEFAULT 0;
	DECLARE ref_genre_id INTEGER DEFAULT 0;
	DECLARE ref_star_id INTEGER DEFAULT 0;
	DECLARE genres_in_movies_count INTEGER DEFAULT 0;
	DECLARE stars_in_movies_count INTEGER DEFAULT 0;

	
	DECLARE exit handler for sqlexception
	  BEGIN
	    
	  ROLLBACK;
	END;

	DECLARE exit handler for sqlwarning
	 BEGIN
	    
	 ROLLBACK;
	END;

	START TRANSACTION;

		
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
		
		
		IF movie_count = 0 THEN
			INSERT INTO movies (title, year, director, banner_url, trailer_url) 
				VALUES (new_movie_title, new_movie_year, new_movie_director, new_movie_banner_url, new_movie_trailer_url);
		END IF;
		
		
		IF genre_count = 0 THEN
			INSERT INTO genres (name) 
				VALUES (new_genre_name);
		END IF;
		
		
		IF star_count = 0 THEN
			INSERT INTO stars (first_name, last_name, dob, photo_url) 
				VALUES (new_star_first_name, new_star_last_name, new_star_dob, new_star_photo_url);
		END IF;
		
		
		
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
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Current Database: `mysql`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `mysql` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `mysql`;

--
-- Dumping routines for database 'mysql'
--

--
-- Current Database: `pets`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pets` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `pets`;

--
-- Dumping routines for database 'pets'
--

--
-- Current Database: `project4`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `project4` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `project4`;

--
-- Dumping routines for database 'project4'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-06  1:40:06
START SLAVE;
