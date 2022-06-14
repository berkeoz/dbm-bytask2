def call(String javaCmd, String rootFolder, String packageFolder, String packageType){
  def pathToScriptsFolder = "${rootFolder}\\${packageFolder}"
  helpMsgbox("Creating package.json manifest file for ${pathToScriptsFolder}")
  def cmd = "${javaCmd} -CreateManifestFile -PathToScriptsFolder ${pathToScriptsFolder} -Operation CreateOrUpdate"
  //using PackageType and ScriptsOrderScope parameters
  cmd = cmd + " -PackageType ${packageType} -ScriptsOrderScope Global"
  bat "${cmd}"
}