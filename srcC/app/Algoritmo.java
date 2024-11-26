package srcC.app;

import srcC.mapa.Conexion;
import srcC.mapa.Mapa;
import srcC.mapa.Posicion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Algoritmo {
    public static List<String> encontrarRuta(Mapa metro, String origen, String destino) {
        PriorityQueue<Nodo> setAbiertos = new PriorityQueue<>(Comparator.comparing(Nodo::getCostoTotal)); // Cola de prioridad para explorar los nodos con el menor costo estimado
        Set<String> setCerrados = new HashSet<>(); // Nodos ya explorados
        Map<String, Double> gScores = new HashMap<>(); // Costo acumulado desde el origen hasta el nodo actual
        Map<String, String> cameFrom = new HashMap<>(); // Para reconstruir el camino

        // Inicializamos priority queue y gScores
        setAbiertos.add(new Nodo(origen, 0, h(metro, origen, destino)));
        gScores.put(origen, 0.0);

        while(!setAbiertos.isEmpty()) {
            Nodo actual = setAbiertos.poll();
            String nombreActual = actual.getNombre();

            // Si llegamos al destino, reconstruir el camino
            if (nombreActual.equals(destino)) {
                return reconstruirCamino(cameFrom, destino);
            }

            setCerrados.add(nombreActual);

            // Iterar por todas las conexiones de la estación actual
            for (Conexion conexion : metro.getEstacion(nombreActual).getConexiones()) {
                String vecino = conexion.getDestino().getNombre();

                if (setCerrados.contains(vecino)) continue;

                double tentativeGScore = gScores.getOrDefault(nombreActual, Double.MAX_VALUE) + conexion.getDistancia();

                // Si el nuevo costo es mejor, actualizamos los datos
                if (tentativeGScore < gScores.getOrDefault(vecino, Double.MAX_VALUE)) {
                    cameFrom.put(vecino, nombreActual);
                    gScores.put(vecino, tentativeGScore);

                    double fScore = tentativeGScore + h(metro, vecino, destino);

                    // Añadir el vecino a la cola si no está ya en ella
                    setAbiertos.add(new Nodo(vecino, tentativeGScore, fScore));
                }
            }
        }

        // No se encontró un camino, devolvemos lista vacía
        return new ArrayList<>();
    }

    // Heurística: distancia en línea recta entre dos estaciones
    private static double h(Mapa metro, String origen, String destino) {
        Posicion posOrigen = metro.getEstacion(origen).getPosicion();
        Posicion posDestino = metro.getEstacion(destino).getPosicion();
        return posOrigen.distanciaEuclidiana(posDestino);
    }

    // Reconstrucción del camino desde cameFrom
    private static List<String> reconstruirCamino(Map<String, String> cameFrom, String destino) {
        List<String> camino = new ArrayList<>();
        String actual = destino;
        while (actual != null) {
            camino.add(actual);
            actual = cameFrom.get(actual);
        }

        //Invertimos el orden para que el camino vaya de origen a destino
        Collections.reverse(camino);
        return camino;
    }

    // Clase interna para representar un nodo en la cola de prioridad
    private static class Nodo {
        private String nombre;
        private double costoAcumulado; // g(n)
        private double costoTotal; // f(n) = g(n) + h(n)

        public Nodo(String nombre, double costoAcumulado, double costoTotal) {
            this.nombre = nombre;
            this.costoAcumulado = costoAcumulado;
            this.costoTotal = costoTotal;
        }

        public String getNombre() {
            return nombre;
        }

        public double getCostoTotal() {
            return costoTotal;
        }   
    }
}
