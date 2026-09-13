def call(Map config = [:]) {

    def token        = config.sonarQubeTokenName        ?: error("sonarQubeTokenName is required")
    def key          = config.sonarQubeProjectKey       ?: error("sonarQubeProjectKey is required")
    def name         = config.sonarQubeProjectName      ?: error("sonarQubeProjectName is required")
    def installation = config.sonarQubeInstallationName ?: error("sonarQubeInstallationName is required")
    def scannerHome  = config.sonarQubeScannerHome      ?: tool('sonar-scanner')
    def extraProps   = config.extraProperties           ?: ""

    echo "========== SonarQube Analysis Started =========="

    withSonarQubeEnv(credentialsId: token, installationName: installation) {
        sh """
            ${scannerHome}/bin/sonar-scanner \
              -Dsonar.projectKey=${key} \
              -Dsonar.projectName='${name}' \
              ${extraProps}
        """
    }

    echo "========== SonarQube Analysis Completed =========="
}