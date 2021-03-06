Problem
-
We would like to have a restful API for our statistics. The main use case for our API is to calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is called every time a transaction is made. It is also the sole input of this rest API. The other onereturns the statistic based of the transactions of the last 60 seconds.

Solution
-
The main requirement is achieved by aggregating statistics on per-second basis. Solution is O(1), executes in constant time and space.
In addition, this solution require very small dependencies as I have chosen not to use Spring MVC (nevertheless I know Spring, Spring Boot, etc). This is a real "micro" service. :)
    
Model layer is implemented as API [ru.eamosov.n26.api.StatsService](https://eamosov.github.io/n26/) with two realisations:
   - [StatsServiceImpl](src/main/java/ru/eamosov/n26/impl/StatsServiceImpl.java) - core logic which calculates statistics
   - [StatsServiceRest](src/main/java/ru/eamosov/n26/impl/StatsServiceRest.java) - REST client used for testing purpose 

Controller layer is implemented as two REST controllers in [ru.eamosov.n26.rest](src/main/java/ru/eamosov/n26/rest) package which are registered as endpoints in single Jetty handler [JettyRestHandler](src/main/java/ru/eamosov/n26/jetty/JettyRestHandler.java).

Building and testing:   
```
$ mvn clean install
```

Running from command line:  
```
$ mvn exec:java  
```

The program starts HTTP server at port 8080 and provides REST interface:  
http://localhost:8080/statistics  
http://localhost:8080/transaction  

Example:  
1) Get statistics:  
```
$ curl http://localhost:8080/statistics  
```
  
2) add transaction:  
```
$ curl -v -d "{\"amount\":13, \"timestamp\":`date +%s`000}" http://localhost:8080/transaction  
```  

3) add invalid transaction:  
```
$ curl -v -d "{\"amount\":13, \"timestamp\":1528143401000}" http://localhost:8080/transaction  
```  

3) Get statistics:  
```
$ curl http://localhost:8080/statistics  
```


