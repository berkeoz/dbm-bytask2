@Library('dbmaestro-library') _

/**********   LOCAL VARIABLES BEGIN   **********/
def varsPath = "jenkins-pipelines/vars.groovy"
def dbmJenkinsNode = "node111-dbmaestro"
/**********    LOCAL VARIABLES END    **********/

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

  stage("Packaging") {
    node (dbmJenkinsNode) {
      cleanWs()
      //checkout whole repo if needed, to be able to see package folders
      checkout scm
      dbmCreateManifestFile(myvars.javaCmd, myvars.rootFolder, packageFolder, myvars.packageType)
      helpZipPackageFolder(myvars.rootFolder, packageFolder)
      dbmPackage(myvars.javaCmd, myvars.projectName, myvars.rootFolder, packageFolder, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  //DRY RUN ENV (PRECHECK)
  stage("PreCheck"){
    node (dbmJenkinsNode) {
      dbmPreCheck(myvars.javaCmd, myvars.projectName, myvars.packageFolder, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  //RELEASE SOURCE ENV (READY FOR RELEASE TO NEXT ENVS)
  stage("Promote to ${myvars.rsEnvName}"){
    node (dbmJenkinsNode) {
      dbmUpgrade(myvars.javaCmd, myvars.projectName, myvars.rsEnvName, packageFolder, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  //QA ENV
  stage("Promote to ${myvars.qaEnvName}"){
    node (dbmJenkinsNode) {
      dbmUpgrade(myvars.javaCmd, myvars.projectName, myvars.qaEnvName, packageFolder, myvars.server, myvars.authType, myvars.useSSL, myvars.dbmCredentials)
    }
  }

  if(feedbackToJira){
    stage("Update Jira Issue"){
        withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
            jiraIssueTransitionTo(issueKey, myvars.qaUpgTransitionId)
            jiraIssueAddLabel(issueKey, myvars.qaUpgOkLabel)
        }
    }
  }

}
catch(e){
  if(feedbackToJira){
    withEnv(["JIRA_SITE=${myvars.jiraSite}"]) {
        jiraIssueAddLabel(issueKey, myvars.qaUpgErrorLabel)
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
