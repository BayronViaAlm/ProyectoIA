package srcC.app;

import srcC.mapa.Estacion;
import srcC.mapa.Mapa;
import srcC.app.Main;
import srcC.mapa.Camino;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        // Configurar System.out para que use UTF-8
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        Mapa metroBuenosAires = cargarEstaciones("srcC/metroBuenosAires/estaciones.txt");
        cargarConexiones("srcC/metroBuenosAires/conexiones.txt", metroBuenosAires);
        System.out.println("Estaciones cargadas: " + metroBuenosAires.getEstaciones().size());
        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            System.out.println(estacion.getNombre() + " -> " + estacion.getConexiones().size());
        }
        String origen;
        do {
            System.out.println("Inserte el nombre de la estación de origen: ");
            origen = System.console().readLine();
        } while (metroBuenosAires.getEstacion(origen) == null);

        String destino;
        do {
            System.out.println("Inserte el nombre de la estación de destino: ");
            destino = System.console().readLine();
        } while (metroBuenosAires.getEstacion(destino) == null);

        List<String> camino = Algoritmo.encontrarRuta(metroBuenosAires, origen, destino);
        if (camino == null) {
            System.out.println("No se encontró una ruta entre " + origen + " y " + destino);
        } else {
            System.out.println("Ruta encontrada: " + camino);
        }
        Camino ruta = Camino.crearCamino(camino, metroBuenosAires);
        int horas = (int) ruta.getTiempoTotal()/60;
        int minutos = (int) (ruta.getTiempoTotal() % 60);
        System.out.println("Tiempo total: " + horas + "hh:" + minutos + "mm");
        System.out.println("Distancia total: " + (double) ruta.getDistanciaTotal()/1000 + " kilometros");
        if(ruta.getnIntercambios() == 0) {
            System.out.println("No se requieren intercambios");
        } else {
            System.out.println("Intercambios requeridos: " + ruta.getnIntercambios());
        }
    }

    private static Mapa cargarEstaciones(String fileName) {
        Mapa mapa = new Mapa();
        try {
            // Leer todas las líneas del archivo usando UTF-8
            List<String> lineas = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            for (String line : lineas) {
                String[] parts = line.split(";");
                try {
                    // Parsear atributos
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
        } catch (Exception e) {
            // TODO: handle exception
        }
        return mapa;
    }

    private static void cargarConexiones(String fileName, Mapa mapa) {
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
                        boolean cambioLinea = lineaO.equals(lineaD) ? false : true;
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
        } catch (Exception e) {

        }
    }
}