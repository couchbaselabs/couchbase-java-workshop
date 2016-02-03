# Couchbase Java Workshop

In this workshop you'll learn how to use the Couchbase Java SDK and AngularJS to create a full stack web application that can perform CRUD operations via an API.

This document will help you through the process for building such an application.

## What's Included

This tutorial is based on a demo application, [try-cb-java](https://www.github.com/couchbaselabs/try-cb-java), designed to show the features of the Couchbase Java SDK.

You may be viewing this tutorial from your web browser via GitHub or on your local file system after cloning the repository.  Whatever the scenario, you should see the following files and directories:

```
initial/
finished/
images/
README.md
```

## Getting Started

In the getting started, you will get the project set up locally, verify it by running the `finished` project, then switch to the `initial` project to follow the steps which will have you learn the Couchbase Java SDK basics.

If needed, clone the application from the [try-cb-java](https://www.github.com/couchbaselabs/try-cb-java) repository:

```
git clone https://github.com/couchbaselabs/try-cb-java
```

Although not required, IntelliJ IDEA will prove to be very helpful throughout the course of this workshop.

### Creating a Couchbase Cluster with Docker

Before jumping into the code, a Couchbase Server node or cluster is required.  Docker is a great way to set up Couchbase since an official image exists for it.

From the Docker Terminal, execute the following to spin up the first Docker container:

```
docker run -d -p 1337:8091 couchbase
```

![Getting Started 9](images/getting-started-9.png)

The above will deploy a Couchbase container that will be accessible via the host computer on port **1337**.  A second container can be deployed by executing the following:

```
docker run -d -p 1338:8091 couchbase
```

![Getting Started 10](images/getting-started-10.png)

Notice how the host port changed.  This is to avoid conflicts as the Docker container shares the same IP address.  To find the Docker IP address, execute the following from the Docker Terminal:

```
docker-machine ip default
```

![Getting Started 11](images/getting-started-11.png)

With this IP address you can access both nodes from a web browser at **http://<ip>:1337** and **http://<ip>:1338**.  Starting with the container on port **1337**, treat it as a new cluster node.  Give it a password you can remember.

![Getting Started 14](images/getting-started-14.png)

Then make sure to install the **travel-sample** bucket.

![Getting Started 15](images/getting-started-15.png)

When the first container is complete, visit the second found on port **1338**.  

![Getting Started 16](images/getting-started-16.png)

This node will join a cluster rather than creating a new one, however, the IP used in the web browser is not the IP that should be used for two containers to communicate with each other.  Instead execute the following from the Docker Terminal:

```
docker network ls
```

![Getting Started 12](images/getting-started-12.png)

This will get various information about the network.  Take note of the network id for the bridge as it will be used here:

```
docker network inspect <network_id_here> | grep IPv4Address
```

Two IP addresses should be returned.  Since this is the second container deployed, note the IP with the largest final IP octet.  This is the IP address to enter in the Couchbase configuration.

![Getting Started 13](images/getting-started-13.png)

With the two nodes joined in the same cluster, the cluster must be re-balanced.  This could take some time as Docker is running locally via Virtual Box.

When the re-balance has completed, the server is ready for use.

### Using IntelliJ

Using IntelliJ, choose **Import Project** from the prompt, or **File -> New -> Project from Existing Sources** from the menu.

![Getting Started 1](images/getting-started-1.png)

Locate the **finished** folder and choose to import the project.

When prompted, choose to **Import project from external model**, and select **Maven** as the particular external model.

![Getting Started 2](images/getting-started-2.png)

The next screen should present you with IntelliJ specific configurations.  In most scenarios it is safe to use the default settings, but specific settings can be seen below.

![Getting Started 3](images/getting-started-3.png)

You'll be asked which Maven project you'd like to import.  Choose **com.couchbase.example:try-cb-java:1.0-SNAPSHOT**, which should be the only available option.

![Getting Started 4](images/getting-started-4.png)

When asked to select the Java Development Kit (JDK) to be used, make sure you select the JDK 1.7 or greater.  If you do not have it installed, you'll need to install it before continuing.

![Getting Started 5](images/getting-started-5.png)

Finally, give the IntelliJ project a name or leave it as the default which should be **try-cb-java**.

![Getting Started 6](images/getting-started-6.png)

Before running the project, a run configuration must be made.  From the menu, choose **Run -> Edit Configurations**.  A new Maven configuration will need to be added which can be done by clicking the plus (+) button at the top left of the popup.

![Getting Started 7](images/getting-started-7.png)

You'll want to give it a good name, to represent that it is a configuration for launching the application.  **spring-boot:run** is a good name and it is the same thing to be entered in the **Command Line** section before clicking **Apply**.

![Getting Started 8](images/getting-started-8.png)

The project can now be run by clicking **Run -> Run 'spring-boot:run'** from the menu.

The project is now running and can be viewed via a web browser at [http://localhost:8080](http://localhost:8080).

### Using the Command Line

If you don't want to use IntelliJ you can launch the project from your Command Prompt (Windows) or Terminal (Mac and Linux).  You are required to have Maven installed and configured to make this possible.

With the `finished` project as your current working directory, execute the following:

```
mvn spring-boot:run
```

The project is now running and can be viewed via a web browser at [http://localhost:8080](http://localhost:8080).

### A Note About the Workshop

We have tried to avoid having this workshop be just a series of cut-and-paste sections of code so you will find below a series of steps that will help you build the application. If you find yourself in trouble and want to skip a step or catch up, you can look at the completed code in the finished/ directory.

In the source code, you will find comments to help locate where the missing code is meant to go. For example:

```
// WORKSHOP STEP 1: missing method to save a new document
```

## STEP 1: Connect to a Couchbase Cluster and Open a Bucket

Make sure you have imported the project from the `initial` directory.  Right now the project is not compiling.  This is normal, you just need to initialize the `Cluster` and `Bucket` objects.  You'll do this over the course of the following steps.  We have described what code needs to be written, occasionally including some code snippets.  Some details and exception handling are left to you as an exercise.

Open `Database.java` in the **src/main/java/trycb/config** directory.

Notice there are two configuration `@Bean` methods.  These two properties, the `Bucket` in particular, are used throughout the rest of the application.

Starting with the Couchbase Cluster, it can typically be created like the following:

```java
Cluser myCluser = CouchbaseCluster.create(String hostname);
```

Of course you'll need alter the statement to whatever is appropriate for the `@Bean`.  With the Cluster configured, the Bucket needs to be opened.  Typically a Couchbase Bucket would be opened like the following:

```java
Bucket myBucket = myBucket.openBucket(Bucket bucket, String password);
```

Like with the Cluster statement, it will need to be altered as appropriate to fit the application.

## STEP 2: Standard NoSQL CRUD Operations

With modern full stack applications the front-end is separated from the back-end.  This is made possible through the use of APIs.  The front-end contacts a particular API endpoint and the API endpoint communicates with the database before returning a response to the front-end that made the request.

Steps 2, 3 and 4 will focus on the database functions that the back-end will make use of.  The API endpoints will come later.

Couchbase, being a NoSQL document database, will store everything as JSON.  Every document will have a key or document id to go with it.  Like with other document databases, operations can be performed based on the key such as `insert`, `get`, `delete`, and `upsert` operations.

To get a single document in Couchbase you can do a `get` by key:

```java
JsonDocument myDocument = myBucket.get(String key);
```

To `insert` a document in Couchbase, you can create a JsonDocument composed of a key and JsonObject like the following:

```java
JsonDocument myDocument = JsonDocument.create(String key, JsonObject data);
myBucket.insert(myDocument);
```

The `login`, `createLogin`, and `registerFlightForUser` methods in the **User.java** service of the **src/main/java/trycb/service** directory makes use of these.

In the `login` method of **User.java** you will get a JsonDocument by the key.  In the `createLogin` function you will create a JsonObject that looks like this as JSON:

```json
{
    "type": "user",
    "name": "username_here",
    "password": "encrypted_password"
}
```

Passwords should only be stored in Couchbase if they have first been hashed.  Bcrypt is the standard when it comes to hashing sensitive data such as passwords.  This can be done like so:

```java
String hash = BCrypt.hashpw(String password, BCrypt.gensalt());
```

Try filling in the blanks to the `createLogin` method.  Create the JsonObject, convert it to a JsonDocument, then insert it into the database.

## STEP 3: Reactive Programming with RxJava and the Couchbase Java SDK

The Couchbase Java SDK supports RxJava for reactive programming.  Data gets pushed into Observable objects and everything that is subscribing to one of those objects will receive its data.  This makes our usage of Couchbase asynchronous.

Not everything in this workshop will make use of RxJava in order to keep the scope of learning wide.  You can however, fill in the gaps however you wish.

The `User` class found at **src/main/java/trycb/service/User.java** is a great example of how we can use reactive programming in our project.  Before jumping into the class, let's take a look at some example RxJava.

By now you're probably familiar with the following:

```java
JsonDocument document = bucket.get("key-here");
```

That is a synchronous call.  If we wish to make it asynchronous we can do the following:

```java
Observable<JsonDocument> document = bucket.async().get("key-here");
```

In the above, the observable will eventually contain a Couchbase JsonDocument.  Now we have the option to do further manipulations to the returned JsonDocument when it is returned.  For example we could map the response to a `ResponseEntity` like so:

```java
ResponseEntity<String> response = bucket.async()
    .get("key-here")
    .map(new Func1<JsonDocument, ResponseEntity<String>>() {
        @Override
        public ResponseEntity<String> call(JsonDocument doc) {
            return new ResponseEntity<String>(doc.content().toString(), HttpStatus.OK);
        }
    });
```

More Observable methods can be seen in the [API documentation](http://docs.couchbase.com/sdk-api/couchbase-java-client-2.1.6/).  Let's take what we know now and apply it to the `getFlightsForUser` function of the `User` class.

## STEP 4: SQL-like Queries with Couchbase N1QL

Starting with Couchbase 4.0, the SQL-like queries called N1QL queries have been available for use.  This allows us to retrieve complex datasets via a query rather than through application level parsing.

The **src/main/java/trycb/service/FlightPath.java** and **src/main/java/trycb/service/Airport.java** files are perfect candidates for these complex queries.

Start with the `Airport` class since it is the simpler of the two classes.

In Java we have the option to use either the Fluent API or raw queries for getting the data we need.  For example, the following is a Fluent API query:

```java
AsPath prefix = select("airportname").from(i("bucket-name-here"));
```

The above translates to the following raw query:

```sql
SELECT airportname FROM `bucket-name-here`
```

Try to fill in the gaps in the `Airport` class using the Fluent API.  The goal is to make a query with three different possible `WHERE` clauses.

For the `FlightPath` class try to use a raw N1QL query rather than being guided by the Fluent API.  There are two queries that we need to take care of:

First we need to find all source and destination `faa` airport codes given an airport name.  The best way to accomplish this is by making use of a SQL `UNION`.  The second query is quite large.  We need to query for some very specific properties that span across three different documents.

## STEP 5: Creating API Endpoints

This particular application has three clear-cut endpoint categories which is why we're breaking them up into separate files.  All endpoints will be in the **src/main/java/trycb/web** directory of the project.

The **UserController.java** class will contain all endpoints that operate on user data, the **AirportController.java** class will contain all endpoints that operate on airport data and finally the **FlightPathController.java** class will contain all endpoints that operate on routing data.

The database services were already created in the previous steps.  Now we just need to figure out what information to obtain and how to use them.

Start with the **UserController.java** file.  We know that the `User` service is a static class.  Find all the endpoints that operate on **GET** and use the `User` service as appropriate.  The endpoints that operate via **POST** are a bit different.  All data posted from the client front-end is in a JSON body.  It is received as a JSON string and typically needs to be parsed like so:

```java
JsonObject jsonData = JsonObject.fromJson(String json);
```

To get properties of the `JsonObject` you can perform operations like `getString` or `getArray`, but not limited to just the two.  Use this knowledge to complete the **POST** endpoints in the **UserController.java** file.

The endpoints in the **FlightPathController.java** and **AirportController.java** files are no different than what we just saw.  See if you can complete them without looking at the `finished` copy.
