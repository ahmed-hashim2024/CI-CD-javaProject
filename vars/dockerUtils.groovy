def build(String imageName, String imageTag) {
    echo "Building Docker Image..."
    sh "docker build -t ${imageName}:${imageTag} ."
}

def push(String imageName, String imageTag) {
    echo "️ Pushing to Docker Hub..."
    sh "docker push ${imageName}:${imageTag}"
}

def login(String user, String pass) {
    sh "echo ${pass} | docker login -u ${user} --password-stdin"
}

def deploy(String containerName, String imageName, String imageTag, String port) {
    script {
        echo "Deploying Container..."
        
        try {
            sh "docker stop ${containerName}"
            sh "docker rm ${containerName}"
        } catch (Exception e) {
            echo "No previous container found."
        }
        
        sh "docker run -d -p ${port}:8080 --name ${containerName} ${imageName}:${imageTag}"
    }
}
