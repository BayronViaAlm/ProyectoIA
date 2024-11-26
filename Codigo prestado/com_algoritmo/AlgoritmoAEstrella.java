package P2.com_algoritmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import P2.grafo.Arista;
import P2.grafo.Coordenada;
import P2.grafo.GrafoEstaciones;

public class AlgoritmoAEstrella {
    public List<String> calcularCamino(GrafoEstaciones grafo, String inicio, String objetivo) {
        // Cola de prioridad para explorar los nodos con el menor costo estimado
        PriorityQueue<NodoAEstrella> abiertos = new PriorityQueue<>(Comparator.comparingDouble(NodoAEstrella::getCostoTotal));
        Map<String, Double> gScores = new HashMap<>(); // Costos desde el inicio hasta cada nodo
        Map<String, String> cameFrom = new HashMap<>(); // Para reconstruir el camino
        Set<String> cerrados = new HashSet<>();

        // Inicializar la cola y las puntuaciones
        gScores.put(inicio, 0.0);
        abiertos.add(new NodoAEstrella(inicio, 0.0, heuristica(grafo, inicio, objetivo)));

        while (!abiertos.isEmpty()) {
            NodoAEstrella actual = abiertos.poll();
            String nodoActual = actual.getNombre();

            // Si llegamos al objetivo, reconstruimos el camino
            if (nodoActual.equals(objetivo)) {
                return reconstruirCamino(cameFrom, objetivo);
            }

            cerrados.add(nodoActual);

            // Iterar por todos los vecinos
            for (Arista arista : grafo.getConexiones(nodoActual)) {
                String vecino = arista.getDestino();

                if (cerrados.contains(vecino)) continue;

                double tentativeGScore = gScores.getOrDefault(nodoActual, Double.MAX_VALUE) + arista.getCosto();

                // Si el nuevo costo es mejor, actualizamos los datos
                if (tentativeGScore < gScores.getOrDefault(vecino, Double.MAX_VALUE)) {
                    cameFrom.put(vecino, nodoActual);
                    gScores.put(vecino, tentativeGScore);

                    double fScore = tentativeGScore + heuristica(grafo, vecino, objetivo);

                    // Añadir el vecino a la cola si no está ya en ella
                    abiertos.add(new NodoAEstrella(vecino, tentativeGScore, fScore));
                }
            }
        }

        // Si no se encuentra el camino, devolvemos una lista vacía
        return new ArrayList<>();
    }

    // Heurística basada en distancia euclidiana
    private double heuristica(GrafoEstaciones grafo, String nodo, String objetivo) {
        Coordenada coord1 = grafo.getEstacion(nodo).getCoordenada();
        Coordenada coord2 = grafo.getEstacion(objetivo).getCoordenada();
        return coord1.distanciaEuclidiana(coord2);
    }

    // Reconstruir el camino desde el objetivo al inicio usando `cameFrom`
    private List<String> reconstruirCamino(Map<String, String> cameFrom, String objetivo) {
        List<String> camino = new ArrayList<>();
        String actual = objetivo;

        while (actual != null) {
            camino.add(actual);
            actual = cameFrom.get(actual);
        }

        // Invertir el camino para que vaya del inicio al objetivo
        Collections.reverse(camino);
        return camino;
    }

    // Clase interna para representar un nodo en la cola de prioridad
    private static class NodoAEstrella {
        private final String nombre;
        private final double costoAcumulado; // g(n)
        private final double costoTotal;    // f(n) = g(n) + h(n)

        public NodoAEstrella(String nombre, double costoAcumulado, double costoTotal) {
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
