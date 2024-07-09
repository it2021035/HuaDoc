pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }

    environment {
        DOCKER_TOKEN = credentials('docker-push-secret')
        DOCKER_USER = 'it2021089'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX_SP = 'ghcr.io/it2021089/sp'
        DOCKER_PREFIX_VUE = 'ghcr.io/it2021089/front'
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
    }

    stages {
       stage('Checkout Spring') {
            steps {
                git branch: 'master', url: 'git@github.com:it2021035/HuaDoc.git'
            }
        }
       stage('Checkout Vue') {
            steps {
                dir('localDocWebAppVue') {
                    git branch: 'main', url: 'https://github.com/it2021089/LocalDocWebAppVue.git'
                }
            }
        }
       stage('Preparation') {
            steps {
                dir('localDocWebApp') {
                    sh 'chmod +x ./mvnw'
                }
            }
        }
        stage('Test') {
            steps {
                dir('localDocWebApp') {
                    sh './mvnw test'
                }
            }
        }
        stage('Docker build and push Spring') {
            steps {
                dir('localDocWebApp') {
                    script {
                        def HEAD_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                        def TAG = "${HEAD_COMMIT}-${BUILD_ID}"
                        sh """
                            docker build --rm -t ${env.DOCKER_PREFIX_SP}:${TAG} -t ${env.DOCKER_PREFIX_SP}:latest -f nonroot.Dockerfile .
                            echo ${env.DOCKER_TOKEN} | docker login ${env.DOCKER_SERVER} -u ${env.DOCKER_USER} --password-stdin
                            docker push ${env.DOCKER_PREFIX_SP}:${TAG}
                            docker push ${env.DOCKER_PREFIX_SP}:latest
                        """
                    }
                }
            }
        }
        stage('Docker build and push Vue') {
            steps {
                dir('localDocWebAppVue') {
                    script {
                        sh 'pwd'
                        sh 'ls -l'
                        def HEAD_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                        def TAG = "${HEAD_COMMIT}-${BUILD_ID}"
                        sh """
                            docker build --rm -t ${env.DOCKER_PREFIX_VUE}:${TAG} -t ${env.DOCKER_PREFIX_VUE}:latest -f localdocwebapp-vue/Dockerfile .
                            echo ${env.DOCKER_TOKEN} | docker login ${env.DOCKER_SERVER} -u ${env.DOCKER_USER} --password-stdin
                            docker push ${env.DOCKER_PREFIX_VUE}:${TAG}
                            docker push ${env.DOCKER_PREFIX_VUE}:latest
                        """
                    }
                }
            }
        }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }
        stage('deploy to k8s') {
            steps {
                script {
                    def HEAD_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    def TAG = "${HEAD_COMMIT}-${BUILD_ID}"
                    sh """
                        kubectl set image deployment/spring-deployment sp=${env.DOCKER_PREFIX_SP}:${TAG}
                        kubectl rollout status deployment spring-deployment --watch --timeout=2m
                    """
                }
            }
        }
    }
}
