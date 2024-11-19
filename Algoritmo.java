import java.util.ArrayList;

public class Algoritmo {
    ArrayList<ParadaB> paradas;
    String origen;
    String destino;

    public Algoritmo(ArrayList<ParadaB> paradas, String origen, String destino){
        this.paradas = paradas;
        this.origen = origen;
        this.destino = destino;
    }

    public double heuristico(ParadaB origen, ParadaB destino){
        return Math.sqrt(Math.pow(origen.posX - destino.posX, 2) + Math.pow(origen.posY - destino.posY, 2));
    }
    
}