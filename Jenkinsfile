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

      // stage('Upload Artifact to JFrog') {
      //   steps {
      //     script {
      //       def server = Artifactory.server 'my-jfrog'
      //       def uploadSpec = ''' {
      //         "files": [{
      //           "pattern": "/var/lib/jenkins/workspace/argo_ci/application-code/target/spring-petclinic-3.1.0-SNAPSHOT.jar",
      //           "target": "my-jfrog",
      //           "recursive": "false"

      //         }]
      //       } '''
      //       server.upload(uploadSpec)
      //     }
      //   }
      // }

      stage('Build and Push Docker Image') {
        steps {
          echo 'Building and Pushing Docker Image'
          script { 
            dir('application-code') {

            // Define variables
            def dockerImage = "dangeo36/argocicd" // Replace with your desired image name and tag
            def ecrRepoUri = "326927831581.dkr.ecr.us-east-1.amazonaws.com/argocicd" // Replace with your ECR repository URI
            def mavenBuildDir = "." // Replace with your Maven build directory

            // Build the Docker image
            sh "docker build -t --debug ${dockerImage} ${mavenBuildDir}"

            // Authenticate Docker to AWS ECR
            sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${ecrRepoUri}"

            // Tag the Docker image with ECR repository URI
            sh "docker tag ${dockerImage} ${ecrRepoUri}:${BUILD_NUMBER}"

            // Push the Docker image to ECR
            sh "docker push ${ecrRepoUri}:${BUILD_NUMBER}"

            // Optionally, clean up local Docker images
            sh "docker rmi ${dockerImage} ${ecrRepoUri}:${BUILD_NUMBER}"
          }
          }
        }
      }

      stage ('Trigger cd pipeline') {
            steps {  
                echo 'Triggering cd pipeline' 
                script {
                    withCredentials([string(credentialsId: 'jenkins-admin-token', variable: 'JENKINS_ADMIN_TOKEN')]) {
                        sh """
                        curl http://18.208.251.57:8080/job/argo_cd/buildWithParameters?token=gitops-config \
                          --user admin:${JENKINS_ADMIN_TOKEN} \
                          --data verbosity=high \
                          -H "Content-Type: application/x-www-form-urlencoded" \
                          -H "Cache-Control: no-cache"
                            """
                    }
                }
                }
        } 


      
    }
}