# Fabflix
Fabflix is a Netflix like website for buying movies, done as a project with two teammates (Carolyn Jung and Minh Phan) for CS122B at UC Irvine. It was my first attempt at a larger sized web application. It supports user authentication, including user account logins and captcha's to prevent bot access. User's can query for movies using a simple type-ahead input field that includes autocomplete search suggestions via AJAX requests. 

## Database ETL Process
#### XML Parsing
The MySQL database was populated from XML files, using a custom SAX XML parser to extract, transform, and load the data into the RDBMS. All XML files were initially validated using DTD files to ensure clean data was used, although this requirement was later relaxed and the system was advanced to attempt to also gather data from potentially unsanitized sources. 

#### Stored Procedures
We also wrote a stored procedure in SQL to efficiently scale the process of adding extracted information from the files into multiple tables atomically.

#### Connection Pooling and Prepared Statements
We used connection pooling via the JDBC to scale the number of concurrent read requests that each datastore could serve concurrently. Prepared statements were used to increase the performance of all particularly expensive or often run queries.

## Server and Database Configuration
#### Server Cluster
The Fabflix website was hosted on a small cluster of AWS EC2 instances running Ubuntu Linux. Each EC2 instance runs its own Apache Tomcat server to serve requests forwarded to them via an Apache2 HTTP server acting as a load balancer. Once a user has a session, their subsequent requests are always sent to the same server to maintain consistency. The application supported both HTTP and HTTPS, forwarding all HTTP requests to the more secure HTTPS server to protect user privacy. A domain name was purchased from GoDaddy.

#### Database
The datastore for this project is MySQL, which is replicated on each server for redundancy in a master-slave configuration. Any machine could process reads, but all writes were forwarded from slaves to the master instance to ensure consistency. The master then propagates the changes via a broadcast to the slaves.

## Application Stack
The web application uses Java servlets as provided by Apache Tomcat and defined in the JEE specification. All content is served from java server pages (JSP), using the JSP expression language to avoid the use of scriptlets. For connectivity to the datastore, JDBC is used. The application also makes use of Bootstrap, as well as jQuery to make AJAX calls and provide tooltips. The Google GSON library was used to easily serialize Java objects into JSON and the Google Guava library was also used.

## Performance Analytics using Apache JMeter.
Performance optimizations were measured against the search feature using Apache JMeter. The data collected from JMeter was parsed from CSV files using Python.

## Then and Now
Fabflix was my first larger sized web application and my first time using JEE. Looking back on it, there are a lot of sections that I would have handled differently. Additionally, as this project was more about learning about data storage and backend web applications, the styling of the website is incredibly minimal (or otherwise in need of a designers TLC). However, it was a really great learning experience, both in terms of working with data ETL processes from potentially disparate sources and in building a scalable web application. I also learned a great deal about DevOps and the cloud.
