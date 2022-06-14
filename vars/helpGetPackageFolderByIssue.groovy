def call(String rootFolder, String issueKey){
  helpMsgbox "Getting Package Folder by Issue ${issueKey} from ${rootFolder}"
  packageFolder = powershell(returnStdout: true, script: "\$packageFolder = Get-ChildItem -Path \"${rootFolder}\\${issueKey}_*\" -Force -Directory | Select-Object -First 1;\$packageFolder.Name")
  packageFolder = packageFolder.trim()
  println "Package Folder is '${packageFolder}'"
  return packageFolder
}