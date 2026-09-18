def call() {
    sh '''
        # Install Docker if missing
        if ! command -v docker >/dev/null 2>&1; then
            echo "Docker not found. Installing..."
            sudo apt-get update -y || { echo "❌ apt-get update failed"; exit 1; }
            sudo apt-get install -y docker.io || { echo "❌ docker install failed"; exit 1; }
            sudo systemctl enable --now docker
            echo "✅ Docker installed"
        else
            echo "✅ Docker already installed: $(docker --version 2>/dev/null || sudo docker --version)"
        fi

        # Ensure docker is running
        sudo systemctl is-active docker >/dev/null 2>&1 || sudo systemctl start docker

        # Permission fix (working in a same session)
        sudo usermod -aG docker jenkins 2>/dev/null || true
        sudo chmod 666 /var/run/docker.sock 2>/dev/null || true

        # Docker Compose v2 (try both package names)
        if ! sudo docker compose version >/dev/null 2>&1; then
            echo "Docker Compose not found. Installing..."
            sudo apt-get install -y docker-compose-v2 2>/dev/null || \
            sudo apt-get install -y docker-compose-plugin 2>/dev/null || \
            echo "⚠️  Docker Compose install failed (manual check needed)"
        fi

        # Final verification
        echo "---"
        sudo docker --version
        sudo docker compose version 2>/dev/null || echo "⚠️  Compose not available"
        echo "---"
    '''
}   
