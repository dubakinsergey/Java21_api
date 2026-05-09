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
                script {
                    try {
                        sh 'mvn clean test'
                    } catch (Exception e) {
                        echo 'Тесты упали, но Allure отчёт всё равно будет сгенерирован.'
                        currentBuild.result = 'UNSTABLE'
                    }
                }
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
        unstable {
            echo '⚠️ Тесты упали, но отчёт сгенерирован. Смотри Allure Report.'
        }
    }
}