pipeline {
    agent any

    tools {
        jdk 'JDK-21'
        maven 'Maven-3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/dubakinsergey/Java21_api.git'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Allure Report') {
            steps {
                script {
                    sh 'mvn allure:report'
                }
            }
            post {
                always {
                    allure includeProperties: false, results: [[path: 'target/allure-results']]
                }
            }
        }
    }

    post {
        success {
            echo '✅ Все тесты прошли успешно!'
        }
        failure {
            echo '❌ Тесты упали!'
        }
    }
}
