import groovy.transform.Field

//DBmaestro
@Field
def projectName = "MsSqlByTask"
@Field
def server = "10.0.1.111:8017" //DBmaestro Agent Windows Server hostname
@Field
def dbmCredentials = "dbmaestro-user-automation-token"
@Field
def authType = "DBmaestroAccount"
@Field
def javaCmd = "java -jar \"C:\\Program Files (x86)\\DBmaestro\\DOP Server\\Agent\\DBmaestroAgent.jar\""
@Field
def packageType = "Regular"
@Field
def useSSL = "n" //use SSL to communicate with DBmaestro?
@Field
def createScriptsOnly = "False" //rollbacks

//Jenkins
@Field
def dbmJenkinsNode = "node111-dbmaestro"

//Git
@Field
def rootFolder = "packages\\mssql\\regular" //DBmaestro packages root folder

//Jira - optional - no need to delete anything if you are not using Jira
@Field
def jiraSite = "LOCAL" //from "Configure System" screen, field added by Jira Pipeline Steps plugin

//DryRun
@Field
def dryrunOkLabel = "DryRun-OK"
@Field
def dryrunErrorLabel = "DryRun-Failed"

//QA
@Field
def rsEnvName = "RS" //name of Release Source Environment in your DBmaestro Project
@Field
def qaEnvName = "QA" //name of QA Environment in your DBmaestro Project

//QA Upgrade
@Field
def qaUpgTransitionId = 101 //Deployed in QA
@Field
def qaUpgOkLabel = "In-QA"
@Field
def qaUpgErrorLabel = "Failed-Deploy-QA"

//QA Rollback
@Field
def qaRbOkLabel = "RolledBack-QA"
@Field
def qaRbErrorLabel = "Failed-Rollback-QA"

//PROD
@Field
def prodEnvName = "PROD" //name of Environment in your DBmaestro Project

//PROD Upgrade
@Field
def prodUpgOkLabel = "In-PROD"
@Field
def prodUpgErrorLabel = "Failed-Deploy-PROD"
@Field
def prodUpgTransitionId = 121 //Deployed in PROD

//PROD Upgrade
@Field
def prodRbOkLabel = "RolledBack-PROD"
@Field
def prodRbErrorLabel = "Failed-Rollback-PROD"

return this;
