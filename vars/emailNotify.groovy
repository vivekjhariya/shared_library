def call(String status, String toEmail) {

    def subject = ""
    def color   = ""
    def heading = ""
    def message = ""

    if (status == "SUCCESS") {
        subject = "✅ SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        color   = "#2ecc71"
        heading = "Build Successful"
        message = "The Jenkins pipeline completed successfully."
    } else if (status == "UNSTABLE") {
        subject = "⚠️ UNSTABLE: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        color   = "#f39c12"
        heading = "Build Unstable"
        message = "The Jenkins pipeline completed with some warnings."
    } else {
        subject = "❌ FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        color   = "#e74c3c"
        heading = "Build Failed"
        message = "The Jenkins pipeline has failed. Please investigate."
    }

    emailext(
        to: toEmail,
        from: 'vivekjhariya242@gmail.com',
        subject: subject,
        mimeType: 'text/html',
        body: """
            <div style="font-family: Arial, sans-serif;">
                <h2 style="color:${color};">${heading}</h2>
                <p>Hello Team,</p>
                <p>${message}</p>

                <table border="1" cellpadding="8" cellspacing="0" style="border-collapse: collapse;">
                    <tr>
                        <td><b>Project</b></td>
                        <td>${env.JOB_NAME}</td>
                    </tr>
                    <tr>
                        <td><b>Build Number</b></td>
                        <td>${env.BUILD_NUMBER}</td>
                    </tr>
                    <tr>
                        <td><b>Status</b></td>
                        <td style="color:${color};"><b>${status}</b></td>
                    </tr>
                    <tr>
                        <td><b>Branch</b></td>
                        <td>${env.GIT_BRANCH ?: 'N/A'}</td>
                    </tr>
                </table>

                <p>🔗 <a href="${env.BUILD_URL}">View Build Details</a></p>

                <p>
                    Regards,<br>
                    <b>Jenkins CI/CD</b>
                </p>
            </div>
        """
    )
}