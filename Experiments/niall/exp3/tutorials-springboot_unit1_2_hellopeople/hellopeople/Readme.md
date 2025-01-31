# Spring Boot Example with Database

### Note : For Simplicity an In-memory DB called H2 is used, so once you stop your application the data will be lost. This is by design, you can however switch the dependencies and application.properties to use MySQL for permanent storage.
#

### Pre-requisites

0. Go through the springboot_unit1_1_helloworld
1. Maven has to be installed on command line OR your IDE must be configured with maven
2. Java version 1.8 - 1.10 (Some versions of springboot are really unhappy with java 11)

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn package</code></br>
<code>java -jar target/helloworld-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

### Avaiable End points : 
1. GET : Can be run on the Browser Directly
    1. /persom/create - create dummy data
    2. /people - get all Owners 
    3. /person/{id} - get specific owner
    4. /oops - to check what happens when exception is thrown  
2. POST :  (USE POSTMAN TO TEST)
    1. /person/new - create a new Owner (Requires PostMan JSON request) Example request { "firstName":"Something", "lastName":"Something else", "address":"here", "telephone":"some number" } </br>
6. Any end points starting /owners are defined in owner/OwnerController, the /oops is defined in the system/CrashController, and the homepage is defined in the system/WelcomeController

## Some helpful links:
[HelloWorld REST Api](https://spring.io/guides/gs/rest-service/)   
[Getting Started with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)   
[Spring Annotations](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)   
[Application Properties Appendix](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)   

