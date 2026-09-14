def call(Map config = [:]) {

    def installation = config.odcInstallation ?: error("odcInstallation name is required")
    def failOnCVSS   = config.failOnCVSS ?: 7
    def scanPath     = config.scanPath ?: "."
    def nvdApiKeyId  = config.nvdApiKeyId ?: "nvd-api-key"   // Jenkins credential ID

    echo "========== OWASP Dependency-Check Started =========="

    withCredentials([string(credentialsId: nvdApiKeyId, variable: 'NVD_API_KEY')]) {
        dependencyCheck(
            odcInstallation: installation,
            additionalArguments: """
                --scan ${scanPath}
                --format XML
                --failOnCVSS ${failOnCVSS}
                --nvdApiKey ${NVD_API_KEY}
                --prettyPrint
            """.trim()
        )
    }

    dependencyCheckPublisher(
        pattern: '**/dependency-check-report.xml'
    )

    echo "========== OWASP Dependency-Check Completed =========="
}
