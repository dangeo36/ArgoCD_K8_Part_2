# ArgoCD  

The goal of this project is to automate Kubernetes manifest files with ArgoCD and Jenkins. The CI portion of the pipeline will build Docker images of a Java application and push them to Amazon ECR. Images are pushed with a build number (or tag) that can be referenced later in the CD portion. The CD portion will pull the latest tag and update the Kube manifest files. Once everything is properly updated, ArgoCD will monitor the latest changes in the pipeline and update accordingly.

## Part 2

For this part of the project, we will be using Jenkins to build out a CI pipeline. The repository contains the source code of a Java Springboot application called PetClinic (details for packaging it and database are below). For the Jenkinsfile, here are the steps:

- Use Sonarcloud.io to scan for vulnerablities in src code
- Build artifact with mvn clean install and activate mysql profile
- Upload artifact to JFrog Artifactory
- Build, Tag, Push Docker Image to Amazon ECR
- Trigger CD pipeline 

Part 3 GitHub repo: https://github.com/dangeo36/ArgoCD_K8_Part_3.git

## Building the local artifact and accessing it from localhost 
```
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
./mvnw package
java -jar target/*.jar
```

You can then access petclinic at http://localhost:8080/

## Database configuration

In its default configuration, Petclinic uses an in-memory database (H2) which
gets populated at startup with data. The h2 console is exposed at `http://localhost:8080/h2-console`,
and it is possible to inspect the content of the database using the `jdbc:h2:mem:testdb` URL.

The app needs to run with a profile: `spring.profiles.active=mysql` for MySQL 
You can start MySQL:

```
docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:8.0
```

## Tools / Dependencies
For this part, I have an Amazon EC2 instance (Ubuntu 20.04, t2.medium) that has Java 17, Jenkins, Terraform, awcli (configured with proper credentials), kubectl, and Docker. Jenkins needs proper plugins and credentials to access AWS, Github repository, and JFrog. 
