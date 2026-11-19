# Compila todo el proyecto y abre el menú de la UT2 (Windows, PowerShell).
# Uso, desde esta carpeta:   .\ejecutar.ps1
# Si PowerShell no deja ejecutar scripts:  powershell -ExecutionPolicy Bypass -File .\ejecutar.ps1

# 1) Buscar un JDK moderno. Primero JAVA_HOME; si no, el javac del PATH.
#    No se usa "java" del PATH a ciegas: en muchos equipos apunta a un Java 8 antiguo.
if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
    $bin = "$env:JAVA_HOME\bin"
} else {
    $bin = Split-Path (Get-Command javac -ErrorAction Stop).Source
}
Write-Host "Usando el JDK de: $bin"

# 2) Compilar todas las fuentes de src/ en out/ (UTF-8 por las tildes)
$fuentes = (Get-ChildItem -Recurse src -Filter *.java).FullName
& "$bin\javac.exe" -encoding UTF-8 -d out $fuentes
if ($LASTEXITCODE -ne 0) { Write-Host "Error de compilación"; exit 1 }

# 3) Arrancar el menú con el MISMO JDK con el que se ha compilado
& "$bin\java.exe" "-Dstdout.encoding=UTF-8" -cp out menu.Menu
