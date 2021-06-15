# Task List - Java CRUD App


The app connects MySQL database with Spring Boot Java configuration. Written in Java there is a class named Task which represents db entity as well as data transfer object equivalent. TaskMapper class is responsible for mapping fields between these classes. There is also an interface named TaskRepository which extends an interface that shares methods and logic for managing data from db. Then, in class named DbService there are several methods built on top of defined repository. Finally, in TaskController we defined methods with assigned routes on which these methods are being executed.

## Tech Details

- Database: MySQL
- Libraries/Framework: 
  - Spring Boot: 
    - Spring Boot Starter Web, 
    - Spring Boot Starter Data JPA,
    - Spring Boot Starter Validation
    - Spring Boot Starter Test,
  - Lombok,
  - MySQL Connector 
- HTTP Methods: GET, POST, PUT, DELETE
