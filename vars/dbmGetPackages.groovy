def call(String javaCmd, String projectName, String filePath, String server, String authType, String useSSL, String dbmCredentials){
  helpMsgbox("GetPackages and put them in file named '${filePath}'")
  withCredentials([usernamePassword(credentialsId: dbmCredentials, usernameVariable: 'username', passwordVariable: 'token')]){
    bat "${javaCmd} -GetPackages -ProjectName ${projectName} -FilePath ${filePath} -Server ${server} -AuthType ${authType} -UseSSL ${useSSL}" + ' -UserName %username% -Password %token%'
  }
}