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
            script {
                withCredentials([
                    string(credentialsId: 'telegram-token', variable: 'TOKEN'),
                    string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')
                ]) {
                    sh '''
                        curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage \
                        -d chat_id=${CHAT_ID} \
                        -d text="✅ Сборка #${BUILD_NUMBER} успешна!
📊 Статус: SUCCESS
📁 Проект: ${JOB_NAME}
🔗 Отчёт: ${BUILD_URL}allure"
                    '''
                }
            }
        }
        failure {
            echo '❌ Тесты упали!'
            script {
                withCredentials([
                    string(credentialsId: 'telegram-token', variable: 'TOKEN'),
                    string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')
                ]) {
                    sh '''
                        curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage \
                        -d chat_id=${CHAT_ID} \
                        -d text="❌ Сборка #${BUILD_NUMBER} упала!
📊 Статус: FAILURE
📁 Проект: ${JOB_NAME}
🔗 Отчёт: ${BUILD_URL}allure"
                    '''
                }
            }
        }
        unstable {
            echo '⚠️ Тесты упали, но отчёт сгенерирован. Смотри Allure Report.'
        }
    }
}