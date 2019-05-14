pipeline {
    agent any
    environment {
        PROD_GIT = "git+ssh://git@push-par-clevercloud-customers.services.clever-cloud.com/app_c61ace01-afe7-4099-b6c4-c6db68dbd2f5.git"
        GIT_CREDENTIAL_ID = '498f56ad-08cc-4ce4-a8dc-d21027509ca5'
    }
    stages {
        stage('build') {
            steps {
                sh './mvnw clean package'
            }
        }
        stage('quality') {
            when {
                branch 'master'
            }
            steps {
                withSonarQubeEnv('Sonar-Nantes') {
                  sh './mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                }
            }
        }
         stage('deploy') {
             when {
                branch 'master'
            }
            steps {
               sshagent(["${GIT_CREDENTIAL_ID}"]) {
                  sh "git checkout ${GIT_BRANCH}"
                  sh "git pull"
                  sh "git push --force ${PROD_GIT} ${GIT_BRANCH}:master"
                  slackSend channel: '#jenkins_nantes', color: 'good', message: "Déploiement en cours chez Clever Cloud ! ${env.JOB_NAME} commit ${env.GIT_COMMIT}"
               }
            }
        }
    }
    post {
        success {
           slackSend channel: '#jenkins_nantes', color: 'good', message: "Succès ! ${env.JOB_NAME} commit ${env.GIT_COMMIT} (<${env.BUILD_URL}|Open>)"
        }
        failure {
           slackSend channel: '#jenkins_nantes', color: 'danger', message: "Oops ! ${env.JOB_NAME} commit ${env.GIT_COMMIT} (<${env.BUILD_URL}|Open>)"
        }
    }
}