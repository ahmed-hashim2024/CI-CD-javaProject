@Library('my-shared-lib') _

pipeline {
    agent any

    tools {
        maven 'maven-354' 
        jdk 'jdk-11'
    }

    environment {
        IMAGE_NAME = "ahmedhashim2024/my-awesome-app"
        TAG_VERSION = "${BUILD_NUMBER}"
        CONTAINER_NAME = "iti-java-app"
        HOST_PORT = "8090"
        DOCKER_CRED = credentials('docker-hub-credentials')

        PATH = "/usr/local/bin:${env.PATH}"
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                echo '☕ Compiling & Packaging...'
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerUtils.build(IMAGE_NAME, TAG_VERSION)
                }
            }
        }

        stage('Push to Hub') {
            steps {
                script {
                    dockerUtils.login(DOCKER_CRED_USR, DOCKER_CRED_PSW)
                    dockerUtils.push(IMAGE_NAME, TAG_VERSION)
                }
            }
        }

        stage('Deploy on Mac') {
            steps {
                script {
                    dockerUtils.deploy(CONTAINER_NAME, IMAGE_NAME, TAG_VERSION, HOST_PORT)
                }
            }
        }
    }
}