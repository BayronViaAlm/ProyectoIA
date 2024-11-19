import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainB {
    public static ArrayList<ParadaB> cargarParadas(String fileName) {
        ArrayList<ParadaB> paradas = new ArrayList<>();
        try{
            List<String> allLines = Files.readAllLines(Paths.get(fileName));
            String linea, nombre;
            int cont = 1;
            int  posX, posY, n_conexiones;
            boolean hasServicios = false;
            boolean hasMinusvalia = false;
            boolean hasAtencion = false;
            String[] conexiones;
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
                    System.arraycopy(conectores, 0, conexiones, 0, n_conexiones);
                }
                if (partes[6].equals("true")){
                    hasServicios = true;
                }
                if (partes[7].equals("true")){
                    hasMinusvalia = true;
                }
                System.out.println(cont);
                if (partes[8].equals("true")){
                    hasAtencion = true;
                }
                paradas.add(new ParadaB(nombre, linea, posX, posY,
                        n_conexiones, conexiones, hasServicios,
                        hasMinusvalia, hasAtencion));
                cont++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return paradas;
    }
}
