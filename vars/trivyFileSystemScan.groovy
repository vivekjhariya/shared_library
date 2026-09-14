def call(Map config = [:]) {

    def path         = config.path ?: "."
    def severity     = config.severity ?: "HIGH,CRITICAL"
    def format       = config.format ?: "table"
    def exitCode     = config.exitCode ?: 1
    def outputFile   = config.outputFile ?: ""

    echo "========== Trivy File System Scan Started =========="

    sh """
        set -e
        TIMESTAMP=\$(date '+%%Y%%m%%d-%%H%%M%%S')
    REPORT_NAME=trivy-fs-\${TIMESTAMP}.txt

    trivy fs \\
        --severity ${severity} \\
        --format ${format} \\
        --exit-code ${exitCode} \\
        ${path} > "\$REPORT_NAME" || true

    echo "========== Trivy FS Scan Report =========="
    cat "\$REPORT_NAME"
    """

    echo "========== Trivy File System Scan Completed =========="
}
