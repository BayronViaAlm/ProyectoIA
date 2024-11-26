package srcC.mapa;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Mapa {
    public static final double VELOCIDAD_METRO = 21; // km/h
    private Map<String, Estacion> estaciones;
    private Map<Estacion, List<Conexion>> conexiones;

    public Mapa() {
        estaciones = new HashMap<>();
        conexiones = new HashMap<>();
    }

    public void agregarEstacion(String linea, String nombre, boolean sBanho, boolean sAtencion, boolean sMinusvalido, int posX, int posY) {
        estaciones.put(nombre, new Estacion(linea, nombre, sBanho, sAtencion, sMinusvalido, posX, posY));
        conexiones.putIfAbsent(estaciones.get(nombre), estaciones.get(nombre).getConexiones());
    }

    public void agregarConexion(String origen, String destino, boolean cambioLinea, int distancia) {
        Conexion conexion = new Conexion(estaciones.get(origen), estaciones.get(destino), cambioLinea, distancia);
        estaciones.get(origen).agregarConexion(conexion);
    }

    public Estacion getEstacion(String nombre) {
        return estaciones.get(nombre);
    }

    public List<Conexion> getConexiones(Estacion estacion) {
        return conexiones.getOrDefault(estacion, estacion.getConexiones());
    }   

    public Conexion getConexion(Estacion origen, Estacion destino) {
        for (Conexion conexion : conexiones.get(origen)) {
            if (conexion.getDestino().equals(destino)) {
                return conexion;
            }
        }
        return null;
    }

    public Map<String, Estacion> getEstaciones() {
        return estaciones;
    }
}
