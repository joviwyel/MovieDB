

<h3>Running the app</h3>
<p>We have a script called project5-compilation.sh that should compile everything.</p>

<p>But here are the steps</p>
<p># From the top level project folder, above WEB-INF, run the command:</p>
<p>
    javac -cp /usr/share/tomcat7/lib/servlet-api.jar:/usr/share/tomcat7/lib/jsp-api.jar:WEB-INF/lib/mysql-connector-java-5.0.8-bin:WEB-INF/lib/recaptcha4j-0.0.7:WEB-INF/lib/javax.json-1.0.jar:WEB-INF/lib/gson-2.6.2.jar:WEB-INF/lib/log4j-1.2.17.jar -d WEB-INF/classes/ WEB-INF/src/recaptcha/*.java WEB-INF/src/models/*.java WEB-INF/src/servlets/*.java
</p>

<p># To make the war file.</p>
<p>jar -cvf fabflix.war *</p>

<p># Deploy the war file to tomcat. Be sure to undeploy and remove fablix if it's already there.</p>
<p>sudo mv fabflix.war /var/lib/tomcat7/webapps</p>


<br />
<h3>Backend Server IPs</h3>
<p>Master instance: 52.71.243.62</p>
<p>Slave instance: 52.87.240.115</p>

<h3>Load Balancer IP</h3>
<p>Load Balancer: 52.87.104.88</p>

<br />

<h3>Running the script</h3>
<p>
    We have a python script called logparser.py stored under WEB-INF that can be used to parse the data after running a test case in apache jmeter.
    The log statements are saved in '../log/SearchPerformance.log'.
</p>
<p></p>