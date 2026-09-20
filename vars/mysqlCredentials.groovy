def call(Map config = [:]) {

    def jwtCredId        = config.jwtCredId        ?: "jwt-secret"
    def rootPassCredId   = config.rootPassCredId   ?: "mysql-root-password"
    def userPassCredId   = config.userPassCredId   ?: "mysql-password"
    def dbName           = config.dbName           ?: "myntraa_db"
    def dbUser           = config.dbUser           ?: "myntraa"
    def appPort          = config.appPort          ?: "3001"
    def envFile          = config.envFile          ?: ".env"

    echo "========== Creating deployment .env file =========="

    withCredentials([
        string(credentialsId: jwtCredId,      variable: 'JWT_SECRET'),
        string(credentialsId: rootPassCredId, variable: 'MYSQL_ROOT_PASSWORD'),
        string(credentialsId: userPassCredId, variable: 'MYSQL_PASSWORD')
    ]) {
        sh """
            set -e
            cat > ${envFile} <<EOF
APP_PORT=${appPort}
JWT_SECRET=${env.JWT_SECRET}
MYSQL_ROOT_PASSWORD=${env.MYSQL_ROOT_PASSWORD}
MYSQL_DATABASE=${dbName}
MYSQL_USER=${dbUser}
MYSQL_PASSWORD=${env.MYSQL_PASSWORD}
EOF
           echo ".env created at ${envFile}"
            echo "Checking keys present (values hidden):"
            grep -E '^(APP_PORT|JWT_SECRET|MYSQL_ROOT_PASSWORD|MYSQL_DATABASE|MYSQL_USER|MYSQL_PASSWORD)=' ${envFile} | cut -d= -f1
            wc -l ${envFile}
        """
    }

    echo "========== .env setup completed =========="
}
