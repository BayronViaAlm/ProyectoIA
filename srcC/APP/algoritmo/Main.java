package srcC.APP.algoritmo;

import srcC.APP.algoritmo.Main;
import srcC.APP.mapa.Estacion;
import srcC.APP.mapa.Mapa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/srcC/resources/interfaz.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Metro Buenos Aires");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Mapa cargarEstaciones(String fileName) {
        Mapa mapa = new Mapa();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            for (String line : lineas) {
                String[] parts = line.split(";");
                try {
                    String linea = parts[0];
                    String nombre = parts[1];
                    boolean sBanho = parts[2].equals("1");
                    boolean sAtencion = parts[3].equals("1");
                    boolean sMinusvalido = parts[4].equals("1");
                    int posX = Integer.parseInt(parts[5]);
                    int posY = Integer.parseInt(parts[6]);
                    mapa.agregarEstacion(linea, nombre, sBanho, sAtencion, sMinusvalido, posX, posY);
                } catch (Exception e) {
                    System.out.println("Error procesando la línea: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las paradas: " + e.getMessage());
        }
        return mapa;
    }

    public static void cargarConexiones(String fileName, Mapa mapa) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            for (String line : lineas) {
                String[] parts = line.split(";");
                try {
                    String lineaO = parts[0];
                    String nombreO = parts[1];
                    Estacion origen = mapa.getEstacion(nombreO);
                    if (origen == null) {
                        System.out.println("Estación de origen no encontrada: " + nombreO);
                        continue;
                    }
                    for (int i = 2; i < parts.length; i++) {
                        String[] conexionParts = parts[i].split(":");
                        String nombreD = conexionParts[0];
                        String lineaD = conexionParts[1];
                        int distancia = Integer.parseInt(conexionParts[2]);
                        boolean cambioLinea = !lineaO.equals(lineaD);
                        Estacion destino = mapa.getEstacion(nombreD);
                        if (destino == null) {
                            System.out.println("Estación de destino no encontrada: " + nombreD);
                            continue;
                        }
                        mapa.agregarConexion(nombreO, nombreD, cambioLinea, distancia);
                    }
                } catch (Exception e) {
                    System.out.println("Error procesando la línea: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las conexiones: " + e.getMessage());
        }
    }
}