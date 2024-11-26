package P2.com_algoritmo;

import java.util.List;
import P2.grafo.GrafoEstaciones;
import P2.grafo.Coordenada;

public class Main {
    public static void main(String[] args) {
        GrafoEstaciones grafo = new GrafoEstaciones();
        grafo.agregarEstacion("A", new Coordenada(0, 0));
        grafo.agregarEstacion("B", new Coordenada(3, 4));
        grafo.agregarEstacion("C", new Coordenada(6, 8));
        grafo.agregarConexion("A", "B", 5, "red");
        grafo.agregarConexion("B", "C", 7, "blue");

        AlgoritmoAEstrella aEstrella = new AlgoritmoAEstrella();
        List<String> camino = aEstrella.calcularCamino(grafo, "A", "C");

        System.out.println("Camino: " + camino);
    }
}