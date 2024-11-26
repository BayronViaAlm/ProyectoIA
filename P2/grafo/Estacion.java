package P2.grafo;

public class Estacion {
    private final String nombre;
    private final Coordenada coordenada;

    public Estacion(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
    }

    public String getNombre() {
        return nombre;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }
}