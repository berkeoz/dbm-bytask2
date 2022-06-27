def call(String packageName, String packagesFilePath){
  def pck = null
  def found = false
  helpMsgbox("Is Package '${packageName}' in '${packagesFilePath}'?");
  def packages = readJSON(file: packagesFilePath);
  (packages).find{
    if(it.Name==packageName || it.Name.startsWith(packageName + " (Rev. ")){
      pck = it
      return true
    }
    else{
      return false
    }
  }
  if(pck){
    found = true
  }
  return found
}