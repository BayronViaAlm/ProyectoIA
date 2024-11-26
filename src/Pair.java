package src;
public class Pair<E, D> {  
    private E sigParada;   
    private D distancia;   

    // Constructor
    public Pair(E sig, D dist) {
        this.sigParada = sig;
        this.distancia = dist;
    }

    // Getters
    public E getSigParada() {  
        return sigParada;
    }

    public D getDistancia() {  
        return distancia;
    }
}
