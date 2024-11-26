package src;
import java.util.*;

public class AStar {

    // Método para encontrar el camino óptimo entre dos paradas
    public static List<Parada> encontrarCamino(Parada inicio, Parada destino, double velocidad) {
        PriorityQueue<Parada> openSet = new PriorityQueue<>(Comparator.comparingDouble(p -> p.fScore));
        Set<Parada> closedSet = new HashSet<>();

        // Inicialización
        inicio.gScore = 0; // Costo desde el inicio hasta la parada actual
        inicio.fScore = heuristica(inicio, destino); // f(n) = g(n) + h(n)
        openSet.add(inicio);

        Map<Parada, Parada> cameFrom = new HashMap<>(); // Rastro para reconstruir el camino

        while (!openSet.isEmpty()) {
            Parada actual = openSet.poll();

            // Si llegamos al destino, reconstruir el camino
            if (actual.equals(destino)) {
                return reconstruirCamino(cameFrom, actual);
            }

            closedSet.add(actual);

            // Evaluar todas las conexiones del nodo actual
            for (Pair<Parada, Double> conexion : actual.conexiones) {
                Parada vecino = conexion.getSigParada();
                double distancia = conexion.getDistancia();
                double tentativeGScore = actual.gScore + Parada.calcularTiempo(distancia, velocidad);

                // Ignorar si el vecino ya está evaluado y no es un mejor camino
                if (closedSet.contains(vecino) && tentativeGScore >= vecino.gScore) {
                    continue;
                }

                // Si no está en openSet o encontramos un mejor camino
                if (!openSet.contains(vecino) || tentativeGScore < vecino.gScore) {
                    cameFrom.put(vecino, actual); // Registrar el mejor camino hacia este vecino
                    vecino.gScore = tentativeGScore;
                    vecino.fScore = vecino.gScore + heuristica(vecino, destino); // f(n) = g(n) + h(n)

                    if (!openSet.contains(vecino)) {
                        openSet.add(vecino);
                    }
                }
            }
        }

        return null; // No se encontró camino
    }

    // Heurística: Distancia euclidiana entre dos paradas
    private static double heuristica(Parada p1, Parada p2) {
        return Parada.calcularDistancia(p1, p2);
    }

    // Reconstrucción del camino desde cameFrom
    private static List<Parada> reconstruirCamino(Map<Parada, Parada> cameFrom, Parada actual) {
        List<Parada> camino = new ArrayList<>();
        while (actual != null) {
            camino.add(actual);
            actual = cameFrom.get(actual);
        }
        Collections.reverse(camino);
        return camino;
    }
}
