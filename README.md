# timeZone
mvn clean install

java -jar target/timezone-1.0-SNAPSHOT.jar

In browser go to:

http://localhost:8081/

UTC time will be displayed in browser and request will be logged in terminal

For custom timezone:

http://localhost:8081/?zone=America/Los_Angeles