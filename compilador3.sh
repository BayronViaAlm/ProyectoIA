#!/bin/bash

# Ruta a la biblioteca de JavaFX
JAVA_FX_PATH="srcC/lib"

# Compilar los archivos
javac -d srcC/bin --module-path $JAVA_FX_PATH --add-modules javafx.controls,javafx.fxml srcC/APP/*.java TestFX.java

# Verificar si la compilación fue exitosa
if [ $? -eq 0 ]; then
    echo "Compilación exitosa. Archivos compilados en srcC/bin"
    echo "Contenido del directorio bin:"
    find srcC/bin -type f

    # Ejecutar el programa con renderizado por software
    java --module-path $JAVA_FX_PATH --add-modules javafx.controls,javafx.fxml -Dprism.order=sw -cp srcC/bin TestFX
else
    echo "Error durante la compilación."
fi