import groovy.transform.Field

//DBmaestro DOP
@Field
def projectName = "MsSqlByTask" //“DBmaestro DOP” Project Name. Default Value for this Lab: MsSqlByTask
@Field
def server = "10.0.1.111:8017" //IP/Hostname and Port of “DBmaestro DOP“ Agent to connect to. In this case we’ll connect to the default one installed in the “DBmaestro DOP” Server. Default port is 8017. Value example: 10.0.1.111:8017
@Field
def dbmCredentials = "dbmaestro-user-automation-token" //Jenkins Credentials ID for “DBmaestro DOP” User to authenticate with. Default value for this Lab: dbmaestro-user-automation-token
@Field
def authType = "DBmaestroAccount" //Authentication type. It can use “DBmaestro DOP“ Account authentication or LDAP/AD authentication. This time we’ll use a value of “DBmaestroAccount”
@Field
def javaCmd = "java -jar \"C:\\Program Files (x86)\\DBmaestro\\DOP Server\\Agent\\DBmaestroAgent.jar\"" //Java command to run the DBmaestroAgent.jar file. Default Value for this Lab contains the default “DBmaestro DOP” installation path to the DBmaestroAgent.jar file. 
@Field
def packageType = "Regular" //You can create Regular and Ad-hoc packages. We’ll use Regular packages
@Field
def useSSL = "n" //use SSL to communicate with DBmaestro?
@Field
def createScriptsOnly = "False" //If you want to create a rollback package and see its scripts before executing it, you can set this as True, we'll use False
@Field
def getPackagesFilePath = "GetPackagesOutput.json"
//DBmaestro DOP Environments
@Field
def rsEnvName = "RS" //name of Release Source Environment in your DBmaestro Project
@Field
def qaEnvName = "QA" //name of QA Environment in your DBmaestro Project
@Field
def prodEnvName = "PROD" //name of Production Environment in your DBmaestro Project

//Git
@Field
def rootFolder = "packages\\mssql\\regular" //DBmaestro DOP packages root folder

//Jira - optional - no need to delete anything if you are not using Jira
@Field
def jiraSite = "LOCAL" //from "Configure System" screen, field added by Jira Pipeline Steps plugin
//Action Labels
//PreCheck
@Field
def precheckOkLabel = "PreCheck-OK" //Jira Issue Label for when PreCheck is successful
@Field
def precheckErrorLabel = "PreCheck-Failed" //Jira Issue Label for when PreCheck fails
//QA Upgrade
@Field
def qaUpgOkLabel = "In-QA" //Jira Issue Label for when QA Upgrade is successful
@Field
def qaUpgErrorLabel = "Failed-Deploy-QA" //Jira Issue Label for when QA Upgrade fails
//QA Rollback
@Field
def qaRbOkLabel = "RolledBack-QA" //Jira Issue Label for when QA Rollback is successful
@Field
def qaRbErrorLabel = "Failed-Rollback-QA" //Jira Issue Label for when QA Rollback fails
//PROD Upgrade
@Field
def prodUpgOkLabel = "In-PROD" //Jira Issue Label for when Production Upgrade is successful
@Field
def prodUpgErrorLabel = "Failed-Deploy-PROD" //Jira Issue Label for when Production Upgrade fails
//PROD Upgrade
@Field
def prodRbOkLabel = "RolledBack-PROD" //Jira Issue Label for when Production Rollback is successful
@Field
def prodRbErrorLabel = "Failed-Rollback-PROD" //Jira Issue Label for when Production Rollback fails

//Transition IDs
@Field
def qaUpgTransitionId = 101 //Transition ID to move Jira Issue to “In QA” status
@Field
def prodUpgTransitionId = 121 //Transition ID to move Jira Issue to “In Prod” status

return this;
