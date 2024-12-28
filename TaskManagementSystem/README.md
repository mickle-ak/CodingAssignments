# Simple Task Management System

## Take-home assignment specification

### Task Description:

You need to develop a simple Task Management System using Java. The system must provide functionality for creating, editing, deleting, and viewing tasks. Each
task should include a title, description, status (e.g., "pending", "in progress", "completed"), priority (e.g., "high", "medium", "low"), as well as the author
and assignee of the task. Only the API needs to be implemented.

### Requirements:

1. The service must support user authentication and authorization via email and password.
2. Access to the API must be authenticated using a JWT token.
3. Users should be able to manage their tasks: create new ones, edit existing ones, view and delete them, change statuses, and assign task executors.
4. Users should be able to view tasks of other users, and task assignees should be able to change the status of their tasks.
5. Comments should be allowed on tasks.
6. The API must allow retrieving tasks by a specific author or assignee, as well as all comments related to them. Filtering and pagination must be supported.
7. The service must handle errors properly and return clear messages, as well as validate incoming data.
8. The service must be well-documented. The API should be described using Open API and Swagger. Swagger should be configured in the service. A README file with
   instructions for local project setup is required. The development environment should be set up using Docker Compose.
9. Write several basic tests to check the core functionality of your system.
10. The system should be implemented using Java 17, Spring, and Spring Boot. PostgreSQL or MySQL can be used as the database. For authentication and
    authorization, Spring Security should be used. Additional tools can be used if necessary (e.g., caching).

### Evaluation:

The following aspects will be evaluated:

1. Compliance with the requirements
2. Code quality and cleanliness
3. System design and use of OOP principles
4. Presence of tests and test coverage
5. Error handling 

## Implementations

I try to create the follow solutions:

### The CRUD Solution

Manually created a simple CRUD solution. I believe this is expected for this assignment.

### The Solution Using Spring REST Repositories

It should be simpler to create and maintain.

### The ChatGPT-Generated Solution

Try to generate a complete application with ChatGPT.

### The Clear Architecture Solution

A variant with clear separation of core - business domain and plugins - here Rest API and DB.

## Remarks

It was a code assignment for a job interview for a _JUNIOR_ Java developer position in Russia. I think it is too complicated for a junior developer. 
I am afraid that many companies in Russia are trying to get at least mid-level or even senior developers for a junior developer's salary :-(

