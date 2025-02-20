SpringBoot version 2.4.0
JAVA JDK version 11

# SpringBoot JPA: One to One relation example
### One to One relations
In object-oriented programming, real-life entities are represented as classes, and in MySQL, they are stored as tables. JPA/Hibernate is a framework that matches a Class to Table(
each variable represents a column) to ease the manipulation of data in Java. Entities almost always interact with one another and establish individual relationships among 
themselves Eg: A Person will have exactly one social security number(One to One), A person can have multiple cars(One to Many), etc. This example is created with the assumption
that one person can have only one laptop(made by poor people). It shows how a one to one relation can be represented between entities using JPA/Hibernate.
### Pre-requisites

0. Go through the springboot_unit1_2_hellopeople
1. Maven has to be installed on command line OR your IDE must be configured with maven
2. Java version 1.8 - 1.10 (Some versions of springboot are really unhappy with java 11)

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean package</code></br>
<code>java -jar target/onetoone-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

### Available End points from POSTMAN: CRUDL
1. CREATE requests - 
POST request: 
    1. /laptops - Create a new Laptop. The request has to be of type application/JSON input format 
    {
        "cpuClock" : "1.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "1000000"
    }
    2. /Persons - Create a new Person with laptop. Request Format : application/json
    {
        "name" : "John",
        "email"  : "johndoe@somemail.com",
        laptop   : {
            "cpuClock" : "2.3",
            "cpuCores"  : "4",
            "ram"   : "8",
            "manufacturer" : "Hp",
            "cost" : "600"
        }
    }.
    3. /Persons - Create Person without laptop. Request Format : application / json
    {
        "name" : "John",
        "email"  : "johndoe@somemail.com"
    }
2. READ requests -
GET request:
    1. /laptops/{id} - Get laptop object for provided id
    2. /Persons/{id} - Get Person object with associated laptop for provided Person id

3. UPDATE requests -
PUT request : 
    1. /laptops/{id} - Update info on a laptop whose id is provided on the URL. The modified info is sent in the body of the message
    {
        "cpuClock" : "2.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "2000000"
    }
    2. /Persons/{id} - Update Person and optionally a lptop associated with him/her for id provided in the url
    {
        "name" : "John",
        "email"  : "johndoe@newmail.com",
        laptop   : {
            "id" : "5"
            "cpuClock" : "3.3",
            "cpuCores"  : "8",
            "ram"   : "16",
            "manufacturer" : "new Laptop",
            "cost" : "1000"
        }
    }.

4. DELETE a record - 
 DELETE request:
    1. /laptops/{id} - Delete the entry with id in te url
    2. /Persons/{id} - delete a Person with the id provided in the url

5. LIST all -  
GET request
    1. /laptops - Get/List all the laptops stored in the db
    2. /Persons - Get/List all the Persons along with their associated laptops

6. Other end points:
GET request:  /oops   --- this shows you what happens when your code throws an exception.

#

### Note :
### 1. /laptops APIs are defined in Laptop/LaptopController, 
### 2. /Persons API is defined in the Person/PersonController

# 
## Some helpful links:
[SpringBoot Entity](https://www.baeldung.com/jpa-entities)   
[SpringBoot OneToOne](https://www.baeldung.com/jpa-one-to-one)    


### Version Tested

|IntelliJ  | Project SDK | Springboot | Maven |
|----------|-------------|------------|-------|
|2023.2.2  |     17      | 3.1.4      | 3.6.3 |

