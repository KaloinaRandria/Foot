package affichage;
//E:\GitHub\Foot\bleu.jpg
//E:\GitHub\Foot\rouge.jpg
import jeu.Ballon;
import jeu.Joueur;
import jeu.Terrain;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GamePanel extends JFrame {
    Joueur joueur;
    Ballon ballon;
    Terrain terrain;

    JLabel imageLabel;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Charger la bibliothèque OpenCV
    }

    public Terrain initTerrain(File file) throws IOException {
        Terrain terrain1 = this.terrain.createTerrain(file);
        Object[] joueurs = this.joueur.createJoueurs(file);
        Ellipse2D ballon = this.ballon.createBallon(file);
        terrain1.setEquipe1((List<Joueur>) joueurs[0]);
        terrain1.setEquipe2((List<Joueur>) joueurs[1]);
        terrain1.setBallon(ballon);
        terrain1.setGardien();
        terrain1.equipeAttaquant();
        terrain1.setLastDefense();
        terrain1.setHorsJeu();

        return terrain1;
    }
    public GamePanel() {
        this.terrain = new Terrain();
        this.joueur = new Joueur();
        this.ballon = new Ballon();

        this.setTitle("VAR CHECK");

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Création du panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Bouton pour choisir un fichier
        JButton chooseFileButton = new JButton("Choisir une image");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chooseFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Zone d'affichage de l'image
        imageLabel = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setOpaque(true);

        // Ajout des composants au panneau principal
        panel.add(chooseFileButton, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);

        // Ajout du panneau principal à la fenêtre
        add(panel);
    }

    private void chooseFile() throws IOException {
        // Ouvre un sélecteur de fichier
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner une image");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filtre pour les fichiers d'image
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers image", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // afficher l'image
            displayImage(selectedFile);

            // superpose
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(850, 600);
            Terrain terrain = initTerrain(selectedFile);
            // Afficher les résultats
            System.out.println("Équipe 1 (Rouge) :");
            for (Joueur joueur : terrain.getEquipe1()) {
                System.out.println(joueur);
            }

            System.out.println("\nÉquipe 2 (Bleu) :");
            for (Joueur joueur : terrain.getEquipe2()) {
                System.out.println(joueur);
            }
            frame.add(new FootPanelSuperpose(terrain));
            //frame.setVisible(true);
        }
    }

    private void displayImage(File file) {
        try {
            // Initialiser le terrain et les joueurs
            Terrain terrain = initTerrain(file);

            // Charger l'image d'origine
            BufferedImage originalImage = ImageIO.read(file);

            // Créer une copie modifiable de l'image
            BufferedImage annotatedImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            // Dessiner l'image originale sur la copie
            Graphics2D g2d = annotatedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, null);

            // Dessiner les annotations pour chaque joueur
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            for (Joueur joueur : terrain.getEquipe1()) {
                annotatePlayer(g2d, joueur, Color.RED);
            }
            for (Joueur joueur : terrain.getEquipe2()) {
                annotatePlayer(g2d, joueur, Color.BLUE);
            }

            g2d.setColor(Color.BLACK);
            g2d.drawString("Ballon", (int) terrain.getBallon().getX(), (int) terrain.getBallon().getY() - 5);

            // Libérer les ressources graphiques
            g2d.dispose();

            // Redimensionner l'image annotée pour l'adapter à la fenêtre
            Image scaledImage = annotatedImage.getScaledInstance(
                    imageLabel.getWidth(),
                    imageLabel.getHeight(),
                    Image.SCALE_SMOOTH
            );
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annotatePlayer(Graphics2D g2d, Joueur joueur, Color color) {
        Ellipse2D position = joueur.getPositionCercle();
        int x = (int) position.getX();
        int y = (int) position.getY();

        // Dessiner le cercle représentant le joueur
        g2d.setColor(color);
        g2d.draw(position);

        // Dessiner l'état "Hors Jeu" si nécessaire
        if (joueur.getHorsJeu()) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Hors Jeu", x, y - 5); // Texte au-dessus du joueur
        }
    }
}
