def call() {
    echo "Checking Trivy installation..."

    sh '''
        set -e

        if command -v trivy >/dev/null 2>&1; then
            echo "Trivy is already installed"
            trivy --version
            exit 0
        fi

        echo "Trivy not found. Installing..."

        sudo apt-get update -y
        sudo apt-get install -y wget gnupg lsb-release

        # Add Trivy GPG key if not present
        if [ ! -f /usr/share/keyrings/trivy.gpg ]; then
            wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key \
                | sudo gpg --dearmor \
                | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null
        fi

        # Add Trivy repository if not present
        if [ ! -f /etc/apt/sources.list.d/trivy.list ]; then
            echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb generic main" \
                | sudo tee /etc/apt/sources.list.d/trivy.list
        fi

        sudo apt-get update -y
        sudo apt-get install -y trivy

        echo "Trivy installed successfully"
        trivy --version
    '''
}