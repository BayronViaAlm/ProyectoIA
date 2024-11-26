package src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static ArrayList<Parada> cargarParadas(String fileName) {
        ArrayList<Parada> paradas = new ArrayList<>();

        try {
            // Leer todas las líneas del archivo usando UTF-8
            List<String> lineas = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

            for (String line : lineas) {
                String[] parts = line.split(";");

                try {
                    // Parsear atributos
                    String linea = parts[0];
                    String nombre = parts[1];
                    double posX = Double.parseDouble(parts[2]);
                    double posY = Double.parseDouble(parts[3]);
                    int numConexiones = Integer.parseInt(parts[4]);
                    String conexionesStr = parts[5];
                    boolean bano = Boolean.parseBoolean(parts[6]);
                    boolean minusvalido = Boolean.parseBoolean(parts[7]);
                    boolean atencionPasajero = Boolean.parseBoolean(parts[8]);

                    // Crear parada
                    Parada parada = new Parada(nombre, linea, posX, posY, new ArrayList<>(), bano, minusvalido, atencionPasajero);

                    // Procesar conexiones
                    if (numConexiones > 0 && conexionesStr != null && !conexionesStr.isEmpty()) {
                        conexionesStr = conexionesStr.replace("<", "").replace(">", ""); // Eliminar los < >
                        parada.conexionesStr = conexionesStr.split(":"); // Separar las conexiones por ':'
                    }

                    paradas.add(parada);
                } catch (Exception e) {
                    System.out.println("Error procesando la línea: " + line + " -> " + e.getMessage());
                }
            }

            // Asignar conexiones reales con distancias
            for (Parada parada : paradas) {
                ArrayList<Pair<Parada, Double>> nuevasConexiones = new ArrayList<>();
                if (parada.conexionesStr != null) {
                    for (String conexion : parada.conexionesStr) {
                        String[] conexionParts = conexion.split(",");
                        if (conexionParts.length == 2) {
                            String conexionNombre = conexionParts[0];
                            double distancia = Double.parseDouble(conexionParts[1]);

                            // Buscar la parada destino
                            for (Parada destino : paradas) {
                                if (destino.nombre.equals(conexionNombre)) {
                                    nuevasConexiones.add(new Pair<>(destino, distancia));
                                    break;
                                }
                            }
                        }
                    }
                }
                parada.conexiones = nuevasConexiones;
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error procesando las paradas: " + e.getMessage());
        }

        // Depuración: Imprimir todas las paradas y sus conexiones
        for (Parada parada : paradas) {
            System.out.println("Parada: " + parada.nombre + ", Línea: " + parada.linea + ", Conexiones:");
            for (Pair<Parada, Double> conexion : parada.conexiones) {
                System.out.println("  -> " + conexion.getSigParada().nombre + " (" + conexion.getDistancia() + " m)");
            }
        }

        return paradas;
    }
}
