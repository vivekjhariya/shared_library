def call(Map config = [:]) {

    def composeFile   = config.composeFile   ?: "docker-compose.yml"
    def envFile       = config.envFile       ?: ".env"
    def pullImage     = config.pullImage     != false   // default true
    def doDown        = config.down          != false   // default true
    def buildImage    = config.build         ?: false   // default false
    def waitSeconds   = config.waitSeconds   ?: 8
    def serviceName   = config.service       ?: ""      // optional: ek service deploy
    def extraArgs     = config.extraArgs     ?: ""      // optional extra compose args

    echo "========== Docker Deploy Started =========="
    echo "Compose file : ${composeFile}"
    echo "Env file     : ${envFile}"
    echo "Pull images  : ${pullImage}"
    echo "Build        : ${buildImage}"

    sh """
        set -e

        if [ ! -f "${composeFile}" ]; then
            echo "ERROR: Compose file not found: ${composeFile}"
            exit 1
        fi

        ENV_OPT=""
        if [ -f "${envFile}" ]; then
            echo "Using env file: ${envFile}"
            ENV_OPT="--env-file ${envFile}"
        else
            echo "WARNING: ${envFile} not found. Continuing without env-file."
        fi

        SERVICE_OPT="${serviceName}"

        if [ "${pullImage}" = "true" ]; then
            echo "Pulling images..."
            docker compose -f "${composeFile}" \$ENV_OPT pull \$SERVICE_OPT ${extraArgs}
        fi

        if [ "${doDown}" = "true" ]; then
            echo "Stopping old containers..."
            docker compose -f "${composeFile}" \$ENV_OPT down || true
        fi

        BUILD_OPT=""
        if [ "${buildImage}" = "true" ]; then
            BUILD_OPT="--build"
        fi

        echo "Starting containers..."
        docker compose -f "${composeFile}" \$ENV_OPT up -d \$BUILD_OPT \$SERVICE_OPT ${extraArgs}

        echo "Waiting ${waitSeconds}s for services..."
        sleep ${waitSeconds}

        echo "Container status:"
        docker compose -f "${composeFile}" \$ENV_OPT ps

        echo "========== Docker Deploy Completed =========="
    """
}
