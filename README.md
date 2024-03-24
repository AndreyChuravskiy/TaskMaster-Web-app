# TaskMaster-Web-app

TaskMaster-Web-App: A Java Spring Project for Managing Tasks and Workflows

TaskMaster-Web-App is a Java Spring project that follows a REST architecture pattern. It provides a web platform for users to manage tasks and workflows efficiently. Users can create, assign, and track tasks, as well as organize workflows to streamline their work processes. With Docker support and a Postman collection for simplified testing, this project can be easily deployed and evaluated.

Here is a list of frameworks, libraries, and technologies used in this project:

#### Development process:

-   IntelliJ IDEA: A popular integrated development environment for Java.
-   Git: A widely used version control system for software development.
-   Maven: A build automation tool for Java projects.
-   Lombok: A Java library that helps reduce boilerplate code.

#### Architecture:

-   REST pattern: A software architecture style that allows for the creation of lightweight and scalable web services.
-   Spring: A popular framework for building Java-based web applications.
-   Spring Boot: A framework that simplifies the development of Spring-based applications.
-   Spring Security: A powerful security framework for Spring applications.
-   jjwt: A Java library for JSON Web Tokens.
-   Jackson: A Java library for parsing JSON data.

#### Database:

-   Hikari: A lightweight and fast JDBC connection pool.
-   Spring Data JPA: A library that simplifies the implementation of JPA repositories.
-   Hibernate: An ORM (object-relational mapping) framework for Java.
-   PostgreSQL: A popular open-source relational database management system.

#### Testing:

-   JUnit: A popular testing framework for Java.
-   AssertJ: A fluent assertion library for Java.
-   Mockito: A popular Java mocking framework for unit testing.

#### Deployment:

-   Docker: A containerization platform that simplifies the deployment process.

To test the application, please refer to the readme.txt file in the docker folder for instructions on creating the Docker container. Additionally, you can use the TaskMaster-Web-app.postman_collection.json file in the resources folder to create requests. Your first request should be Register from auth controller folder. You will get response that looks like this:

![image](https://github.com/SSDishnik/TaskMaster-Web-app/assets/76844297/1f5cb4f5-e616-45cf-9476-0460d5023556)

Copy accessToken, that will be your authentification key for performing next requests. Chose request that you want to send, then go to the Auth field in Postman and Paste accessToken to the Token field. Now server will respond to you. There is an example with Hello, issuer request:

![image](https://github.com/SSDishnik/TaskMaster-Web-app/assets/76844297/49550106-02c9-44a7-903b-e13387a88f4a)

For testing purposes accessToken won't expire, but in production time of expiration should be set to 5 minutes.
