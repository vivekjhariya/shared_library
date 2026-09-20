def call(Map config = [:]) {
    def installation = config.odcInstallation ?: error("odcInstallation name is required")
    def failOnCVSS   = config.failOnCVSS ?: 7
    def scanPath     = config.scanPath ?: "."
    def nvdApiKeyId  = config.nvdApiKeyId ?: "nvd-api-key"

    echo "========== OWASP Dependency-Check Started =========="

    withCredentials([string(credentialsId: nvdApiKeyId, variable: 'NVD_API_KEY')]) {
        dependencyCheck(
            odcInstallation: installation,
            additionalArguments: "--scan ${scanPath} --format XML --format HTML --prettyPrint --failOnCVSS ${failOnCVSS} --nvdApiKey ${env.NVD_API_KEY}"
        )
    }

    // Jenkins UI trend / issues (XML se)
    dependencyCheckPublisher(
        pattern: '**/dependency-check-report.xml'
    )

    sh '''
        set +e
        echo "========== OWASP Report Files =========="
        find . -name "dependency-check-report.*" -type f 2>/dev/null

        REPORT_XML=$(find . -name "dependency-check-report.xml" -type f 2>/dev/null | head -n 1)
        REPORT_HTML=$(find . -name "dependency-check-report.html" -type f 2>/dev/null | head -n 1)

        if [ -n "$REPORT_XML" ]; then
            echo "XML Report : $REPORT_XML"
            echo "Vulnerabilities (approx):"
            grep -o "<vulnerability>" "$REPORT_XML" | wc -l
        fi

        if [ -n "$REPORT_HTML" ]; then
            echo "HTML Report: $REPORT_HTML"
        else
            echo "WARNING: dependency-check-report.html not found"
        fi
    '''

    // HTML + XML downloadable artifacts
    archiveArtifacts artifacts: '**/dependency-check-report.*', allowEmptyArchive: true

    echo "========== OWASP Dependency-Check Completed =========="
}
