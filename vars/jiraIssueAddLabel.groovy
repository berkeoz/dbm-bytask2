def call(String issueKey, String label){
    helpMsgbox("Jira Issue ${issueKey}: Add Label ${label}")
    def testIssue = [fields: [labels: [label]]]
    def queryParams = [notifyUsers: false]
    response = jiraEditIssue idOrKey: issueKey, queryParams: queryParams, issue: testIssue
    echo "Jira Response Successful?: " + response.successful.toString()
    echo "Jira Response Data: " + response.data.toString()
}