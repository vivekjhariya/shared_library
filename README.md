this project specially for jenkins shared library you can use all this functions according to your usecasses. According to pipeline syntax intregate Node on jenkins functions are aligen

1. jenkinsPlugininstallation.grrovy file use for install java on your node agent this is prequsite for your agent nodes

 how to use this fun in pipeline looking like this: 

    stage('jenkins Plugin Installation'){
        steps{


             script {

                 jenkinsPluginsInstallation()

                  }

         }
         
    }

2. install docker in you nodes with giving them all permission via dockerInstall.groovy 

   how to use this fun :

     stage('install docker '){

        steps{
             
             script{

             dockerInstall()

             }
 
        }

    }    

3. install sonarQube for code quality , security and maintainability. it detects bugs, security vulnerabilities, code smells and duplications  sonarQubeInstallation.groovy

stage('install sonarqube'){
    
     steps{


         script{

          sonarQubeInstallation()

        }

    }

}

4. install Trivy for detect misconfigurations and secrets in images and also scanning the file system  trivyInstallation.groovy

 stage('trivy installation'){

   steps{

      
          script{

            trivyInstallation()
           }
    }
}
  
5. Now cloning the project from github repo gitClone.groovy

 stage('git clone'){

  steps{

          gitClone(

             url: 'your github reposetory link',
            branch: 'main'
        )

    }

 }

 if you use private repo you can also use extra arguments like 

   stage('git clone'){

     steps{

          gitClone(

             url: 'your github reposetory link',
            branch: 'main'
            credentialsId: 'github-creds'
         )

     }

  }

6. scanning file system in our project due to findout all the misconfiguration and vulnerability scanning

 stage('Trivy file system scan'){
   
      step{

           trivyFileSystemScan(
              path: '.'
              severity: 'HIGH,CRITICAL',
              exitCode: 1                         
             
           )
     

      }
  
 }

 if you put the value =1 in exitCode due to Trivy found any High vulnerability then pipeline is stop or value = 0 found any High vulnerability pipeline is not stop 

7. OWASP (Open Worldwide Application Security Project) use for primarily to identify, prioritize, and mitigate critical security risks in web application, APIs, and software system

   stage('OWASP Dependency Check') {
    steps {
        owaspDependencyScan(
            odcInstallation: 'OWASP-Dependency-Check',   // put your Global tool name here
            failOnCVSS: 7,
            scanPath: '.'
        )
    }
}

8. SonarQube analysis is primarily used for static code analysis to continuously inspect source code quality and security

  stages {

        stage('SonarQube Analysis') {
            steps {
                sonarQubeQualityAnalysis(
                    sonarQubeTokenName: '${sonar-token}',                 // Jenkins Credentials ID
                    sonarQubeProjectKey: 'myntraa',
                    sonarQubeProjectName: 'Myntraa Project',
                    sonarQubeInstallationName: 'SonarQube',            // Jenkins SonarQube installation name
                    extraProperties: '-Dsonar.sources=src -Dsonar.exclusions=**/*.spec.ts,**/*.test.ts'
                )
            }
        }
  }   

9. SonarQube quality gates are used to enforce code quality policies by determining if a project is ready for release.

   stage('SonarQube Quality Gate') {
            steps {
                sonarQubeQualityGate(
                    timeout: 5,                // minutes
                    abortPipeline: true        // true = if fail pipeline is stop
                )
            }
        }
    
10. docker build the image after files and deps checking    

    stage('Docker Build') {
    steps {
        script {
            dockerBuild(
                imageName: 'myntraa',
                // buildNumber: env.BUILD_NUMBER,   // optional
                // dockerfile: 'Dockerfile',        // optional
                // context: '.'                     // optional
            )
        }
    }
}

11. The trivy image command is used to scan container images for security vulnerabilities, misconfigurations, exposed secrets, and license risks.

  stage('Trivy Image Scan') {
    steps {
        trivyImageScan(
            image: 'myntraa:latest',
            severity: 'HIGH,CRITICAL',
            exitCode: 1
        )
    }
  }

   if you put the value =1 in exitCode due to Trivy found any High vulnerability then pipeline is stop or value = 0 found any High vulnerability pipeline is not stop 

12. push the docker image to DockerHub    

   stage('Docker Push') {
            steps {
                dockerPush(
                    credentialsId: "${DOCKERHUB_CREDENTIALS}",   // Jenkins Credentials ID
                    imageName: "${DOCKER_IMAGE_NAME}",         // pushing latest tag
                    pushLatest: true
                )
            }
        }


 13. docker pulling the latest image and deploying to this into the server 

   stage('Deploy') {
            steps {
                dockerDeploy(
                    composeFile: 'docker-compose.yml',
                    pullImage: true
                )
            }
        }

14. cleanup old images from sever due to freeup space 
    
    stage('Docker Cleanup') {
            steps {
                dockerCleanup(
                    imageName: "${DOCKER_IMAGE_NAME}",
                    keepImages: 3    // how many past images you want to remove
                )
            }
        }

15. sending the Email notification due to your pipeline is success , failed and unstable     

    post {
        success {
            emailNotify("SUCCESS", "${EMAIL}")
        }
        failure {
            emailNotify("FAILED", "${EMAIL}")
        }
        unstable {
            emailNotify("UNSTABLE", "${EMAIL}")
        }
    }


how to use pipeline jenkins

 @Library('shared_library') _

 pipeline{

  agent {label "prod"};
   
    environment {
        DOCKER_IMAGE_NAME = "myntraa"
        DOCKERHUB_CREDENTIALS = "dockerhub-creds"          // Jenkins Credentials ID
        SONAR_TOKEN = "sonar-token"                        // Jenkins Credentials ID
        SONAR_INSTALLATION = "SonarQube"
        OWASP_INSTALLATION = "OWASP-Dependency-Check"
        EMAIL = "vivekjhariya242@gmail.com"
    }

    stages{
       
           stage your all stage{
                
                steps}{}

           }

           post{}

    

    }


 
    
            

