package srcC.APP;

import srcC.APP.mapa.Estacion;
import srcC.APP.mapa.Mapa;
import srcC.APP.algoritmo.Algoritmo;
import srcC.APP.mapa.Camino;
import srcC.APP.mapa.Conexion;
import srcC.APP.algoritmo.Main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controlador {
    private Map<String, Color> lineColors;
    private Mapa metroBuenosAires;

    @FXML
    private Canvas mapCanvas;

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

    public Controlador() {
        // Inicializar colores para cada línea
        lineColors = new HashMap<>();
        lineColors.put("A", Color.TURQUOISE);
        lineColors.put("B", Color.RED);
        lineColors.put("C", Color.DARKBLUE);
        lineColors.put("D", Color.GREEN);
        lineColors.put("E", Color.PURPLE);
    }

    @FXML
    public void initialize() {
        try {
            // Cargar estaciones y conexiones
            metroBuenosAires = Main.cargarEstaciones("srcC/APP/metroBuenosAires/estaciones.txt");
            Main.cargarConexiones("srcC/APP/metroBuenosAires/conexiones.txt", metroBuenosAires);

            // Llenar los ComboBox
            llenarComboBox(origenBox);
            llenarComboBox(destinoBox);

            // Dibujar el mapa
            drawMap();
        } catch (Exception e) {
            System.err.println("Error al inicializar la aplicación: " + e.getMessage());
        }
    }

    private void llenarComboBox(ComboBox<String> comboBox) {
        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            comboBox.getItems().add(estacion.getNombre());
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

        if (origen.equals(destino)) {
            resultadosArea.setText("La estación de origen y destino son las mismas.");
            return;
        }

        try {
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
        } catch (Exception e) {
            resultadosArea.setText("Error al calcular la ruta: " + e.getMessage());
        }
    }

    @FXML
    public void drawMap() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // Dibujar componentes del mapa
        drawStations(gc);
        drawConnections(gc);
        drawLegend(gc);
    }

    private void drawStations(GraphicsContext gc) {
        double scaleX = mapCanvas.getWidth() / 800.0;
        double scaleY = mapCanvas.getHeight() / 1000.0;

        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            // Coordenadas escaladas e inversión del eje Y
            double x = estacion.getPosicion().getX() * scaleX;
            double y = mapCanvas.getHeight() - (estacion.getPosicion().getY() * scaleY);

            // Obtener el color de la línea
            Color stationColor = lineColors.getOrDefault(estacion.getLinea(), Color.GRAY);

            // Dibujar círculo para la estación
            gc.setFill(stationColor);
            gc.fillOval(x - 5, y - 5, 10, 10);

            // Etiquetar la estación
            gc.setFill(Color.BLACK);
            gc.fillText(estacion.getNombre(), x + 10, y);
        }
    }

    private void drawConnections(GraphicsContext gc) {
        double scaleX = mapCanvas.getWidth() / 800.0;
        double scaleY = mapCanvas.getHeight() / 1000.0;

        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            for (Conexion conexion : estacion.getConexiones()) {
                Estacion destino = conexion.getDestino();

                // Coordenadas escaladas con inversión del eje Y
                double x1 = estacion.getPosicion().getX() * scaleX;
                double y1 = mapCanvas.getHeight() - (estacion.getPosicion().getY() * scaleY);
                double x2 = destino.getPosicion().getX() * scaleX;
                double y2 = mapCanvas.getHeight() - (destino.getPosicion().getY() * scaleY);

                // Si es un intercambio, dibujar en negro
                if (conexion.esCambioLinea()) {
                    gc.setStroke(Color.BLACK);
                } else {
                    // Usar el color de la línea del origen
                    gc.setStroke(lineColors.getOrDefault(estacion.getLinea(), Color.GRAY));
                }

                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawLegend(GraphicsContext gc) {
        double startX = 10; // Coordenadas iniciales para la leyenda
        double startY = 10;

        // Dibujar un rectángulo para cada línea
        for (Map.Entry<String, Color> entry : lineColors.entrySet()) {
            String linea = entry.getKey();
            Color color = entry.getValue();

            // Dibujar el rectángulo de color
            gc.setFill(color);
            gc.fillRect(startX, startY, 20, 20);

            // Etiquetar la línea
            gc.setFill(Color.BLACK);
            gc.fillText("Línea " + linea, startX + 30, startY + 15);

            // Mover la posición hacia abajo para la siguiente línea
            startY += 30;
        }
    }
}
