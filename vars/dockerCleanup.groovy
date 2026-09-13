def call(Map config = [:]) {

    def imageName  = config.imageName  ?: error("imageName is required")
    def keepImages = config.keepImages ?: 2

    echo "========== Docker Cleanup Started =========="
    echo "Image Name     : ${imageName}"
    echo "Keep Latest    : ${keepImages} images"

    sh """
        set +e

        echo "Removing old images of ${imageName} (keeping latest ${keepImages})..."

        docker images ${imageName} --format '{{.ID}}' \
            | awk '!seen[\$0]++' \
            | tail -n +\$((${keepImages} + 1)) \
            | xargs -r docker rmi -f

        echo "Removing dangling images..."
        docker image prune -f

        echo "Cleanup completed"
    """

    echo "========== Docker Cleanup Completed =========="
}