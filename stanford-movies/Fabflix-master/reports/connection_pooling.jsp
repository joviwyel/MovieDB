

<h3>Connection Pooling</h3>
<p>
    Our application has a custom DBConnectionManager class that delegated the (single) connection to the database.
    In order to receive a connection to the database, code would request the DBConnectionManager.getInstance().getConnection()
    method, which returned a connection for them to use.
</p>
</br>
<p>
    To use connection pooling, we simply overrode this method to return a Connection from the DataSource.
</p>
</br>
<p>
    Connection pooling was enabled in the typical fashion, by declaring the connection in our app's context.xml as such:
</p>
<br/>
<p>
    <bold>
    &lt;"Context path="/fabflix">
        &lt;Resource name="jdbc/moviedb" auth="Container" type="javax.sql.DataSource"
                  maxActive="100" maxIdle="30" maxWait="10000"
                  username="testuser" password="testpass" driverClassName="com.mysql.jdbc.Driver"
                  url="jdbc:mysql://localhost:3306/moviedb" />
    &lt;/Context>
    </bold>
</p>
    <br />

<p>
    Then, we created a DataSource in the DBConnectionManager class to allocate DBConnections:
</p>
<br />
<p align="center"><bold>
    initContext = new InitialContext();
</bold></p>
<p align="center"><bold>
    envContext = (Context) initContext.lookup("java:comp/env");
</bold></p>
<p align="center"><bold>
    dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
</bold></p>


<br />

<h3>Connection pooling with two backend servers</h3>
<p>
    Every read connection from each backend server (master/slave) goes to its own local mysql server, and so they each
    maintain their own Connection pool. The only real concern with using two backend servers to connection pool is that
    writes need to go to only the master server. Because writes are so infrequent (performed only by employees), writes establish
    their own connection directly to the ip address of the master instance and forgo connection pooling all together. This is
    not expected to have impact on performance as writes are expected to be relatively infrequent and it would be wasteful to maintain
    a separate pool dedicated to writes (e.g. on the slave).
</p>
