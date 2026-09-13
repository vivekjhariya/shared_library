def call(Map config = [:]) {

    def credentialsId = config.credentialsId ?: error("credentialsId is required")
    def imageName     = config.imageName     ?: error("imageName is required")
    def buildNumber   = config.buildNumber   ?: env.BUILD_NUMBER
    def pushLatest    = config.pushLatest    ?: true

    def sourceImage = env.IMAGE_TAG ?: "${imageName}:build-${buildNumber}"

    echo "========== Docker Push Started =========="
    echo "Source Image : ${sourceImage}"

    withCredentials([
        usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {
        sh """
            set -e

            TARGET_IMAGE=\$DOCKER_USER/${imageName}:build-${buildNumber}

            echo "Logging into Docker Hub..."
            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin

            echo "Tagging image..."
            docker tag ${sourceImage} \$TARGET_IMAGE

            echo "Pushing image: \$TARGET_IMAGE"
            docker push \$TARGET_IMAGE

            if [ "${pushLatest}" = "true" ]; then
                LATEST_IMAGE=\$DOCKER_USER/${imageName}:latest
                docker tag ${sourceImage} \$LATEST_IMAGE
                echo "Pushing latest tag: \$LATEST_IMAGE"
                docker push \$LATEST_IMAGE
            fi

            echo "Docker push completed successfully"
        """
    }

    echo "========== Docker Push Completed =========="
}