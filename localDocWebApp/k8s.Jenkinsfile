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
                    sh '''
                        HEAD_COMMIT=$(git rev-parse --short HEAD)
                        TAG=$HEAD_COMMIT-$BUILD_ID
                        docker build --rm -t $DOCKER_PREFIX_SP:$TAG -t $DOCKER_PREFIX_SP:latest -f nonroot.Dockerfile .
                        echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                        docker push $DOCKER_PREFIX_SP --all-tags
                        '''
                }
            }
        }
        stage('Docker build and push Vue') {
            steps {
                dir('localDocWebAppVue') {
                    sh '''
                        HEAD_COMMIT=$(git rev-parse --short HEAD)
                        TAG=$HEAD_COMMIT-$BUILD_ID
                        docker build --rm -t $DOCKER_PREFIX_VUE:$TAG -t $DOCKER_PREFIX_VUE:latest -f localdocwebapp-vue/Dockerfile .
                        echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                        docker push $DOCKER_PREFIX_VUE --all-tags
                        '''
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
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    ./kubectl set image deployment/spring sp=$DOCKER_PREFIX:$TAG
                    ./kubectl rollout status deployment spring --watch --timeout=2m
                '''
            }
        }
    }
}
