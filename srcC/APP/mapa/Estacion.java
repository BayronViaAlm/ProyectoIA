package srcC.APP.mapa;

import java.util.ArrayList;

public class Estacion {
    private String nombre;
    private boolean sBanho;
    private boolean sAtencion;
    private boolean sMinusvalido;
    private ArrayList<Conexion> conexiones;
    private Posicion pos;
    private String linea;

    public Estacion(String linea, String nombre, boolean sBanho, boolean sAtencion, boolean sMinusvalido, int posX, int posY) {
        this.linea = linea;
        this.nombre = nombre;
        this.sBanho = sBanho;
        this.sAtencion = sAtencion;
        this.sMinusvalido = sMinusvalido;
        this.conexiones = new ArrayList<>();
        this.pos = new Posicion(posX, posY);
    }

    public String getLinea() {
        return linea;
    }
    
    public String getNombre() {
        return nombre;
    }

    public boolean tieneBanho() {
        return sBanho;
    }

    public boolean tieneAtencion() {
        return sAtencion;
    }

    public boolean tieneMinusvalido() {
        return sMinusvalido;
    }

    public void agregarConexion(Conexion conexion) {
        conexiones.add(conexion);
    }

    public ArrayList<Conexion> getConexiones() {
        return conexiones;
    }

    public Posicion getPosicion() {
        return pos;
    }

}
