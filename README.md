

### Import To Eclipse Java EE Edition

1. Download the zip of this repo and unzip it.

2. Import the project into eclipse as a maven project.

3. Config Run for development.

4. Config Run for Deployment


5. Access servlet.

   As you have the servlet at `org.example.controller.HelloServlet.java`.



### CMD

#### To develop the project.

``` bash 
mvn jetty:run
```

> The server config for development is at `pom.xml:86`.
>
> Feel free to configure it.



#### To deploy the project.

``` bash
mvn clean install exec:exec
```
> The server config for deployment is in `org.example.EmbeddingJettyStarter`.
>
> Feel free to configure it.



### Configuration of Jetty

#### Configure in coding

In `org.example.EmbeddingJettyStarter`, you can directly change the port or the context path by:

``` java
Server server = new Server(8080);		// change the port number 
// ...
context.setContextPath("/new_context");   // change the context path
// ...
```

Then you can access the servlet by:

http://155.248.227.33:8080/new_context/HelloServlet

At the same time, please make sure your servlet's annotation value mapping is valid for the context path:

``` java
@WebServlet(name = "HelloServlet", value = "HelloServlet")
```

Do not add the root in the front:

``` java
@WebServlet(name = "HelloServlet", value = "/HelloServlet")
```



Our objectives includes:  
1. Program a class that represents an Audio item as a resource.
2. Bulid a multithreaded client that simulates concurrent requests from multiple clients 
   1. The ratio of clients sending GET requests and POST requests can be changed as 2:1, 5:1 and 10:1  
      The total number of clients is 10, 50, and 100
   2. Phase 1  
      1. Returning a property value given an artist’s name
      2. Returning all the artists’ data in JSON.
   3. Phase 2  
      Record the round-trip time taken for each request.



# 1 - Data Generation
## 1.1 - Design
To be able to generate data, we need
1. Make a class that holds all the information for one audio event.  
   The class is `AudioServlet.java`


Then, we implemented `Runnable` where we generate events randomly and make them available to the posting threads using a shared `BlockingQueue`.  
1. The `Runnable` is `BlockingServlet.java` whose constructor accepts a `BlockingQueue` as parameter.
2. In the overrided `run()` method, we generate in total 10K skier lift ride events and store them into the `BlockingQueue`.

To generate data in action, we could simply call:
```java
new Thread(producer).start();
```
This way, all posting threads could get randomly generated skier lift ride events by calling `q.take()` ([documentation for `take()` method](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#take())).

## 1.2 - Major Classes

### [`AudioServlet`](src/main/java/AudioServlet.java)  
A class being responsible for generating all audio events


### [`BlockingServlet`](src/main/java/BlockingServlet.java) 
Store all randomly genated events into a share `BlockingQueue` to make all events accessible for posting threads

### [`HelloServlet`](src/main/java/HelloServlet.java) 
A class being responsible for testing default servelet

### [`ResourceServlet`](src/main/java/ResourceServlet.java) 
It implements `Runnable` interface so that we could easily create and start a new thread doing its work specified in the overrided `run()` method. 

### [`EmbeddingJettyStarter`](src/main/java/EmbeddingJettyStarter.java) 
It starts server at 8080 port specified for given uri path.

# 2 - Multithreaded Client Sending Requests
## 2.1 - Implementation
1. The ratio of clients sending GET requests and POST requests can be changed as 2:1, 5:1 and 10:1
2. The total number of clients is 10, 50, and 100
3. Recorded the round-trip time taken for each request and the plot line chart as the y axis
represents the time in seconds and x axis represents the number of clients. Each line represents a ratio of client GET and POST.



