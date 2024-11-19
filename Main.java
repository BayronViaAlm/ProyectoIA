
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
                        conexionesStr = conexionesStr.replace("<", "").replace(">", ""); // Eliminar los <>
                        parada.conexionesStr = conexionesStr.split(":"); // Separar los nombres de las conexiones
                    }

                    paradas.add(parada);
                } catch (Exception e) {
                    System.out.println("Error procesando la línea: " + line + " -> " + e.getMessage());
                }
            }

            // Asignar conexiones reales
            for (Parada parada : paradas) {
                ArrayList<Pair<Parada, Double>> nuevasConexiones = new ArrayList<>();
                if (parada.conexionesStr != null) {
                    for (String conexionNombre : parada.conexionesStr) {
                        for (Parada destino : paradas) {
                            if (destino.nombre.equals(conexionNombre)) {
                                double distancia = Parada.calcularDistancia(parada, destino);
                                nuevasConexiones.add(new Pair<>(destino, distancia));
                                break;
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
