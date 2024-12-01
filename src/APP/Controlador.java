package src.APP;

import src.APP.mapa.Estacion;
import src.APP.mapa.Mapa;
import src.APP.algoritmo.Algoritmo;
import src.APP.mapa.Camino;
import src.APP.mapa.Conexion;
import src.APP.algoritmo.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controlador {
    private final Map<String, Color> lineColors = new HashMap<>();
    private Mapa metroBuenosAires;
    private ObservableList<String> estaciones;
    private FilteredList<String> estacionesFiltradasO;
    private FilteredList<String> estacionesFiltradasD;

    @FXML
    private Canvas mapCanvas;

    // Bloque parada de origen
    @FXML
    private TextField paradaO;
    @FXML
    private Label errorLabelO;
    @FXML
    private ListView<String> listaEstacionesO;

    // Bloque parada de destino
    @FXML
    private TextField paradaD;
    @FXML
    private Label errorLabelD;
    @FXML
    private ListView<String> listaEstacionesD;

    //Bloque ruta
    @FXML
    private Label rutaLabel;
    @FXML
    private Label tiempoLabel;
    @FXML
    private Label distanciaLabel;
    @FXML
    private Label intercambiosLabel;

    //Bloque filtros
    @FXML
    private CheckBox filtroBanho;
    @FXML
    private CheckBox filtroAtencion;
    @FXML
    private CheckBox filtroAccesible;

    @FXML
    public void initialize() {
        try {
            // Inicializar colores de las líneas del mapa
            inicializarColores();

            // Cargar datos de estaciones y conexiones
            cargarDatos();

            // Inicializar listas de estaciones y filtrado
            inicializarListasEstaciones();
            inicializarFiltradoTexto();
            inicializarSeleccionListas();

            // Dibujar el mapa inicial
            drawMap();

            // Listener para pintar el origen cuando cambie el texto
            paradaO.textProperty().addListener((observable, oldValue, newValue) -> pintarOrigen(newValue));
        } catch (Exception e) {
            System.err.println("Error al inicializar la aplicación: " + e.getMessage());
        }
    }

    private void inicializarColores() {
        lineColors.put("A", Color.TURQUOISE);
        lineColors.put("B", Color.RED);
        lineColors.put("C", Color.DARKBLUE);
        lineColors.put("D", Color.GREEN);
        lineColors.put("E", Color.PURPLE);
    }

    private void cargarDatos() throws Exception {
        metroBuenosAires = Main.cargarEstaciones("src/APP/metroBuenosAires/estaciones.txt");
        Main.cargarConexiones("src/APP/metroBuenosAires/conexiones.txt", metroBuenosAires);
    }

    private void inicializarListasEstaciones() {
        estaciones = FXCollections.observableArrayList();
        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            estaciones.add(estacion.getNombre());
        }
        // Ordenar la lista de estaciones alfabéticamente
        Collections.sort(estaciones);

        estacionesFiltradasO = new FilteredList<>(estaciones, s -> true);
        estacionesFiltradasD = new FilteredList<>(estaciones, s -> true);

        listaEstacionesO.setItems(estacionesFiltradasO);
        listaEstacionesD.setItems(estacionesFiltradasD);
    }

    private void inicializarFiltradoTexto() {
        paradaO.textProperty().addListener(
                (observable, oldValue, newValue) -> actualizarFiltroTexto(estacionesFiltradasO, errorLabelO, newValue));
        paradaD.textProperty().addListener(
                (observable, oldValue, newValue) -> actualizarFiltroTexto(estacionesFiltradasD, errorLabelD, newValue));
    }

    private void actualizarFiltroTexto(FilteredList<String> listaFiltrada, Label errorLabel, String nuevoTexto) {
        listaFiltrada.setPredicate(estacion -> estacion.toLowerCase().contains(nuevoTexto.toLowerCase()));
        errorLabel.setText(listaFiltrada.isEmpty() ? "Estación no encontrada" : "");
    }

    private void inicializarSeleccionListas() {
        listaEstacionesO.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> paradaO.setText(newValue));
        listaEstacionesD.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> paradaD.setText(newValue));
    }

    @FXML
    public void drawMap() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        drawConnections(gc);
        drawStations(gc);
        drawLegend(gc);
    }

    private void drawStations(GraphicsContext gc) {
        double offsetX = 150; // Ajuste horizontal para más espacio
        double offsetY = -50; // Ajuste vertical para más espacio
        double scaleX = mapCanvas.getWidth() / 1000.0; // Escala horizontal ajustada
        double scaleY = mapCanvas.getHeight() / 1200.0; // Escala vertical ajustada

        gc.setFont(javafx.scene.text.Font.font("Arial", 10));

        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            double x = estacion.getPosicion().getX() * scaleX + offsetX;
            double y = mapCanvas.getHeight() - (estacion.getPosicion().getY() * scaleY) + offsetY;

            gc.setFill(Color.WHITE);
            gc.fillOval(x - 5, y - 5, 10, 10);

            // Dividir el nombre de la estación en palabras
            String[] palabras = estacion.getNombre().split(" ");
            double textY = y - (palabras.length - 1) *12 - 15; // Ajustar la posición inicial del texto

            for (String palabra : palabras) {
                javafx.scene.text.Text text = new javafx.scene.text.Text(palabra);
                text.setFont(gc.getFont());
                double textWidth = text.getLayoutBounds().getWidth();
                double textHeight = text.getLayoutBounds().getHeight();
                double textX = x - textWidth / 2;

                gc.setFill(Color.WHITE);
                gc.fillRect(textX - 2, textY - textHeight + 2, textWidth + 4, textHeight);

                gc.setFill(Color.BLACK);
                gc.fillText(palabra, textX, textY);

                textY += textHeight; // Mover la posición Y para la siguiente palabra
            }
        }
    }

    private void drawConnections(GraphicsContext gc) {
        double offsetX = 150;
        double offsetY = -50;
        double scaleX = mapCanvas.getWidth() / 1000.0;
        double scaleY = mapCanvas.getHeight() / 1200.0;

        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            for (Conexion conexion : estacion.getConexiones()) {
                Estacion destino = conexion.getDestino();
                double x1 = estacion.getPosicion().getX() * scaleX + offsetX;
                double y1 = mapCanvas.getHeight() - (estacion.getPosicion().getY() * scaleY) + offsetY;
                double x2 = destino.getPosicion().getX() * scaleX + offsetX;
                double y2 = mapCanvas.getHeight() - (destino.getPosicion().getY() * scaleY) + offsetY;

                gc.setStroke(conexion.esCambioLinea() ? Color.BLACK : lineColors.get(estacion.getLinea()));
                gc.setLineWidth(8);
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawLegend(GraphicsContext gc) {
        double startX = 10;
        double startY = 10;
        gc.setFont(javafx.scene.text.Font.font("Arial", 12));

        for (Map.Entry<String, Color> entry : lineColors.entrySet()) {
            String linea = entry.getKey();
            Color color = entry.getValue();

            gc.setFill(color);
            gc.fillRect(startX, startY, 20, 20);

            gc.setFill(Color.BLACK);
            gc.fillText("Línea " + linea, startX + 30, startY + 15);

            startY += 30;
        }
    }

    @FXML
    private void calcularRuta() {
        // Obtener las estaciones de los TextField
        String origen = paradaO.getText().trim();
        String destino = paradaD.getText().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            rutaLabel.setText("Por favor, ingrese el origen y el destino.");
            tiempoLabel.setText("");
            distanciaLabel.setText("");
            intercambiosLabel.setText("");
            return;
        }

        // Buscar la ruta utilizando los nombres ingresados
        List<String> camino = Algoritmo.encontrarRuta(metroBuenosAires, origen, destino);
        if (camino == null) {
            rutaLabel.setText("Ruta no encontrada entre " + origen + " y " + destino);
            tiempoLabel.setText("");
            distanciaLabel.setText("");
            intercambiosLabel.setText("");
        } else {
            // Resaltar la ruta en el mapa
            drawHighlightedRoute(camino);

            // Indicar cambios de línea en el texto de la ruta
            StringBuilder rutaTexto = new StringBuilder();
            String lineaActual = "";
            for (int i = 0; i < camino.size(); i++) {
                String estacion = camino.get(i);
                Estacion est = metroBuenosAires.getEstacion(estacion);
                if (!lineaActual.equals(est.getLinea())) {
                    if (!lineaActual.isEmpty()) {
                        rutaTexto.append(" Cambio a línea ").append(est.getLinea()).append(" en ");
                    }
                    lineaActual = est.getLinea();
                }
                rutaTexto.append(estacion);
                if (i < camino.size() - 1) {
                    rutaTexto.append(" -> ");
                }
            }
            rutaLabel.setText(rutaTexto.toString());

            // Calcular detalles de la ruta
            Camino ruta = Camino.crearCamino(camino, metroBuenosAires);
            double tiempoTotal = ruta.getTiempoTotal();
            double distanciaTotal = ruta.getDistanciaTotal();
            int intercambios = ruta.getnIntercambios();

            int horas = (int) tiempoTotal / 60;
            int minutos = (int) tiempoTotal % 60;
            tiempoLabel.setText(horas + "hh:" + minutos + "mm");
            distanciaLabel.setText(distanciaTotal / 1000 + " kilometros");
            intercambiosLabel.setText(String.valueOf(intercambios));
        }
    }

    private void drawHighlightedRoute(List<String> camino) {
        // Limpiar el mapa
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        drawConnections(gc);
        drawStations(gc);
        drawLegend(gc);

        // Resaltar la ruta
        double offsetX = 150;
        double offsetY = -50;
        double scaleX = mapCanvas.getWidth() / 1000.0;
        double scaleY = mapCanvas.getHeight() / 1200.0;

        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(3);

        for (int i = 0; i < camino.size() - 1; i++) {

            Estacion origen = metroBuenosAires.getEstacion(camino.get(i));
            Estacion destino = metroBuenosAires.getEstacion(camino.get(i + 1));

            double x1 = origen.getPosicion().getX() * scaleX + offsetX;
            double y1 = mapCanvas.getHeight() - (origen.getPosicion().getY() * scaleY) + offsetY;
            double x2 = destino.getPosicion().getX() * scaleX + offsetX;
            double y2 = mapCanvas.getHeight() - (destino.getPosicion().getY() * scaleY) + offsetY;

            gc.strokeLine(x1, y1, x2, y2);
        }

        drawStations(gc); // Dibujar las estaciones nuevamente para que estén sobre las líneas
        pintarOrigen(camino.get(0)); // Pintar el origen con un color distinto
    }

    @FXML
    private void aplicarFiltros() {
        drawMap();
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        double offsetX = 150;
        double offsetY = -50;
        double scaleX = mapCanvas.getWidth() / 1000.0;
        double scaleY = mapCanvas.getHeight() / 1200.0;

        if(!filtroAccesible.isSelected() && !filtroAtencion.isSelected() && !filtroBanho.isSelected()) {
            drawMap();
            return;
        }

        for (Estacion estacion : metroBuenosAires.getEstaciones().values()) {
            boolean cumpleFiltro = true;
            if (filtroBanho.isSelected() && !estacion.tieneBanho()) {
                cumpleFiltro = false;
            }
            if (filtroAtencion.isSelected() && !estacion.tieneAtencion()) {
                cumpleFiltro = false;
            }
            if (filtroAccesible.isSelected() && !estacion.esAccesible()) {
                cumpleFiltro = false;
            }

            if (cumpleFiltro) {
                double x = estacion.getPosicion().getX() * scaleX + offsetX;
                double y = mapCanvas.getHeight() - (estacion.getPosicion().getY() * scaleY) + offsetY;

                gc.setFill(Color.YELLOW);
                gc.fillOval(x - 7, y - 7, 14, 14);
            }
        }
    }

    private void pintarOrigen(String nombreEstacion) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        Estacion estacion = metroBuenosAires.getEstacion(nombreEstacion);
        if (estacion != null) {
            double offsetX = 150; // Ajuste horizontal para más espacio
            double offsetY = -50; // Ajuste vertical para más espacio
            double scaleX = mapCanvas.getWidth() / 1000.0; // Escala horizontal ajustada
            double scaleY = mapCanvas.getHeight() / 1200.0; // Escala vertical ajustada

            double x = estacion.getPosicion().getX() * scaleX + offsetX;
            double y = mapCanvas.getHeight() - (estacion.getPosicion().getY() * scaleY) + offsetY;

            gc.setFill(Color.RED); // Color para la estación de origen
            gc.fillOval(x - 10, y - 10, 20, 20); // Dibujar un círculo más grande para resaltar la estación de origen

            // Dibujar una estrella o símbolo distintivo
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(new double[]{x, x - 5, x + 5}, new double[]{y - 10, y + 5, y + 5}, 3);
        }
    }
}
