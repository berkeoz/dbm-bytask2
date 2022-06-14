def call(String issueKey, Object comment){
    helpMsgbox("Jira Issue ${issueKey}: Add Comment")
    response = jiraAddComment idOrKey: issueKey, input: comment
    echo "Jira Response Successful?: " + response.successful.toString()
    echo "Jira Response Data: " + response.data.toString()
}