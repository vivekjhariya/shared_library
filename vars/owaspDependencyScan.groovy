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
                --nvdApiKey ${env.NVD_API_KEY}
                --prettyPrint
            """.trim()
        )
    }

    dependencyCheckPublisher(
        pattern: '**/dependency-check-report.xml'
    )

    // Console output + artifacts
    sh '''
        set +e
        echo "========== OWASP Report Files =========="
        ls -la dependency-check-report.* 2>/dev/null || ls -la **/dependency-check-report.* 2>/dev/null || true

        if [ -f dependency-check-report.xml ]; then
            echo "========== OWASP XML Summary =========="
            echo "Vulnerabilities found (approx):"
            grep -o "<vulnerability>" dependency-check-report.xml | wc -l || true
            echo "---------- Report path ----------"
            echo "dependency-check-report.xml"
        else
            echo "WARNING: dependency-check-report.xml not found in workspace root"
            find . -name "dependency-check-report.xml" 2>/dev/null || true
        fi
    '''

    archiveArtifacts artifacts: '**/dependency-check-report.*', allowEmptyArchive: true

    echo "========== OWASP Dependency-Check Completed =========="
}
