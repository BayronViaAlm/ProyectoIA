package src.APP.mapa;

public class Posicion {
    private int x;
    private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

     public double distanciaEuclidiana(Posicion otra) {
        return Math.sqrt(Math.pow(x - otra.x, 2) + Math.pow(y - otra.y, 2));
    }
     
}