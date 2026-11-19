#!/bin/sh
# Compila todo el proyecto y abre el menú de la UT2 (Linux / macOS).
# Uso, desde esta carpeta:   sh ejecutar.sh
set -e
BIN="${JAVA_HOME:+$JAVA_HOME/bin/}"          # si hay JAVA_HOME se usa su JDK; si no, el del PATH
"${BIN}javac" -encoding UTF-8 -d out $(find src -name '*.java')
"${BIN}java" -cp out menu.Menu
