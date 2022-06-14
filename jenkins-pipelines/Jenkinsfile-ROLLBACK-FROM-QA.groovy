@Library('dbmaestro-library') _

/**********   VARIABLES BEGIN   **********/

//DBmaestro
def projectName = "MsSqlByTask"
def server = "10.0.1.111:8017" //DBmaestro Agent Windows Server hostname
def authType = "DBmaestroAccount"
def dbmCredentials = "dbmaestro-user-automation-token"
def rsEnvName = "RS"
def qaEnvName = "QA"
def createScriptsOnly = "False"
def javaCmd = "java -jar \"C:\\Program Files (x86)\\DBmaestro\\DOP Server\\Agent\\DBmaestroAgent.jar\""
def useSSL = "n" //use SSL to communicate with DBmaestro?

//Jenkins
def dbmJenkinsNode = "node111-dbmaestro"

//Git
def rootFolder = "packages\\mssql\\regular" //DBmaestro packages root folder

//Jira - optional - no need to delete anything if you are not using Jira
def jiraSite = "LOCAL" //from "Configure System" screen, field added by Jira Pipeline Steps plugin
def label = "RolledBack-${qaEnvName}"
def errorLabel = "Failed-Rollback-${qaEnvName}"

/**********    VARIABLES END    **********/

def issueKey //Ticket or Jira Issue associated to DBmaestro Package
def feedbackToJira //return feedback to Jira

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

try{
  
  stage("Rollback from ${qaEnvName}"){
    node (dbmJenkinsNode) {
      cleanWs()
      //checkout whole repo if needed, to be able to see package folders
      checkout scm
      packageFolder = issueKey
      dbmRollback(javaCmd, projectName, qaEnvName, packageFolder, createScriptsOnly, server, authType, useSSL, dbmCredentials)
    }
  }

  stage("Rollback from ${rsEnvName}"){
    node (dbmJenkinsNode) {
      dbmRollback(javaCmd, projectName, rsEnvName, packageFolder, createScriptsOnly, server, authType, useSSL, dbmCredentials)
    }
  }

  if(feedbackToJira){
    stage("Update Jira Issue"){
        withEnv(["JIRA_SITE=${jiraSite}"]) {
            jiraIssueAddLabel(issueKey, label)
        }
    }
  }

}
catch(e){
  if(feedbackToJira){
    withEnv(["JIRA_SITE=${jiraSite}"]) {
        jiraIssueAddLabel(issueKey, errorLabel)
    }
  }
  throw e
}
finally{
  if(feedbackToJira){
    withEnv(["JIRA_SITE=${jiraSite}"]) {
        def comment = [ body: "${BUILD_URL}console" ]
        jiraIssueAddComment(issueKey, comment)
    }
  }
}
