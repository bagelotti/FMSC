#FMSC Server

###Overview
This server services all incoming events using multithreaded environment. It was developed in Java (compiled in Java 8) and uses ExecutorService to manage our thread pool. The idea was to seperate the request handlers for events and the incoming clients, and have another independent thread process the events are ready to be published. 

Once the server starts, it will listen in for events and incoming clients. The event processing thread will start looking for event with sequence number 1 at the head of the queue. The event process worker will process all events in order by sequence number. 

Once all events have been processed, the event socket will enqueue a special event (ShutdownEvent()) to notify the process worker that all events have been received. The idea follows the poison pill pattern. The event process worker will terminate, and the ExecutorService will complete its termination safely.


The design was developed as follows:

#### Design
**Main/** : simply runs our main class

**Server/**: Our Server package holds all of our server components.The Server class is the main class that holds the ExecutorService, and runs as many threads as there are processors available. 

One thread is used only for receving the stream of Events, and enqueueing the events into a priority queue. The priority queue uses the sequence numbers of the incoming events as the priority, which means that the minimum sequence number goes to the head of the queue.

The second thread handles all of the events that are ready to be processed in the queue. That is, if the next sequnce number to process is at the head of the queue, we dequeue it and process it. This thread also contains synchronized hashtable of users to publish the events to the proper output stream.

The remaining threads are used to connect the incoming clients as fast as possible. 

**Event/**: contains the different classes and interface of an Event. The classes are seperated to construct individual objects of them, since they do not share the same parameters. Within this, there is a special event called the ShutdownEvent, which is used to notify the event processing worker that all events are complete.

**User/**: represents an incoming user. A user can be an offline user or an online user. An offline user is created when an event related to them has happened but they're not connected. I figured we need to keep track of who follows these users even if they're offline. These users can become 'online users' once they connect through the client port and we can store their connection in the User class.

**test/**: contains all of our unit tests




### Build instructions

#### Run conventionally in the terminal
```
$ cd /to/directory of your choice
$ git clone https://github.com/bagelotti/FMSC.git
$ cd FMSC/
$ cd src/main/java/
$ javac Main/main.java
$ java Main.main
$ running
```



####Recommended: run using Maven (or for unit tests):
Although running the server in the terminal manually using javac is perfectly fine, I recommend using Maven (if you have it installed) only for the reason of the test scripts executing correctly. 

```
$ cd FMSC/
$ mvn package

```
- Maven will create the project and run all of the unit tests
- EventSocketTest will take about 30 seconds to finish since it expects a timeout exception

At the end of the package build, you should see:
 
```
[INFO] Building jar: /Users/anon/programming/testJava/FMSC/target/Follower-Server-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 33.598 s
[INFO] Finished at: 2016-09-23T21:29:20-04:00
[INFO] Final Memory: 21M/303M
[INFO] ------------------------------------------------------------------------
```
After the build is complete, just type in:

```
$ java -cp target/Follower-Server-1.0-SNAPSHOT.jar Main/main
$ running
```


##### Additional tests
I ran additional tests outside of the unit tests to check for performance issues. I ran mulitple settings of the parameters from the .sh file, and I also profiled each of the components timing. For example, every few seconds, I monitored the queue size to look for any bottlenecks. I found that the majority of the time the queue size was relatively low, or empty. I also checked to make sure clients connected as fast possible. The only bottleneck I found was the incoming events stream. I tried mulitple ways to enqueue the events as fast possible, but didnt find any significant performance difference between them and the current implementation. 
#### Notes and Concerns
All of the test files will most likely not run correctly outside of a maven environment since I used maven to handle my dependencies for testing which include JUnit, Mockito, JUnitParams, and Hamcrest. The server itself should not be affected whether you use Maven or not since it doesn't have any dependencies outside of the java library. 

- The server was compiled using the latest version of Java (java 8)
- I had a lot of fun doing this coding assignment. This was definitely one of the more enjoyable ones that I've completed.
- I purposely named this project FMSC so that other users cannot search the web and get a solution to this problem
- all commits and pushes were done under random names, and an anonymous email address was used
