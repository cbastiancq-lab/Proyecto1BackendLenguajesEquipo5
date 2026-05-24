$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.8-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "JAVA_HOME: $env:JAVA_HOME"
java -version

cd "C:\Users\sebas\Documents\Lenguajes\Proyecto1BackendLenguajesEquipo5\Proyecto1BackendLenguajesEquipo5"
./mvnw.cmd clean compile
