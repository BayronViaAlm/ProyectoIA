import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainB {
    public static ArrayList<Parada> cargarParadas(String fileName) {
        ArrayList<Parada> paradas = new ArrayList<>();
        try{
            List<String> allLines = Files.readAllLines(Paths.get("lineas.txt"));
            String linea, nombre;
            int  posX, posY, n_conexiones;
            boolean hasServicios, hasMinusvalia, hasAtencion;
            String[] conexiones
            for (String line : allLines){
                String[] partes = line.split(";");
                linea = partes[0];
                nombre = partes[1];
                posX = Integer.parseInt(partes[2]);
                posY = Integer.parseInt(partes[3]);
                n_conexiones = Integer.parseInt(partes[4]);
                conexiones = new String[n_conexiones];
                if (n_conexiones == 1){
                    conexiones[0] = partes[5];
                } else {
                String[] conectores = partes[5].split(":");
                for (int i = 0; i < n_conexiones; i++){
                    conexiones[i] = conectores[i];
                    }
                }
                if (partes[6].equals("true")){
                    hasServicios = true;
                }
                if (partes[7].equals("true")){
                    hasMinusvalia = true;
                }
                if (partes[8].equals("true")){
                    hasAtencion = true;
                }
                paradas.add(new Parada(nombre, linea, posX, posY,
                                     n_conexiones, conexiones, hasServicios,
                                     hasMinusvalia, hasAtencion));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return paradas;
    }
}