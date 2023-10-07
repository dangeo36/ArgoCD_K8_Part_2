pipeline {
    agent any 
    tools {
      maven 'MAVEN3'
    }
    environment {
      AWS_ACCESS_KEY_ID = credentials('aws-creds')
      AWS_SECRET_ACCESS_KEY = credentials('aws-creds')
      SONAR_TOKEN = credentials('sonarcloud-creds')
      APP_NAME = "argoclinic"
      IMAGE_TAG = "${BUILD_NUMBER}"
      IMAGE_NAME = "${DOCKERHUB_USERNAME}" + "/" + "${APP_NAME}"
      ECRURL = "https://326927831581.dkr.ecr.us-east-1.amazonaws.com/argocicd"
      ECR_REGISTRY = "326927831581.dkr.ecr.us-east-1.amazonaws.com/argocicd"
    }

    // parameters {
    //  string(name: 'ECRURL', defaultValue: 'https://326927831581.dkr.ecr.us-east-1.amazonaws.com', description: 'Please Enter your Docker ECR REGISTRY URL?')
    //  string(name: 'REPO', defaultValue: 'argocicd', description: 'Please Enter your Docker Repo Name?')
    //  string(name: 'REGION', defaultValue: 'us-east-1', description: 'Please Enter your AWS Region?')
    // }


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
            def server = Artifactory.server 'my-jfrog'
            def uploadSpec = ''' {
              "files": [{
                "pattern": "/var/lib/jenkins/workspace/argo_ci/application-code/target/spring-petclinic-3.1.0-SNAPSHOT.jar",
                "target": "my-jfrog",
                "recursive": "false"

              }]
            } '''
            server.upload(uploadSpec)
          }
        }
      }

      // stage ('Docker Image Build') {  
      //       steps {
      //               script {   
      //                   dir ('application-code') {
      //                       dockerTag = params.REPO + ":" + "${IMAGE_TAG}"
      //                       docker.withRegistry( params.ECRURL, 'ecr:us-east-1:aws-creds' ) {
      //                       myImage = docker.build(dockerTag)
      //                   }
      //               }
      //           }
      //       }  
      //   }

      
    }
}