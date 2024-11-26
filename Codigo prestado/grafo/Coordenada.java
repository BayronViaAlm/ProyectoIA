package P2.grafo;

public class Coordenada {
    private final double x;
    private final double y;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanciaEuclidiana(Coordenada otra) {
        return Math.sqrt(Math.pow(x - otra.x, 2) + Math.pow(y - otra.y, 2));
    }
}
