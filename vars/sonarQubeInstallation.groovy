def call() {
    echo "Checking SonarQube container status..."

    sh '''
        set -e
        CONTAINER_NAME="sonarqube-server"

        # Ensure socket is usable in this job
        sudo chmod 666 /var/run/docker.sock || true

        if sudo docker ps -a --format "{{.Names}}" | grep -qw "$CONTAINER_NAME"; then
            if sudo docker ps --format "{{.Names}}" | grep -qw "$CONTAINER_NAME"; then
                echo "SonarQube is already running"
            else
                echo "SonarQube exists but is stopped. Starting it..."
                sudo docker start "$CONTAINER_NAME"
            fi
        else
            echo "SonarQube container not found. Creating and starting..."
            sudo docker run -d \
                --name "$CONTAINER_NAME" \
                --restart unless-stopped \
                -p 9000:9000 \
                -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
                -v sonarqube_data:/opt/sonarqube/data \
                -v sonarqube_logs:/opt/sonarqube/logs \
                -v sonarqube_extensions:/opt/sonarqube/extensions \
                sonarqube:lts-community
        fi
    '''
}
