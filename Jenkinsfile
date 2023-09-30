pipeline {
    agent any 
    tools {
      maven 'MAVEN3'
    }
    environment {
      AWS_ACCESS_KEY_ID = credentials('aws-creds')
      AWS_SECRET_ACCESS_KEY = credentials('aws-creds')
      SONAR_TOKEN = credentials('sonarcloud-creds')
    }

    stages {
      stage('Sonarcloud scan') {
        steps {
          echo 'scanning repo'
          script {
            dir('application-code') {
              env.SONAR_TOKEN = "${SONAR_TOKEN}"
              sh 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=dangeo36_ArgoCD_K8_Part_2'
            }

          }
        }
      }

      stage('Building Jar') {
        steps {
          echo 'Building Jar'
          script {
            dir('application-code') {
              sh 'mvn clean install -DskipTests,spring.profiles.active=mysql'
            }
          }
        }
      }

      stage('Upload Artifact to JFrog') {
        steps {
          script {
            def server = Artifactory.server = 'my-jfrog'
            def uploadSpec = ''' {
              "files": [{
                "patterns": "/.target/spring-petclinic-3.1.0-SNAPSHOT.jar",
                "target": "my-jfrog"
              }]
            } '''
            server.upload(uploadSpec)
          }
        }
      }
      
    }
}