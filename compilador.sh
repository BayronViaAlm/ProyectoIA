#!/bin/bash
# Crear el directorio bin si no existe
mkdir -p srcC/bin

# Compilar los archivos
javac -encoding UTF-8 -d srcC/bin -sourcepath srcC srcC/**/*.java

# Ejecutar el programa
java -cp srcC/bin srcC.app.Main
