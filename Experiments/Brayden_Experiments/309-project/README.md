# Spring Boot Example

### Pre-requisites

0. Go through the springboot_unit1_1_helloworld
1. Maven has to be installed on command line OR your IDE must be configured with maven

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean package</code></br>
<code>java -jar target/helloworld-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

### Available End points from POSTMAN: CRUDL
1. CREATE a new Person
POST request: 
    /people - Create a new person. The request has to be of type application/JSON input format 
    {
        "firstName" : "tom",
        "lastName"  : "hanks",
        "address"   : "hollywood",
        "telephone" : "999"
    }

2. READ info on a person
GET request:
    /people/{firstName} - Get all person whose name is the same as the parameter {firstName} (Example: /people/tom )

3. UPDATE info on a person
POST request : 
    /people/{firstname} - Update info on a person whose firstname is sent on the URL. The modified info is sent in the body of the message
    {
        "firstName" : "tom",
        "lastName"  : "hanks",
        "address"   : "hollywood",
        "telephone" : "888"
    }

4. DELETE person record
 DELETE request:
    /people/{firstName} - Delete the entry in the hashmap with key {firstName}. (Example: /people/tom)

5. LIST all persons 
GET request
    /people - Get/List all the perople stored in the HasMap in the form of a JSON

6. Other end points:
GET request:  /oops   --- this shows you what happens when your code throws an exception.

#

### Note :
### 1. /people APIs are defined in people/PeopleController, 
### 2. /oops API is defined in the CrashController
### 3. The homepage is defined in the WelcomeController

# 
## Some helpful links:
[HelloWorld REST Api](https://spring.io/guides/gs/rest-service/)   
[Spring Annotations](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)   
[Application Properties Appendix](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)   
### Version Tested

|IntelliJ  | Project SDK | Springboot | Maven |
|----------|-------------|------------|-------|
|2023.2.2  |     17      | 3.1.4      | 3.6.3 |
