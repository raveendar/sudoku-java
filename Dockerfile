FROM openjdk:17-jdk-alpine
COPY target/sudoku-0.0.1-SNAPSHOT.jar sudoku.jar
ENTRYPOINT ["java","-jar","/sudoku.jar"]