package srcB;
import java.util.Objects;

class ParadaB {
    String nombre;
    String linea;
    int posX;
    int posY;
    int n_conexiones;
    String[] conexiones;
    boolean hasServicios;
    boolean hasMinusvalia;
    boolean hasAtencion;

    // A* variables
    double gScore = Double.MAX_VALUE; // Costo desde el inicio
    double fScore = Double.MAX_VALUE; // Estimación de costo total
    public String[] conexionesStr;

    // Constructor
    public ParadaB(String nombre, String linea, int posX, int posY, int n_conexiones,
                  String[] conexiones,
                  boolean hasServicios, boolean hasMinusvalia, boolean hasAtencion) {
        this.nombre = nombre;
        this.linea = linea;
        this.posX = posX;
        this.posY = posY;
        this.conexiones = conexiones;
        this.hasServicios = hasServicios;
        this.hasMinusvalia = hasMinusvalia;
        this.hasAtencion = hasAtencion;
    }

    // Método para calcular la distancia entre dos paradas
    public static double calcularDistancia(ParadaB p1, ParadaB p2) {
        return Math.sqrt(Math.pow(p2.posX - p1.posX, 2) + Math.pow(p2.posY - p1.posY, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParadaB)) return false;
        ParadaB parada = (ParadaB) o;
        return nombre.equals(parada.nombre) && linea.equals(parada.linea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, linea);
    }
}

