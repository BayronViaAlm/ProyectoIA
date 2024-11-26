package App;

import src.AStar;
import src.Main;
import src.Pair;
import src.Parada;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MetroApp extends JFrame {
    private JComboBox<String> origenBox;
    private JComboBox<String> destinoBox;
    private JTextArea resultadosArea;
    private JLabel tiempoTrayectoLabel;

    private ArrayList<Parada> paradas;
    private List<Parada> rutaActual;

    private Color colorParadas = Color.RED; // Color de los puntos y las líneas
    private int grosorParadas = 5; // Grosor de los puntos y las líneas

    public MetroApp(ArrayList<Parada> paradas) {
        this.paradas = paradas;

        // Configuración del JFrame
        setTitle("Metro Buenos Aires");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior para selección de paradas
        JPanel seleccionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        seleccionPanel.setBorder(BorderFactory.createTitledBorder("Selecciona las paradas"));
        seleccionPanel.setBackground(Color.LIGHT_GRAY);

        seleccionPanel.add(new JLabel("Parada de origen:"));
        origenBox = new JComboBox<>(getNombresParadas());
        seleccionPanel.add(origenBox);

        seleccionPanel.add(new JLabel("Parada de destino:"));
        destinoBox = new JComboBox<>(getNombresParadas());
        seleccionPanel.add(destinoBox);

        tiempoTrayectoLabel = new JLabel("Tiempo de trayecto: ");
        seleccionPanel.add(tiempoTrayectoLabel);

        JButton buscarButton = new JButton("Buscar Ruta");
        buscarButton.setBackground(Color.BLUE);
        buscarButton.setForeground(Color.WHITE);
        buscarButton.addActionListener(e -> buscarRuta());
        seleccionPanel.add(buscarButton);

        add(seleccionPanel, BorderLayout.NORTH);

        // Panel central para el mapa
        JPanel mapaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarMapa(g);
            }
        };
        mapaPanel.setBorder(BorderFactory.createTitledBorder("Mapa del metro"));
        add(mapaPanel, BorderLayout.CENTER);

        // Panel inferior para resultados
        JPanel resultadosPanel = new JPanel(new BorderLayout());
        resultadosArea = new JTextArea(10, 30);
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
        resultadosPanel.add(scrollPane, BorderLayout.CENTER);

        add(resultadosPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String[] getNombresParadas() {
        return paradas.stream().map(p -> p.nombre).toArray(String[]::new);
    }

    private void buscarRuta() {
        String origenNombre = (String) origenBox.getSelectedItem();
        String destinoNombre = (String) destinoBox.getSelectedItem();

        Parada origen = paradas.stream().filter(p -> p.nombre.equals(origenNombre)).findFirst().orElse(null);
        Parada destino = paradas.stream().filter(p -> p.nombre.equals(destinoNombre)).findFirst().orElse(null);

        if (origen == null || destino == null) {
            resultadosArea.setText("Seleccione paradas válidas.");
            return;
        }

        // Ejecutar el algoritmo A* (simulado para este ejemplo)
        rutaActual = AStar.encontrarCamino(origen, destino, 50);
        if (rutaActual == null) {
            resultadosArea.setText("No se encontró una ruta.");
        } else {
            StringBuilder resultados = new StringBuilder("Ruta encontrada:\n");
            for (Parada parada : rutaActual) {
                resultados.append("- ").append(parada.nombre).append(" (").append(parada.linea).append(")\n");
            }
            resultadosArea.setText(resultados.toString());
            tiempoTrayectoLabel.setText("Tiempo de trayecto: " + calcularTiempoTrayecto(rutaActual) + " minutos");
        }

        // Redibujar el mapa con la ruta
        repaint();
    }

    private int calcularTiempoTrayecto(List<Parada> ruta) {
        // Simulación del cálculo del tiempo de trayecto
        return ruta.size() * 5; // Ejemplo: 5 minutos por parada
    }

    private void dibujarMapa(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(grosorParadas));

        // Dimensiones del panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Configuración del rango de coordenadas (X: 0-800, Y: 0-1200)
        double minX = 0, maxX = 800;
        double minY = 0, maxY = 1200;

        // Factores de escala
        double escalaX = panelWidth / (maxX - minX);
        double escalaY = panelHeight / (maxY - minY);

        // Ajuste del centro (0,0) al centro inferior del panel
        int centroX = panelWidth / 2; // Centro horizontal del panel
        int centroY = panelHeight;    // Base del panel como eje Y=0

        // Dibujar conexiones
        for (Parada parada : paradas) {
            for (Pair<Parada, Double> conexion : parada.conexiones) {
                Parada destino = conexion.getSigParada();

                // Transformar coordenadas de la conexión
                int x1 = (int) ((parada.posX - minX) * escalaX);
                int y1 = panelHeight - (int) ((parada.posY - minY) * escalaY);
                int x2 = (int) ((destino.posX - minX) * escalaX);
                int y2 = panelHeight - (int) ((destino.posY - minY) * escalaY);

                g2d.setColor(getColorPorLinea(parada.linea));
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        // Dibujar estaciones
        for (Parada parada : paradas) {
            int x = (int) ((parada.posX - minX) * escalaX);
            int y = panelHeight - (int) ((parada.posY - minY) * escalaY);

            if (rutaActual != null && rutaActual.contains(parada)) {
                g2d.setColor(Color.YELLOW); // Destacar paradas de la ruta
                g2d.fillOval(x - 7, y - 7, 14, 14);
            } else {
                g2d.setColor(getColorPorLinea(parada.linea));
                g2d.fillOval(x - 5, y - 5, 10, 10);
            }

            g2d.setColor(Color.BLACK);
            g2d.drawString(parada.nombre, x + 10, y);

            // Dibujar iconos si la parada tiene servicios
            if (parada.bano) {
                g2d.drawImage(new ImageIcon("icons/bano.png").getImage(), x - 15, y - 15, 10, 10, this);
            }
            if (parada.atencionPasajero) {
                g2d.drawImage(new ImageIcon("icons/oficina.png").getImage(), x - 15, y - 5, 10, 10, this);
            }
            if (parada.minusvalido) {
                g2d.drawImage(new ImageIcon("icons/minusvalidos.png").getImage(), x - 15, y + 5, 10, 10, this);
            }
        }
    }

    private Color getColorPorLinea(String linea) {
        switch (linea) {
            case "A": return Color.BLUE;
            case "B": return Color.RED;
            case "C": return Color.GREEN;
            case "D": return Color.ORANGE;
            case "E": return Color.MAGENTA;
            default: return Color.BLACK;
        }
    }

    public static void main(String[] args) {
        ArrayList<Parada> paradas = Main.cargarParadas("ficheros_texto/Lineas2.txt");
        new MetroApp(paradas);
    }
}