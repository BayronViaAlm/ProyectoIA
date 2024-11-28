package srcC.APP;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import srcC.APP.mapa.Estacion;
import srcC.APP.mapa.Mapa;
import srcC.APP.algoritmo.Algoritmo;
import srcC.APP.algoritmo.Main;
import srcC.APP.mapa.Camino;

import java.util.List;

public class Controlador {

    @FXML
    private ComboBox<String> origenBox;

    @FXML
    private ComboBox<String> destinoBox;

    @FXML
    private TextArea resultadosArea;

    @FXML
    private Label tiempoTotalLabel;

    @FXML
    private Label distanciaTotalLabel;

    @FXML
    private Label intercambiosLabel;

    private Mapa metroBuenosAires;

    @FXML
    public void initialize() {
        metroBuenosAires = Main.cargarEstaciones("srcC/metroBuenosAires/estaciones.txt");
        Main.cargarConexiones("srcC/metroBuenosAires/conexiones.txt", metroBuenosAires);

        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            origenBox.getItems().add(estacion.getNombre());
            destinoBox.getItems().add(estacion.getNombre());
        }
    }

    @FXML
    public void buscarRuta() {
        String origen = origenBox.getValue();
        String destino = destinoBox.getValue();

        if (origen == null || destino == null) {
            resultadosArea.setText("Por favor, seleccione una estación de origen y una de destino.");
            return;
        }

        List<String> camino = Algoritmo.encontrarRuta(metroBuenosAires, origen, destino);
        if (camino == null) {
            resultadosArea.setText("No se encontró una ruta entre " + origen + " y " + destino);
        } else {
            resultadosArea.setText("Ruta encontrada: " + camino);
            Camino ruta = Camino.crearCamino(camino, metroBuenosAires);
            int horas = (int) ruta.getTiempoTotal() / 60;
            int minutos = (int) (ruta.getTiempoTotal() % 60);
            tiempoTotalLabel.setText(horas + "hh:" + minutos + "mm");
            distanciaTotalLabel.setText((double) ruta.getDistanciaTotal() / 1000 + " km");
            intercambiosLabel.setText(String.valueOf(ruta.getnIntercambios()));
        }
    }
}
