def call() {
    echo "Checking Java installation..."

    sh '''
        set -e

        if command -v java >/dev/null 2>&1; then
            echo "Java is already installed"
            java -version
        else
            echo "Java not found. Installing..."
            sudo apt-get update -y
            sudo apt-get install -y fontconfig openjdk-21-jre
            echo "Java installed successfully"
            java -version
        fi
    '''
}