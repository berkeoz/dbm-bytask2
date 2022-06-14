def call(String javaCmd, String projectName, String packageName, String server, String authType, String useSSL, String dbmCredentials)
{
    helpMsgbox("Performing PreCheck on Package ${packageName}")
    withCredentials([usernamePassword(credentialsId: dbmCredentials, usernameVariable: 'username', passwordVariable: 'token')]){
        bat "${javaCmd} -PreCheck -ProjectName ${projectName} -PackageName ${packageName} -Server ${server} -AuthType ${authType} -UseSSL ${useSSL}" + ' -UserName %username% -Password %token%'
    }
}