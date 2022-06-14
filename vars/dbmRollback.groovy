def call(String javaCmd, String projectName, String envName, String packageName, String createScriptsOnly, String server, String authType, String useSSL, String dbmCredentials)
{
    helpMsgbox("Rollback ${packageName} from ${envName}")
    withCredentials([usernamePassword(credentialsId: dbmCredentials, usernameVariable: 'username', passwordVariable: 'token')]){
        bat "${javaCmd} -Rollback -ProjectName ${projectName} -EnvName \"${envName}\" -PackageName ${packageName} -CreateScriptsOnly ${createScriptsOnly} -Server ${server} -AuthType ${authType} -UseSSL ${useSSL}" + ' -UserName %username% -Password %token%'
    }
}