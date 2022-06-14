@Library('dbmaestro-library') _

/**********   VARIABLES BEGIN   **********/

//DBmaestro
def projectName = "MsSqlByTask"
def server = "10.0.1.111:8017" //DBmaestro Agent Windows Server hostname
def dbmCredentials = "dbmaestro-user-automation-token"
def authType = "DBmaestroAccount"
def rsEnvName = "RS" //name of Release Source Environment in your DBmaestro Project
def qaEnvName = "QA" //name of QA Environment in your DBmaestro Project
def javaCmd = "java -jar \"C:\\Program Files (x86)\\DBmaestro\\DOP Server\\Agent\\DBmaestroAgent.jar\""
def packageType = "Regular"
def useSSL = "n" //use SSL to communicate with DBmaestro?

//Jenkins
def dbmJenkinsNode = "node111-dbmaestro"

//Git
def rootFolder = "packages\\mssql\\regular" //DBmaestro packages root folder

//Jira - optional - no need to delete anything if you are not using Jira
def jiraSite = "LOCAL" //from "Configure System" screen, field added by Jira Pipeline Steps plugin
def transitionId = 81 //Deployed in QA
def label = "In-QA"
def errorLabel = "Failed-Deploy-QA"

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
  
  stage("Packaging") {
    node (dbmJenkinsNode) {
      cleanWs()
      //checkout whole repo if needed, to be able to see package folders
      checkout scm
      packageFolder = issueKey      
      dbmCreateManifestFile(javaCmd, rootFolder, packageFolder, packageType)
      helpZipPackageFolder(rootFolder, packageFolder)
      dbmPackage(javaCmd, projectName, rootFolder, packageFolder, server, authType, useSSL, dbmCredentials)
    }
  }
  
  //DRY RUN ENV (PRECHECK)
  stage("PreCheck"){
    node (dbmJenkinsNode) {
      dbmPreCheck(javaCmd, projectName, packageFolder, server, authType, useSSL, dbmCredentials)
    }
  }

  //RELEASE SOURCE ENV (READY FOR RELEASE TO NEXT ENVS)
  stage("Promote to ${rsEnvName}"){
    node (dbmJenkinsNode) {
      dbmUpgrade(javaCmd, projectName, rsEnvName, packageFolder, server, authType, useSSL, dbmCredentials)
    }
  }

  //QA ENV
  stage("Promote to ${qaEnvName}"){
    node (dbmJenkinsNode) {
      dbmUpgrade(javaCmd, projectName, qaEnvName, packageFolder, server, authType, useSSL, dbmCredentials)
    }
  }

  if(feedbackToJira){
    stage("Update Jira Issue"){
        withEnv(["JIRA_SITE=${jiraSite}"]) {
            jiraIssueTransitionTo(issueKey, transitionId)
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
