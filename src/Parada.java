package src;

import java.util.ArrayList;
import java.util.Objects;

public class Parada {
    public String nombre;
    public String linea;
    public double posX;
    public double posY;
    public ArrayList<Pair<Parada, Double>> conexiones;
    boolean bano;
    boolean minusvalido;
    boolean atencionPasajero;

    // A* variables
    double gScore = Double.MAX_VALUE; // Costo desde el inicio
    double fScore = Double.MAX_VALUE; // Estimación de costo total
    public String[] conexionesStr;

    // Constructor
    public Parada(String nombre, String linea, double posX, double posY,
                  ArrayList<Pair<Parada, Double>> conexiones,
                  boolean bano, boolean minusvalido, boolean atencionPasajero) {
        this.nombre = nombre;
        this.linea = linea;
        this.posX = posX;
        this.posY = posY;
        this.conexiones = conexiones;
        this.bano = bano;
        this.minusvalido = minusvalido;
        this.atencionPasajero = atencionPasajero;
    }

    // Método para calcular la distancia entre dos paradas (en metros)
    public static double calcularDistancia(Parada p1, Parada p2) {
        return Math.sqrt(Math.pow(p2.posX - p1.posX, 2) + Math.pow(p2.posY - p1.posY, 2));
    }

    // Método para calcular el tiempo de trayecto (velocidad en m/s)
    public static double calcularTiempo(double distancia, double velocidad) {
        return distancia / velocidad; // Tiempo en segundos
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parada)) return false;
        Parada parada = (Parada) o;
        return nombre.equals(parada.nombre) && linea.equals(parada.linea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, linea);
    }
}
