<!DOCTYPE html>
<!DOCTYPE html>
<html>
<head>
    <style>
        body {
            background-color: linen;
        }
        td {
            border-top-style: solid;
        }
    </style>
</head>
<body>

<table style="width:100%">
    <tr style="font-weight:bold; background-color: orange">
        <td width="300px">Single-instance version cases</td>
        <td>Graph Results Screenshot</td>
        <td>Average Query Time(ms)</td>
        <td>Average Search Servlet Time(ms)</td>
        <td>Average JDBC Time(ms)</td>
        <td>Analysis</td>
    </tr>
    <tr>
        <td>Case 1: HTTP/1 thread</td>
        <td><img src="../images/graph_results_case1.png" alt="Graph Results Screenshot Case 1" style="width:304px;height:228px;"></td>
        <td>73</td>
        <td>23.5501550696</td>
        <td>23.4251659001</td>
        <td>This was result set expected to be the fastest as it didnt have the overhead of going through load balancer's redirection and only has a single thread running the queries, thus utilizing the most possible resources for mysql server.</td>
    </tr>
    <tr>
        <td>Case 2: HTTP/10 threads</td>
        <td><img src="../images/graph_results_case2.png" alt="Graph Results Screenshot Case 2" style="width:304px;height:228px;"></td>
        <td>282</td>
        <td>130.182585321</td>
        <td>130.116510355</td>
        <td>The performance on the single instance decreased quit a bit with the increased thread count, as the same amount of resources (on a free tier AWS server) need to be shared amongst more threads.</td>
    </tr>
    <tr>
        <td>Case 3: HTTPS/10 threads</td>
        <td><img src="../images/graph_results_case3.png" alt="Graph Results Screenshot Case 3" style="width:304px;height:228px;"></td>
        <td>458</td>
        <td>83.04031038460001</td>
        <td>82.92235992810001</td>
        <td>The page load performance suffered when using HTTPS because of the added overhead of encrypting/decrypting the data that moves from server/client. The extra time between page loads relieves pressure on the JDBC connection pool (as it is no longer necessarily the only major bottleneck) and that's why the JDBC processes are faster.</td>
    </tr>
    <tr>
        <td>Case 4: HTTP/10 threads/No prepared statements</td>
        <td><img src="../images/graph_results_case4.png" alt="Graph Results Screenshot Case 4" style="width:304px;height:228px;"></td>
        <td>418</td>
        <td>78.47040451780001</td>
        <td>78.3623874765</td>
        <td>The database performance actually increases in this case as the overhead of caching the execution plan for later use is removed / using the prepared statement and setting the bind parameters. Here, we just use string construction to create the query/statement. As we only call the prepared statement once, we get no performance gain.</td>
    </tr>
    <tr>
        <td>Case 5: HTTP/10 threads/No connection pooling</td>
        <td><img src="../images/graph_results_case4.png" alt="Graph Results Screenshot Case 4" style="width:304px;height:228px;"></td>
        <td>330</td>
        <td>171.605920518</td>
        <td>171.413754098</td>
        <td>The database performance obviously suffers because we are not using connection pooling. This makes the page loads slower as well.</td>
    </tr>

</table>


<table style="width:100%">
    <tr style="font-weight:bold; background-color: orange">
        <td width="300px">Scaled version cases</td>
        <td>Graph Results Screenshot</td>
        <td>Average Query Time(ms)</td>
        <td>Average Search Servlet Time(ms)</td>
        <td>Average JDBC Time(ms)</td>
        <td>Analysis</td>
    </tr>
    <tr>
        <td>Case 1: HTTP/1 thread</td>
        <td><img src="../images/graph_results_case6.png" alt="Graph Results Screenshot Case 1" style="width:304px;height:228px;"></td>
        <td>92</td>
        <td>27.9087313505</td>
        <td>27.7874428637</td>
        <td>This was expected to be very fast, the page loads were a little slower than the non-scaled version because it has to go through a proxy server.</td>
    </tr>
    <tr>
        <td>Case 2: HTTP/10 threads</td>
        <td><img src="../images/graph_results_case7.png" alt="Graph Results Screenshot Case 2" style="width:304px;height:228px;"></td>
        <td>192</td>
        <td>80.58224127509999</td>
        <td>80.46350617937964</td>
        <td>Performance notably suffers due to the increased thread count on such low end AWS servers and the associated context switching overhead but these results still surpass the unscaled version in every aspect. These results are expected to be improved as the many client connections are being evenly distributed by the load balancer.</td>
    </tr>
    <tr>
        <td>Case 3: HTTP/10 threads/No prepared statements</td>
        <td><img src="../images/graph_results_case8.png" alt="Graph Results Screenshot Case 4" style="width:304px;height:228px;"></td>
        <td>205</td>
        <td>68.58223903820002</td>
        <td>68.4823571476</td>
        <td>Page load performance measured by Jmeter greatly increased when using load balancing (compared to the unscaled version). JDBC performance increases because prepared statements have an overhead of adding each parameter.</td>
    </tr>
    <tr>
        <td>Case 4: HTTP/10 threads/No connection pooling</td>
        <td><img src="../images/graph_results_case9.png" alt="Graph Results Screenshot Case 4" style="width:304px;height:228px;"></td>
        <td>393</td>
        <td>47.6090858530324752</td>
        <td>47.345243206735055708</td>
        <td>Page load performance suffered but JDBC performance increased due to the load blananced sharing pressure amongst the backend servers.</td>
    </tr>

</table>

</body>
</html>