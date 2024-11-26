package srcC.mapa;

public class Conexion {
    private Estacion origen;
    private Estacion destino;
    private boolean cambioLinea;
    private int distancia;

    public Conexion(Estacion origen, Estacion destino, boolean cambioLinea, int distancia) {
        this.origen = origen;
        this.destino = destino;
        this.cambioLinea = cambioLinea;
        this.distancia = distancia;
    }

    public Estacion getOrigen() {
        return origen;
    }

    public Estacion getDestino() {
        return destino;
    }

    public boolean esCambioLinea() {
        return cambioLinea;
    }

    public int getDistancia() {
        return distancia;
    }
}
