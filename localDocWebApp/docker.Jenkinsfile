pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }

    environment {
        DOCKER_TOKEN = credentials('docker-push-secret')
        DOCKER_USER = 'it2021089'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = 'ghcr.io/it2021089/sp'
    }

    stages {
       stage('Checkout') {
            steps {
                git branch: 'master', url: 'git@github.com:it2021035/HuaDoc.git'
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
        stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest  -f nonroot.Dockerfile .
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }
        stage('Install project with docker compose') {
                    steps {
                        sh '''
                            export ANSIBLE_CONFIG=~/workspace/ansible/ansible-playground/ansible.cfg
                            ansible-playbook -i ~/workspace/ansible/ansible-playground/hosts.yaml -l appserver ~/workspace/ansible/ansible-playground/playbooks/spring-vue-docker.yaml
                        '''
                    }
         }
    }

}