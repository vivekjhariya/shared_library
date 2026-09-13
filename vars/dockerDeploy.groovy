def call(Map config = [:]) {

    def composeFile = config.composeFile ?: "docker-compose.yml"
    def pullImage   = config.pullImage != false     // default true

    echo "========== Docker Deploy Started =========="

    sh """
        set -e

        if [ "${pullImage}" = "true" ]; then
            echo "Pulling latest image from Docker Hub..."
            docker compose -f ${composeFile} pull
        fi

        echo "Stopping old containers..."
        docker compose -f ${composeFile} down || true

        echo "Starting containers with latest image..."
        docker compose -f ${composeFile} up -d

        echo "Waiting for services to start..."
        sleep 8

        echo "Current container status:"
        docker compose -f ${composeFile} ps
    """

    echo "========== Docker Deploy Completed =========="
}