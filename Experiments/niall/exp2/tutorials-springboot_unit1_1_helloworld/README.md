## Simple Spring Boot application

### Functionalities
1. The main class does not contain a lot of logic, it only acts as the program starter.
2. The systems folder contains the Controller class. This is responsible for generating the web endpoints.
3. The annotations (Eg @RestController) are already defined in springboot, they try their best to make your life easy.
4. The application.properties contains information about the different configurations for your applications (Eg server port)

### Maven

Maven is a popular build-tool/project-management-tool for Java. It uses an XML file called pom.xml(Project Object Model) to detail out project specifications.
Manually running a complex project would involve tedious steps of getting all the libraries, adding them all to the classpath, specifying the build type, adding specific
resources and then finally compiling the project(build specification would need to be specified each time it runs!!). Build-tools help ease this process by automatically
downloading all the required libraries/frameworks and adding them to the project classpath automatically. Maven can also be configured to run various forms of tests by 
applying different test plugins in the pom.xml file. Additionally, maven also packages the project into a jar/war files.

#### Below are the steps that take place when a maven project is run.

1. Validity of the pom.xml is checked.
2. Resources(dependencies) that are specified in the pom.xml are gathered from the maven-central repository(and stored in local cache for future builds).
3. The project is compiled with all the required libraries.
4. The test are run if they've been configured.
5. The project is then packeged into jar/war files which can be used either as a library or an executable.

#

### Pre requisites

1. Maven has to be installed on command line OR your IDE must be configured with maven
2. Java version 1.8 - 1.10 (Some versions of springboot are really unhappy with java 11)

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean install</code></br>
<code>cd target</code></br>
<code>java -jar helloworld-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

#

### Additional Resources

[SpringBoot + Maven](https://www.bogotobogo.com/Java/tutorials/Spring-Boot/Spring-Boot-HelloWorld-with-Maven.php)

[Springboot hello world](https://spring.io/guides/gs/spring-boot/)

### Version Tested

|IntelliJ  | Project SDK | Springboot | Maven |
|----------|-------------|------------|-------|
|2023.2.2  |     17      | 3.1.4      | 3.6.3 |