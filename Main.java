import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static ArrayList<Parada> cargarParadas(String fileName) {
        ArrayList<Parada> paradas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 8) {
                    System.out.println("Formato incorrecto: " + line);
                    continue;
                }

                // Parsear atributos
                String linea = parts[0];
                String nombre = parts[1];
                double posX = Double.parseDouble(parts[2]);
                double posY = Double.parseDouble(parts[3]);
                String conexionesStr = parts[5];
                boolean bano = Boolean.parseBoolean(parts[6]);
                boolean minusvalido = Boolean.parseBoolean(parts[7]);
                boolean atencionPasajero = Boolean.parseBoolean(parts[8]);

                // Crear parada
                Parada parada = new Parada(nombre, linea, posX, posY, new ArrayList<>(), bano, minusvalido, atencionPasajero);
                paradas.add(parada);

                // Procesar conexiones iniciales como cadenas
                if (conexionesStr != null && !conexionesStr.isEmpty()) {
                    String[] conexiones = conexionesStr.split(":");
                    for (String conexion : conexiones) {
                        try {
                            String[] conexionParts = conexion.replace("<", "").replace(">", "").split(",");
                            String destino = conexionParts[0];
                            double distancia = Double.parseDouble(conexionParts[1]);
                            parada.conexiones.add(new Pair<>(new Parada(destino, "", 0, 0, null, false, false, false), distancia));
                        } catch (Exception e) {
                            System.err.println("Error procesando conexión: " + conexion);
                        }
                    }
                }
            }

            // Asignar conexiones reales
            for (Parada parada : paradas) {
                ArrayList<Pair<Parada, Double>> nuevasConexiones = new ArrayList<>();
                for (Pair<Parada, Double> conexion : parada.conexiones) {
                    for (Parada destino : paradas) {
                        if (conexion.getSigParada().nombre.equals(destino.nombre)) {
                            nuevasConexiones.add(new Pair<>(destino, conexion.getDistancia()));
                            break;
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
                System.out.println("  -> " + conexion.getSigParada().nombre + " (" + conexion.getDistancia() + " km)");
            }
        }

        return paradas;
    }
}
