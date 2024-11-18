import java.util.*;

class AStar {

    // Método para encontrar el camino óptimo
    public static List<Parada> encontrarCamino(Parada inicio, Parada destino, double velocidad) {
        PriorityQueue<Parada> openSet = new PriorityQueue<>(Comparator.comparingDouble(p -> p.fScore));
        Set<Parada> closedSet = new HashSet<>();

        // Inicialización
        inicio.gScore = 0; // Costo desde el inicio
        inicio.fScore = heuristica(inicio, destino); // f(n) = h(n) inicialmente
        openSet.add(inicio);

        Map<Parada, Parada> cameFrom = new HashMap<>(); // Rastro para reconstruir el camino

        while (!openSet.isEmpty()) {
            Parada actual = openSet.poll();

            // Si llegamos al destino, reconstruir el camino
            if (actual.equals(destino)) {
                return reconstruirCamino(cameFrom, actual);
            }

            closedSet.add(actual);

            // Evaluar las conexiones del nodo actual
            for (Pair<Parada, Double> conexion : actual.conexiones) {
                Parada vecino = conexion.getSigParada();
                double distancia = conexion.getDistancia();
                double tentativeGScore = actual.gScore + Parada.calcularTiempo(distancia, velocidad);

                // Si ya se evaluó este vecino, ignorarlo
                if (closedSet.contains(vecino) && tentativeGScore >= vecino.gScore) {
                    continue;
                }

                // Si no está en openSet o encontramos un mejor camino
                if (!openSet.contains(vecino) || tentativeGScore < vecino.gScore) {
                    cameFrom.put(vecino, actual);
                    vecino.gScore = tentativeGScore;
                    vecino.fScore = vecino.gScore + heuristica(vecino, destino);

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
        return Math.sqrt(Math.pow(p2.posX - p1.posX, 2) + Math.pow(p2.posY - p1.posY, 2));
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
