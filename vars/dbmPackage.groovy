def call(String javaCmd, String projectName, String rootFolder, String packageFolder, String server, String authType, String useSSL, String dbmCredentials){
  def filePath = "${rootFolder}\\${packageFolder}.zip"
  helpMsgbox("Packaging Files for ${filePath}")
  withCredentials([usernamePassword(credentialsId: dbmCredentials, usernameVariable: 'username', passwordVariable: 'token')]){
    bat "${javaCmd} -Package -ProjectName ${projectName} -FilePath ${filePath} -Server ${server} -AuthType ${authType} -UseSSL ${useSSL}" + ' -UserName %username% -Password %token%'
  }
}