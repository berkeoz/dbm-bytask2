def call(String javaCmd, String projectName, String envName, String packageName, String server, String authType, String useSSL, String dbmCredentials)
{
    helpMsgbox("Upgrading ${envName} to ${packageName}")
    withCredentials([usernamePassword(credentialsId: dbmCredentials, usernameVariable: 'username', passwordVariable: 'token')]){
        bat "${javaCmd} -Upgrade -ProjectName ${projectName} -EnvName \"${envName}\" -PackageName ${packageName} -Server ${server} -AuthType ${authType} -UseSSL ${useSSL}" + ' -UserName %username% -Password %token%'
    }
}