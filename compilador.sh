#!/bin/bash

# Directorios
SOURCE_DIR="src"
BIN_DIR="src/bin"
LIB_DIR="src/JavaFX/lib"
RESOURCES_DIR="src/resources"
DATA_DIR="src/APP/metroBuenosAires"
MAIN_CLASS="src.APP.algoritmo.Main"

# Limpiar y crear el directorio de salida
echo "Limpiando el directorio de salida..."
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

# Copiar recursos al directorio de salida
echo "Copiando recursos..."
cp -r "$RESOURCES_DIR" "$BIN_DIR"
cp -r "$DATA_DIR" "$BIN_DIR"

# Verificar que los recursos se copiaron correctamente
if [ $? -ne 0 ]; then
    echo "Error al copiar los recursos."
    exit 1
fi
echo "Recursos copiados correctamente."

# Listar archivos copiados para verificación
echo "Archivos en $BIN_DIR después de copiar recursos:"
ls -R "$BIN_DIR"

# Verificar que los archivos JAR de JavaFX existen
echo "Verificando archivos JAR de JavaFX..."
if [ ! -d "$LIB_DIR" ]; then
    echo "Error: El directorio $LIB_DIR no existe."
    exit 1
fi

JAR_FILES=$(ls "$LIB_DIR"/*.jar 2> /dev/null)
if [ -z "$JAR_FILES" ]; then
    echo "Error: No se encontraron archivos JAR de JavaFX en $LIB_DIR."
    exit 1
fi
echo "Archivos JAR de JavaFX encontrados: $JAR_FILES"

# Compilación
echo "Compilando el proyecto..."
javac -d "$BIN_DIR" --module-path "$LIB_DIR" --add-modules javafx.controls,javafx.fxml $(find "$SOURCE_DIR" -name "*.java")
if [ $? -ne 0 ]; then
    echo "Error durante la compilación."
    exit 1
fi
echo "Compilación completada."

# Verificar que los archivos compilados existen
echo "Verificando archivos compilados..."
if [ ! -f "$BIN_DIR/src/APP/algoritmo/Main.class" ]; then
    echo "Error: Main.class no encontrado."
    exit 1
fi
echo "Archivos compilados verificados."

# Listar archivos compilados para verificación
echo "Archivos en $BIN_DIR después de la compilación:"
ls -R "$BIN_DIR"

# Verificar la ubicación del archivo FXML
echo "Verificando la ubicación del archivo FXML..."
if [ ! -f "$BIN_DIR/resources/interfaz.fxml" ]; then
    echo "Error: El archivo interfaz.fxml no se encuentra en $BIN_DIR/resources."
    exit 1
fi
echo "Archivo FXML encontrado en $BIN_DIR/resources."

# Verificar la ubicación de los archivos de datos
echo "Verificando la ubicación de los archivos de datos..."
if [ ! -f "$BIN_DIR/metroBuenosAires/estaciones.txt" ]; then
    echo "Error: El archivo estaciones.txt no se encuentra en $BIN_DIR/metroBuenosAires."
    exit 1
fi
if [ ! -f "$BIN_DIR/metroBuenosAires/conexiones.txt" ]; then
    echo "Error: El archivo conexiones.txt no se encuentra en $BIN_DIR/metroBuenosAires."
    exit 1
fi
echo "Archivos de datos encontrados en $BIN_DIR/metroBuenosAires."

# Ejecución
echo "Ejecutando el proyecto..."
java -Dprism.verbose=true -Dprism.order=sw -Dprism.debug=true -Djava.library.path="$LIB_DIR" --module-path "$LIB_DIR" --add-modules javafx.controls,javafx.fxml -cp "$BIN_DIR" $MAIN_CLASS
if [ $? -ne 0 ]; then
    echo "Error durante la ejecución."
    exit 1
fi
echo "Ejecución completada."