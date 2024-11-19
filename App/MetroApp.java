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
    private JPanel mapaPanel;

    private ArrayList<Parada> paradas;

    public MetroApp(ArrayList<Parada> paradas) {
        this.paradas = paradas;

        // Configuración del JFrame
        setTitle("Metro Buenos Aires");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior para selección de paradas
        JPanel seleccionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        seleccionPanel.setBorder(BorderFactory.createTitledBorder("Selecciona las paradas"));
        seleccionPanel.setBackground(Color.LIGHT_GRAY);

        seleccionPanel.add(new JLabel("Parada de origen:"));
        origenBox = new JComboBox<>(getNombresParadas());
        seleccionPanel.add(origenBox);

        seleccionPanel.add(new JLabel("Parada de destino:"));
        destinoBox = new JComboBox<>(getNombresParadas());
        seleccionPanel.add(destinoBox);

        add(seleccionPanel, BorderLayout.NORTH);

        // Panel central para el mapa
        mapaPanel = new JPanel() {
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

        JButton buscarButton = new JButton("Buscar Ruta");
        buscarButton.setBackground(Color.BLUE);
        buscarButton.setForeground(Color.WHITE);
        buscarButton.addActionListener(e -> buscarRuta());
        resultadosPanel.add(buscarButton, BorderLayout.SOUTH);

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
        List<Parada> ruta = AStar.encontrarCamino(origen, destino, 50);
        if (ruta == null) {
            resultadosArea.setText("No se encontró una ruta.");
        } else {
            StringBuilder resultados = new StringBuilder("Ruta encontrada:\n");
            for (Parada parada : ruta) {
                resultados.append("- ").append(parada.nombre).append(" (").append(parada.linea).append(")\n");
            }
            resultadosArea.setText(resultados.toString());
        }

        // Redibujar el mapa con la ruta
        mapaPanel.repaint();
    }

    private void dibujarMapa(Graphics g) {
        // Dimensiones del panel
        int panelWidth = mapaPanel.getWidth();
        int panelHeight = mapaPanel.getHeight();
    
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
                int x1 = (int) ((parada.posX - (maxX / 2)) * escalaX) + centroX;
                int y1 = centroY - (int) (parada.posY * escalaY);
                int x2 = (int) ((destino.posX - (maxX / 2)) * escalaX) + centroX;
                int y2 = centroY - (int) (destino.posY * escalaY);
    
                g.setColor(Color.GRAY);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    
        // Dibujar estaciones
        for (Parada parada : paradas) {
            int x = (int) ((parada.posX - (maxX / 2)) * escalaX) + centroX;
            int y = centroY - (int) (parada.posY * escalaY);
    
            g.setColor(getColorPorLinea(parada.linea));
            g.fillOval(x - 5, y - 5, 10, 10); // Dibujar estación como círculo
            g.setColor(Color.BLACK);
            g.drawString(parada.nombre, x + 10, y); // Dibujar nombre de la estación
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