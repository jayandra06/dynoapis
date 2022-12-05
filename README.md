# DAMS
Dyno APIs Mobile Server

Before starting, Please configure the db. Currently the db is set to postgres.
You can configure the db to mysql or postgres. In order to configure, please navigate to src->main->resources->application.properties

Minimum Java JDK required is Java 1.8
After configuring, You can build a jar file and run it from the command line (it should work just as well with Java 11 or newer)
Use the below command to start the server on port 8080
```
./mvnw package
java -jar target/*.jar
```
Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```
