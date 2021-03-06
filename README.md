[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jerome13250_p9_mediscreen&metric=alert_status)](https://sonarcloud.io/dashboard?id=jerome13250_p9_mediscreen)  [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jerome13250_p9_mediscreen&metric=coverage)](https://sonarcloud.io/dashboard?id=jerome13250_p9_mediscreen)

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
* Deploy the app in docker containers.

## Class Diagram
![UML-class-diagram](https://raw.githubusercontent.com/jerome13250/p9_mediscreen/master/readme_img/Mediscreen_ClassDiagram.png)

## User Interface responsive design Bootstrap : Desktop / Mobile
![Bootstrap responsive design](https://raw.githubusercontent.com/jerome13250/p9_mediscreen/master/readme_img/bootstrap_responsive.png)

### Built With

* [Java 8](https://adoptopenjdk.net/)
* [Docker](https://docs.docker.com/)

<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites

Check that you have : 
* Java 8 installed
  ```sh
  java -version
  ```
* For Windows Home Edition, use WSL2 (Windows subsystem Linux)  
  [How to install and run Docker natively on Windows 10 Home](https://www.padok.fr/en/blog/docker-windows-10)

* Docker Desktop  
  [Install Docker Desktop on Windows](https://docs.docker.com/desktop/windows/install/)
  

### Installation

1. Choose a directory
   ```sh
   cd /path/to/directory/project
   ```
2. Clone the repo
   ```sh
   git clone https://github.com/jerome13250/p9_mediscreen.git
   ```
3. Select the mediscreen directory
   ```sh
   cd mediscreen
   ```
4. Assemble executable jar archives using maven wrapper provided in the folder, it downloads automatically the correct Maven version if necessary.
   ```sh
   mvnw clean install
   ```
5. Launch the docker-compose to launch the 4 components:
   ```sh
   docker-compose up
   ```
6. To access the application endpoints, open your browser, go to :  
	[Client UI](http://localhost:8080/)  
	[PATIENT Microservice REST API Swagger](http://localhost:8081/swagger-ui/#/)  
	[NOTE Microservice REST API Swagger](http://localhost:8082/swagger-ui/#/)  
	[DIABETE ASSESSMENT Microservice REST API Swagger](http://localhost:8083/swagger-ui/#/)  
7.	By default the databases are empty, if you want to test the application with data provided by openclassrooms you can use following options:
* Option 1: Execute CURL commands for [mpatient](https://github.com/jerome13250/p9_mediscreen/blob/master/mpatient/data/curl/curl_sprint1.txt) and [mnote](https://github.com/jerome13250/p9_mediscreen/blob/master/mnote/data/curl/curl_sprint2.txt)
* Option 2: Import [this data](https://github.com/jerome13250/p9_mediscreen/blob/master/mpatient/data/sql/MySQL_mediscreen_all_sprints.sql) to MySQL using a local client ( host=127.0.0.1, port=3307, login=mpatient, password=mpatient) and [this data](https://github.com/jerome13250/p9_mediscreen/blob/master/mnote/data/js/Mongodb_data_all_sprints.js) to MongoDB using a local client ( host=127.0.0.1, port=27018)

