package P2.grafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoEstaciones {
    private Map<String, Estacion> estaciones = new HashMap<>();
    private Map<String, List<Arista>> conexiones = new HashMap<>();

    public void agregarEstacion(String nombre, Coordenada coord) {
        estaciones.put(nombre, new Estacion(nombre, coord));
        conexiones.putIfAbsent(nombre, new ArrayList<>());
    }

    public void agregarConexion(String origen, String destino, double distancia, String color) {
        Arista arista = new Arista(origen, destino, distancia, color);
        conexiones.get(origen).add(arista);
        // Añadimos la conexión inversa (si es bidireccional)
        conexiones.get(destino).add(new Arista(destino, origen, distancia, color));
    }

    public Estacion getEstacion(String nombre) {
        return estaciones.get(nombre);
    }

    public List<Arista> getConexiones(String nombre) {
        return conexiones.getOrDefault(nombre, new ArrayList<>());
    }
}
