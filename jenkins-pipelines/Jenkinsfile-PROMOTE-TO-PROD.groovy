@Library('dbmaestro-library') _

/**********   VARIABLES BEGIN   **********/
def varsPath = "jenkins-pipelines/vars.groovy"
def dbmJenkinsNode = "node111-dbmaestro"
/**********    VARIABLES END    **********/

try{

  stage("Init"){
    node(dbmJenkinsNode){
      checkout scm
      //rootDir = pwd()
      myvars = load varsPath
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

  stage("Promote to ${myvars.prodEnvName}"){
    node (dbmJenkinsNode) {
      cleanWs()
      //checkout whole repo if needed, to be able to see package folders
      checkout scm
      packageFolder = issueKey
      dbmUpgrade(myvars.javaCmd, myvars.projectName, myvars.prodEnvName, packageFolder, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  if(feedbackToJira){
    stage("Update Jira Issue"){
        withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
            jiraIssueTransitionTo(issueKey, myvars.prodUpgTransitionId)
            jiraIssueAddLabel(issueKey, myvars.prodUpgOkLabel)
        }
    }
  }
}
catch(e){
  if(feedbackToJira){
    withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
        jiraIssueAddLabel(issueKey, myvars.prodUpgErrorLabel)
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