def call(Map config = [:]) {

    def image        = config.image ?: error("image is required (e.g. myapp:latest)")
    def severity     = config.severity ?: "HIGH,CRITICAL"
    def format       = config.format ?: "table"
    def exitCode     = config.exitCode ?: 1          // 1 = fail on vulnerabilities
    def outputFile   = config.outputFile ?: ""

    echo "========== Trivy Image Scan Started =========="
    echo "Scanning image: ${image}"

    sh """
        set -e
        TIMESTAMP=\$(date '+%Y%m%d-%H%M%S')
        SAFE_TAG=\$(echo "${image}" | sed 's#[/:]#_#g')
        REPORT_NAME=\${OUTPUT_FILE:-trivy-image-\${SAFE_TAG}-\${TIMESTAMP}.txt}

        trivy image \
            --severity ${severity} \
            --format ${format} \
            --exit-code ${exitCode} \
            ${image} > "\$REPORT_NAME"

        echo "Report generated: \$REPORT_NAME"
    """

    echo "========== Trivy Image Scan Completed =========="
}