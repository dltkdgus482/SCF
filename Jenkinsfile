pipeline {
    agent any

    environment {
        DOCKER_HUB_NAMESPACE = "zlxldgus123"
        DOCKER_TAG = "latest"
        DEPLOY_DIR = "/home/ubuntu/deploy"
        GIT_BRANCH = "back/develop"
    }

    stages {
        stage('Git Clone') {
            steps {
                script {
                    git branch: "${GIT_BRANCH}", credentialsId: 'gitlab_login', url: 'https://lab.ssafy.com/s11-webmobile1-sub2/S11P12E202.git'
                }
            }
        }

        stage('Show Git Branch') {
            steps {
                script {
                    def branch = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    echo "Current Git Branch: ${branch}"
                }
            }
        }

        stage('Show Directory Structure') {
            steps {
                script {
                    sh 'find .'
                }
            }
        }

        stage('Show Dockerfiles') {
            steps {
                script {
                    def services = ['battle', 'user', 'multi']
                    for (service in services) {
                        def dockerfilePath = "backend/${service}/Dockerfile"
                        def dockerfileExists = fileExists(dockerfilePath)

                        if (dockerfileExists) {
                            echo "Dockerfile for ${service} exists, displaying content."
                            sh "cat ${dockerfilePath}"
                        } else {
                            echo "Dockerfile for ${service} does not exist, skipping."
                        }
                    }
                }
            }
        }

        stage('Show Docker Compose File') {
            steps {
                script {
                    def dockerComposeFilePath = "${WORKSPACE}/docker-compose.yml"
                    def dockerComposeFileExists = fileExists(dockerComposeFilePath)

                    if (dockerComposeFileExists) {
                        echo "docker-compose.yml exists, displaying content."
                        sh "cat ${dockerComposeFilePath}"
                    } else {
                        echo "docker-compose.yml does not exist."
                    }
                }
            }
        }

        stage('Build Docker Images and Push') {
            steps {
                script {
                    def services = ['problem', 'user', 'multi']
                    for (service in services) {
                        def image = "${DOCKER_HUB_NAMESPACE}/${service}:${DOCKER_TAG}"
                        def dockerfilePath = "backend/${service}/Dockerfile"

                        // Check if Dockerfile exists
                        def dockerfileExists = fileExists(dockerfilePath)

                        if (dockerfileExists) {
                            echo "Dockerfile for ${service} exists, proceeding with build and push."
                            withCredentials([usernamePassword(credentialsId: 'dockerhub_credentials', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                                sh """
                                docker build -t ${image} -f ${dockerfilePath} backend/${service}
                                docker login -u \$DOCKER_HUB_USERNAME -p \$DOCKER_HUB_PASSWORD
                                docker push ${image}
                                """
                            }
                        } else {
                            echo "Dockerfile for ${service} does not exist, skipping."
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Docker Compose 설치 여부 확인 및 설치
                    sh """
                    export PATH=\$PATH:/usr/local/bin
                    if ! command -v docker-compose &> /dev/null
                    then
                        echo "docker-compose could not be found. Installing..."
                        curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-\$(uname -s)-\$(uname -m)" -o /usr/local/bin/docker-compose
                        chmod +x /usr/local/bin/docker-compose
                    else
                        echo "docker-compose is already installed."
                    fi
                    """

                    // DEPLOY_DIR 디렉토리가 존재하지 않으면 생성하고 파일 목록 확인
                    sh """
                    echo "Current User: \$(whoami)"
                    if [ ! -d "${DEPLOY_DIR}" ]; then
                        mkdir -p ${DEPLOY_DIR}
                    fi

                    cd ${DEPLOY_DIR}

                    # 리포지토리에서 가져온 docker-compose.yml 파일 복사
                    cp ${WORKSPACE}/docker-compose.yml ${DEPLOY_DIR}/

                    echo "Directory Contents:"
                    ls -al

                    if [ -f "docker-compose.yml" ]; then
                        echo "docker-compose.yml exists."
                        cat docker-compose.yml
                    else
                        echo "docker-compose.yml does not exist."
                        exit 1
                    fi

                    docker-compose down
                    docker-compose up --build -d
                    """
                }
            }
        }

        stage('Docker Cleanup') {
            steps {
                script {
                    sh """
                    echo "Cleaning up old Docker images..."
                    docker images --filter "dangling=false" --filter "reference=${DOCKER_HUB_NAMESPACE}/*" --format "{{.ID}}:{{.Tag}}" | grep -v ":${DOCKER_TAG}" | awk -F ':' '{print \$1}' | xargs -r docker rmi
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
