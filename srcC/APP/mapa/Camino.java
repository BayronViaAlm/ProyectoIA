package srcC.APP.mapa;

import java.util.List;

public class Camino {
    private List<String> ruta;
    private int nIntercambios;
    private double tiempoTotal;
    private int distanciaTotal;

    private Camino(List<String> ruta, int nIntercambios, double tiempoTotal, int distanciaTotal) {
        this.ruta = ruta;
        this.nIntercambios = nIntercambios;
        this.tiempoTotal = tiempoTotal;
        this.distanciaTotal = distanciaTotal;
    }

    public static Camino crearCamino(List<String> ruta, Mapa metro) {
        int nIntercambios = 0;
        double tiempoTotal = 0;
        int distanciaTotal = 0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            Estacion origen = metro.getEstacion(ruta.get(i));
            Estacion destino = metro.getEstacion(ruta.get(i + 1));
            Conexion conexion = metro.getConexion(origen, destino);
            if (conexion.esCambioLinea()) {
                nIntercambios++;
                continue; // No sumar distancia
            }
            distanciaTotal += conexion.getDistancia();
            System.out.println("origen " + origen.getNombre() + " destino " + destino.getNombre() + " distancia " + conexion.getDistancia());
        }
        tiempoTotal += (distanciaTotal / (Mapa.VELOCIDAD_METRO*1000/60)) + (ruta.size()-nIntercambios) * 1 + nIntercambios*5  ; // 1 min por parada metro (incluyendo tiempo de frenado y aceleracion)
        return new Camino(ruta, nIntercambios, tiempoTotal, distanciaTotal);
    }

    public double getTiempoTotal() {
        return tiempoTotal;
    }

    public int getDistanciaTotal() {
        return distanciaTotal;
    }   

    public int getnIntercambios() {
        return nIntercambios;
    }
    
}
