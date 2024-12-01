#!/bin/bash

# Directorios
SOURCE_DIR="src"
BIN_DIR="src/bin"
LIB_DIR="src/JavaFX/lib"
RESOURCES_DIR="src/resources"
DATA_DIR="src/APP/metroBuenosAires"
MAIN_CLASS="src.APP.algoritmo.Main"
JAR_FILE="app.jar"

# Limpiar el directorio de salida
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

# Crear el manifiesto
echo "Creando el archivo MANIFEST.MF..."
MANIFEST_FILE="$BIN_DIR/META-INF/MANIFEST.MF"
mkdir -p "$BIN_DIR/META-INF"
cat > "$MANIFEST_FILE" <<EOL
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS
Class-Path: $(find "$LIB_DIR" -name "*.jar" | tr '\n' ' ' )
EOL
echo "Manifiesto creado."

# Crear el archivo JAR
echo "Creando el archivo JAR..."
jar cvfm "$JAR_FILE" "$MANIFEST_FILE" -C "$BIN_DIR" .
if [ $? -ne 0 ]; then
    echo "Error durante la creación del JAR."
    exit 1
fi
echo "Archivo JAR creado: $JAR_FILE"

# Listar los archivos generados
echo "Archivos generados:"
ls -l "$JAR_FILE"

# Ejecución (opcional)
# Ejecutando el archivo JAR
echo "Ejecutando el archivo JAR..."
java -Djava.library.path="$LIB_DIR" --module-path "$LIB_DIR" --add-modules javafx.controls,javafx.fxml -jar "$JAR_FILE"
if [ $? -ne 0 ]; then
    echo "Error durante la ejecución del archivo JAR."
    exit 1
fi
echo "Ejecución completada."

