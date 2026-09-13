def call(Map config = [:]) {

    def url           = config.url           ?: error("Git repository URL is required")
    def branch        = config.branch        ?: "main"
    def credentialsId = config.credentialsId ?: null

    echo "========== Git Clone Started =========="
    echo "Repository : ${url}"
    echo "Branch     : ${branch}"

    if (credentialsId) {
        // Private repo (Jenkins Credentials se)
        git url: url,
            branch: branch,
            credentialsId: credentialsId
    } else {
        // Public repo
        git url: url,
            branch: branch
    }

    echo "========== Git Clone Completed =========="
}