package srcC.APP.algoritmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import srcC.APP.mapa.Conexion;
import srcC.APP.mapa.Mapa;
import srcC.APP.mapa.Posicion;

public class Algoritmo {

    // Método principal que encuentra la ruta más corta usando una variante del algoritmo A*
    public static List<String> encontrarRuta(Mapa metro, String origen, String destino) {
        // Cola de prioridad que prioriza nodos con menor costo total (f(n)).
        PriorityQueue<Nodo> setAbiertos = new PriorityQueue<>(Comparator.comparing(Nodo::getCostoTotal));
        Set<String> setCerrados = new HashSet<>(); // Nodos ya explorados.
        Map<String, Double> gScores = new HashMap<>(); // g(n): costo acumulado desde el origen al nodo actual.
        Map<String, String> cameFrom = new HashMap<>(); // Para rastrear el camino óptimo.

        // Inicialización: el nodo inicial tiene g(n)=0 y su f(n) depende únicamente de h(n).
        setAbiertos.add(new Nodo(origen, 0, h(metro, origen, destino)));
        gScores.put(origen, 0.0);

        // Bucle principal: explora los nodos hasta encontrar el destino o agotar posibilidades.
        while (!setAbiertos.isEmpty()) {
            Nodo actual = setAbiertos.poll(); // Nodo con menor f(n).
            String nombreActual = actual.getNombre();

            // Si alcanzamos el destino, reconstruimos el camino desde el origen.
            if (nombreActual.equals(destino)) {
                return reconstruirCamino(cameFrom, destino);
            }

            setCerrados.add(nombreActual); // Marcar nodo como explorado.

            // Explorar los vecinos del nodo actual.
            for (Conexion conexion : metro.getEstacion(nombreActual).getConexiones()) {
                String vecino = conexion.getDestino().getNombre();

                // Ignorar vecinos ya explorados.
                if (setCerrados.contains(vecino)) continue;

                // Calcular g(n) provisional para el vecino.
                double tentativeGScore = gScores.getOrDefault(nombreActual, Double.MAX_VALUE) + conexion.getDistancia();

                // Si encontramos un mejor camino al vecino, actualizamos.
                if (tentativeGScore < gScores.getOrDefault(vecino, Double.MAX_VALUE)) {
                    cameFrom.put(vecino, nombreActual); // Registrar el nodo previo.
                    gScores.put(vecino, tentativeGScore); // Actualizar g(n).

                    // Calcular f(n) = g(n) + h(n) para el vecino.
                    double fScore = tentativeGScore + h(metro, vecino, destino);

                    // Añadir el vecino a la cola de prioridad.
                    setAbiertos.add(new Nodo(vecino, tentativeGScore, fScore));
                }
            }
        }

        // Si no se encuentra camino, devolver una lista vacía.
        return new ArrayList<>();
    }

    // Función heurística h(n): distancia en línea recta entre dos estaciones.
    private static double h(Mapa metro, String origen, String destino) {
        Posicion posOrigen = metro.getEstacion(origen).getPosicion();
        Posicion posDestino = metro.getEstacion(destino).getPosicion();
        return posOrigen.distanciaEuclidiana(posDestino);
    }

    // Método para reconstruir el camino más corto desde el mapa de cameFrom.
    private static List<String> reconstruirCamino(Map<String, String> cameFrom, String destino) {
        List<String> camino = new ArrayList<>();
        String actual = destino;

        // Seguir los nodos desde el destino hasta el origen.
        while (actual != null) {
            camino.add(actual);
            actual = cameFrom.get(actual);
        }

        // Invertir el orden del camino para que sea origen -> destino.
        Collections.reverse(camino);
        return camino;
    }

    // Clase interna para representar un nodo en la cola de prioridad.
    private static class Nodo {
        private String nombre; // Nombre de la estación.
        private double costoAcumulado; // g(n): costo acumulado desde el origen.
        private double costoTotal; // f(n) = g(n) + h(n): costo total estimado.

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
