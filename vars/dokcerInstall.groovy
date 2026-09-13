def call() {
    sh '''
        set -e

        # Install Docker if not present
        if ! command -v docker >/dev/null 2>&1; then
            echo "Docker not found. Installing..."
            sudo apt-get update -y
            sudo apt-get install -y docker.io
            sudo usermod -aG docker jenkins || true
            sudo usermod -aG docker $USER || true
            newgrp docker 
            sudo systemctl enable --now docker
            echo "Docker installed successfully"
        else
            echo "Docker is already installed"
        fi

        # Install Docker Compose V2 if not present
        if ! docker compose version >/dev/null 2>&1; then
            echo "Docker Compose not found. Installing..."
            sudo apt-get install -y docker-compose-v2
            echo "Docker Compose installed successfully"
        else
            echo "Docker Compose is already installed"
        fi

        docker --version
        docker compose version
    '''
}