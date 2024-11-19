import java.util.ArrayList;
import java.util.List;

public class tester {
    public static void main(String[] args) {
        // Crear datos de prueba cargando de Lineas.txt
        ArrayList<Parada> paradas = Main.cargarParadas("Lineas2.txt");

        // Encontrar paradas específicas para la prueba
        Parada inicio = null, destino = null;
        for (Parada p : paradas) {
            if (p.nombre.equals("Retiro")) inicio = p;
            if (p.nombre.equals("Facultad de medicina")) destino = p;
        }

        if (inicio != null && destino != null) {
            // Ejecutar A*
            List<Parada> camino = AStar.encontrarCamino(inicio, destino, 50.0);
            if (camino != null) {
                System.out.println("Camino encontrado:");
                for (Parada p : camino) {
                    System.out.println(p.nombre + " (" + p.linea + ")");
                }
            } else {
                System.out.println("No se encontró camino.");
            }
        } else {
            System.out.println("No se encontraron las paradas de inicio o destino.");
        }
    }
}
