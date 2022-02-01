# springboot-sample-game-app

To Manage the scores of players and analyse the scores.

## Instructions on how to build and run your app
There are several ways to run a Spring Boot application on your local machine.

### To Build
```shell 
gradle build
```

### To Run
```shell
gradle bootRun
```

From Ide

 One way is to execute the `main` method in the com.github.test.game.GameApplication` class from your IDE.

Alternatively you can use the [Spring Boot gradle plugin]

## Instruction on how to run unit and integration tests.

   Input format restriction for Time.
 
  "yyyy-MM-dd'T'HH:mm:ss.SSSX"
  "yyyy-MM-dd'T'HH:mm:ss.SSS"
  "EEE, dd MMM yyyy HH:mm:ss zzz" 
  "yyyy-MM-dd"

Run below command from your shell.
```shell
gradle Test
```

## APIs documentaion
  Upon cloning and extraction open the subdirectory game/javadoc/index.html in any browser of your choice.

 Refer the Java doc folder path- https://github.com/prerna1990/learning/blob/master/game/javadoc/index.html
 
## Requirements
Java 8 or above
Gradle 6 or above
Spring 2.2 or above
Spring Boot
HSQL
JUnit 5