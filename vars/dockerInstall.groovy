def call() {
    sh '''
        set -e

        # Install Docker if missing
        if ! command -v docker >/dev/null 2>&1; then
            echo "Docker not found. Installing..."
            sudo apt-get update -y
            sudo apt-get install -y docker.io
            sudo systemctl enable --now docker || true
        else
            echo "Docker is already installed"
        fi

        # Permission fix (reboot ki zaroorat nahi)
        sudo usermod -aG docker jenkins || true
        sudo chmod 666 /var/run/docker.sock || true

        # Docker Compose v2
        if ! sudo docker compose version >/dev/null 2>&1; then
            echo "Docker Compose not found. Installing..."
            sudo apt-get install -y docker-compose-v2 || true
        else
            echo "Docker Compose is already installed"
        fi

        sudo docker --version
        sudo docker compose version || true
    '''
}
