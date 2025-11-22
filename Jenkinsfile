// استدعاء مكتبة الدوكر اللي ربطناها فوق
@Library('my-shared-lib') _

pipeline {
    agent any

    tools {
        maven 'maven-354' 
        jdk 'jdk-11'
    }

    environment {
        // إعدادات مشروعك
        IMAGE_NAME = "hassaneid/iti-java-project"
        TAG_VERSION = "${BUILD_NUMBER}" // كل مرة ياخد رقم Build جديد
        CONTAINER_NAME = "iti-java-app"
        HOST_PORT = "8090" // البورت اللي هتفتح بيه الموقع
        
        // سحب بيانات الدخول المخزنة في جينكنز
        DOCKER_CRED = credentials('docker-hub-credentials')
    }

    stages {
        stage('Checkout SCM') {
            steps {
                // سحب الكود من جيت
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                echo '☕ Compiling & Packaging...'
                // أمر بناء ملف الـ Jar وتجاهل التيست توفيراً للوقت حالياً
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // هنا بننادي على الدالة من الملف الأول
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