def call(Map config = [:]) {

    def installation = config.odcInstallation ?: error("odcInstallation name is required")
    def failOnCVSS   = config.failOnCVSS ?: 7
    def scanPath     = config.scanPath ?: "."
    def format       = config.format ?: "XML"

    echo "========== OWASP Dependency-Check Started =========="

    dependencyCheck(
        odcInstallation: installation,
        additionalArguments: "--scan ${scanPath} --format ${format} --failOnCVSS ${failOnCVSS} --prettyPrint"
    )

    dependencyCheckPublisher(
        pattern: '**/dependency-check-report.xml'
    )

    echo "========== OWASP Dependency-Check Completed =========="
}