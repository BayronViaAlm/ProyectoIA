package ProyectoIA;
import java.util.ArrayList;

class Parada {
    String nombre;
    String linea;
    double posX;                
    double posY;               
    int nConexiones;            
    ArrayList<Parada> conexiones; 
    Pair<Parada, Double> sigConexDist; 
    boolean bano;               
    boolean minusvalido;        
    boolean atencionPasajero;   

    // Constructor para inicializar Parada (opcional)
    public Parada(String nombre, String linea, double posX, double posY, int nConexiones,
                  ArrayList<Parada> conexiones, Pair<Parada, Double> sigConexDist,
                  boolean bano, boolean minusvalido, boolean atencionPasajero) {
        this.nombre = nombre;
        this.linea = linea;
        this.posX = posX;
        this.posY = posY;
        this.nConexiones = nConexiones;
        this.conexiones = conexiones;
        this.sigConexDist = sigConexDist;
        this.bano = bano;
        this.minusvalido = minusvalido;
        this.atencionPasajero = atencionPasajero;
    }
}
