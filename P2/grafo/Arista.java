package P2.grafo;

public class Arista {
    private final String origen;
    private final String destino;
    private final double costo;
    private final String color;

    public Arista(String origen, String destino, double costo, String color) {
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.color = color;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public double getCosto() {
        return costo;
    }

    public String getColor() {
        return color;
    }
}