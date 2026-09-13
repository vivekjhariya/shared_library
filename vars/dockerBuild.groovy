def call(Map config = [:]) {

    def imageName   = config.imageName   ?: error("imageName is required")
    def buildNumber = config.buildNumber ?: env.BUILD_NUMBER
    def dockerfile  = config.dockerfile  ?: "Dockerfile"
    def context     = config.context     ?: "."

    def imageTag = "${imageName}:build-${buildNumber}"

    echo "========== Docker Build Started =========="
    echo "Image Tag : ${imageTag}"

    sh """
        set -e
        docker build -t ${imageTag} -f ${dockerfile} ${context}
    """

    // Save image tag for next stages
    env.IMAGE_TAG = imageTag

    echo "Docker image built successfully: ${imageTag}"
    echo "========== Docker Build Completed =========="

    return imageTag
}