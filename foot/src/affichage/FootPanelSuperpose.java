package affichage;

import jeu.Joueur;
import jeu.Terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class FootPanelSuperpose extends JPanel {
    private final Terrain terrain;

    public FootPanelSuperpose(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dessiner le terrain
        g2d.setColor(Color.GREEN);
        g2d.fill(terrain.getFormeTerrain());

        // Dessiner les joueurs de l'équipe 1 (Rouge)
        g2d.setColor(Color.RED);
        for (Joueur joueur : terrain.getEquipe1()) {
            Ellipse2D position = joueur.getPositionCercle();
            g2d.fill(position);

            // Afficher l'ID du joueur au-dessus
            String idText = "ID: " + joueur.getId();
            g2d.setColor(Color.BLACK); // Texte en noir pour contraster
            g2d.drawString(idText, (int) position.getCenterX() - 10, (int) position.getCenterY() - 10);
            g2d.setColor(Color.RED); // Restaurer la couleur de l'équipe
        }

        // Dessiner les joueurs de l'équipe 2 (Bleu)
        g2d.setColor(Color.BLUE);
        for (Joueur joueur : terrain.getEquipe2()) {
            Ellipse2D position = joueur.getPositionCercle();
            g2d.fill(position);

            // Afficher l'ID du joueur au-dessus
            String idText = "ID: " + joueur.getId();
            g2d.setColor(Color.BLACK); // Texte en noir pour contraster
            g2d.drawString(idText, (int) position.getCenterX() - 10, (int) position.getCenterY() - 10);
            g2d.setColor(Color.BLUE); // Restaurer la couleur de l'équipe
        }

        // Dessiner le ballon
        g2d.setColor(Color.ORANGE);
        g2d.fill(terrain.getBallon());
    }
}

