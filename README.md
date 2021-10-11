# Mediscreen
Openclassrooms project number 9

<!-- ABOUT THE PROJECT -->
## About The Project

Microservices application for the startup Mediscreen. This is a Spring Boot application for project number 9 of [Openclassrooms](https://openclassrooms.com/) java back-end formation.

Project goals:
* Use Agile methodology
* Create a client UI for the application (module clientui).
* Create a microservice for SQL data access (module mpatient using MySQL).
* Create a microservice for NoSQL data access (module mnote using MongoDB).
* Create a microservice for diabetes assessment (module mdiabeteassess).

## Class Diagram
![UML-class-diagram](https://raw.githubusercontent.com/jerome13250/p9_mediscreen/master/readme_img/Mediscreen_ClassDiagram.png)

## User Interface responsive design Bootstrap : Desktop / Mobile
![Bootstrap responsive design](https://raw.githubusercontent.com/jerome13250/p9_mediscreen/master/readme_img/bootstrap_responsive.png)

### Built With

* [Java 8](https://adoptopenjdk.net/)

<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites

Check that you have : 
* Java 11 installed
  ```sh
  java -version
  ```

### Installation

1. Choose a directory
   ```sh
   cd /path/to/directory/project
   ```
2. Clone the repo
   ```sh
   git clone https://github.com/jerome13250/paymybuddy.git
   ```
3. Select the paymybuddy directory
   ```sh
   cd paymybuddy
   ```
4. Package the application (fat jar file) using [maven wrapper](https://github.com/takari/maven-wrapper) provided in the folder, it downloads automatically the correct Maven version if it's not found.
   ```sh
   mvnw package
   ```
5. Execute the jar file
   ```JS
   java -jar ./target/paymybuddy-0.0.1-SNAPSHOT.jar
   ```
6. To access the application, open your browser, go to [http://localhost:8080](http://localhost:8080)

7. Note that the first time, since you start with an empty database, you need to [register](http://localhost:8080/registration) some users to be able to do some operations.

![homepage](https://github.com/jerome13250/paymybuddy/blob/master/images/PayMyBuddy_homepage.png)