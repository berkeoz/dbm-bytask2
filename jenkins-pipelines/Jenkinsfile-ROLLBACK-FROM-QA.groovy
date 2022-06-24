@Library('dbmaestro-library') _

/**********   VARIABLES BEGIN   **********/
def dbmJenkinsNode = "node111-dbmaestro"
/**********    VARIABLES END    **********/

try{

  stage("Init"){
    node(dbmJenkinsNode){
      checkout scm
      //rootDir = pwd()
      myvars = load "jenkins-pipelines/vars.groovy"

      //if ticket number comes from a Jenkins parameter
      if(env.TICKET){
        issueKey = env.TICKET
        feedbackToJira = false
      }
      //if ticket number comes from Jira
      else{
        issueKey = env.JIRA_ISSUE_KEY
        feedbackToJira = true
      }
      packageFolder = issueKey
    }
  }

  stage("Rollback from ${myvars.qaEnvName}"){
    node (dbmJenkinsNode) {
      cleanWs()
      //checkout whole repo if needed, to be able to see package folders
      checkout scm
      packageFolder = issueKey
      dbmRollback(myvars.javaCmd, myvars.projectName, myvars.qaEnvName, packageFolder, myvars.createScriptsOnly, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  stage("Rollback from ${myvars.rsEnvName}"){
    node (dbmJenkinsNode) {
      dbmRollback(myvars.javaCmd, myvars.projectName, myvars.rsEnvName, packageFolder, myvars.createScriptsOnly, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  if(feedbackToJira){
    stage("Update Jira Issue"){
        withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
            jiraIssueAddLabel(issueKey, myvars.qaRbOkLabel)
        }
    }
  }

}
catch(e){
  if(feedbackToJira){
    withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
        jiraIssueAddLabel(issueKey, myvars.qaRbErrorLabel)
    }
  }
  throw e
}
finally{
  if(feedbackToJira){
    withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
        def comment = [ body: "${BUILD_URL}console" ]
        jiraIssueAddComment(issueKey, comment)
    }
  }
}
