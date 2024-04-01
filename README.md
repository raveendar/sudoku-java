# Sudoku Java Stanalone Server

This is a Sudoku game project developed using Java backend without MVC framework (Using Framework-less Java REST API
).  Grid generated using mocked data. User Input's validation done on the server side

### Prerequisite
Java 17
Maven 3.6.3
Docker

### Build

```shell
git clone https://github.com/raveendar/sudoku-java.git
cd sudoku-java
mvn install

Create docker image
docker image build -t sudoku-java-jar:latest .
```

### Run the server

```shell
Run as Java Application
cd target
java -cp "sudoku-0.0.1-SNAPSHOT.jar;lib/*" Server

Run as Docker Application
docker run --publish 8080:8080 sudoku-java-jar:latest

```

### Test application using Curl commands

```shell
Generate Grid
curl --location 'http://localhost:8080/api/generate'

Validate User Input
curl --location 'http://localhost:8080/api/validate' \
--header 'Content-Type: application/json' \
--data '{
	"input" : [
		[0,0,0,0,0,0,0,0,3],
		[5,7,0,0,0,9,0,2,1],
		[0,0,2,0,0,0,0,7,0],
		[9,0,0,0,0,4,0,0,0],
		[6,0,0,7,0,0,3,5,0],
		[7,0,0,0,8,1,0,0,0],
		[0,6,0,0,1,0,2,0,0],
		[2,0,0,0,0,0,0,4,5],
		[0,0,1,4,0,5,0,0,0]
	]
}'

```
