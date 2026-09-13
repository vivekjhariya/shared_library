def call(Map config = [:]) {

    def timeoutMinutes = config.timeout ?: 5
    def abortOnFailure = config.abortPipeline != false   // default true

    echo "========== Checking SonarQube Quality Gate =========="

    timeout(time: timeoutMinutes, unit: 'MINUTES') {
        def qg = waitForQualityGate()

        if (qg.status != 'OK') {
            if (abortOnFailure) {
                error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
            } else {
                echo "Quality Gate failed: ${qg.status} (continuing pipeline as abortPipeline=false)"
            }
        } else {
            echo "Quality Gate passed successfully!"
        }
    }

    echo "========== SonarQube Quality Gate Check Completed =========="
}