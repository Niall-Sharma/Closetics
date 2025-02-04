## HelloWorld Spring Boot application

### Functionalities
1. The main class does not contain a lot of logic, it only acts as the program starter.
3. The annotations (Eg @RestController) are already defined in springboot, they make your life easy.
4. The application.properties contains information about the different configurations for your applications (Eg server port)


### Pre requisites

1. Maven has to be installed on command line OR your IDE must be configured with maven
2. Java version 1.8 - 1.10 (Some versions of springboot are really unhappy with java 11)

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean install</code></br>
<code>cd target</code></br>
<code>java -jar helloworld-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

