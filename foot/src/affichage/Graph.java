package affichage;

import javax.swing.*;
import java.awt.*;

public class Graph extends JFrame {
    public Graph() {
        super("Detecteur de Hors-Jeu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        // Création du panneau principal
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessin d'un exemple (lignes pour représenter un terrain de football)
                g.setColor(Color.GREEN); // Couleur du fond
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE); // Couleur des lignes
                g.drawRect(50, 50, getWidth() - 100, getHeight() - 100); // Rectangle du terrain
                g.drawLine(getWidth() / 2, 50, getWidth() / 2, getHeight() - 50); // Ligne centrale
                g.drawOval(getWidth() / 2 - 50, getHeight() / 2 - 50, 100, 100); // Cercle central
            }
        };

        // Ajout du panneau au contenu de la fenêtre
        panel.setBackground(Color.DARK_GRAY); // Couleur de fond si aucun dessin
        add(panel);

        // Rendre la fenêtre visible
        setVisible(true);
    }
}
