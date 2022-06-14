def call(String issueKey, int transitionId){
    helpMsgbox("Jira Issue ${issueKey}: Transition to ${transitionId}")
    def transitionInput = [ transition: [ id: transitionId ] ]
    jiraTransitionIssue idOrKey: issueKey, input: transitionInput
}