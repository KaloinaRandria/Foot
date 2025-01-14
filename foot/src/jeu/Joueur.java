package jeu;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Joueur {
    int id;
    int equipe; // équipe 1 ou équipe 2
    Ellipse2D positionCercle;
    Boolean isHorsJeu;
    Boolean isGardien;
    Boolean isLastDefense;
    Boolean isMety;

    public Object[] createJoueurs(File imageFile) throws IOException {
        String imagePath = imageFile.getPath();
        Object[] equipes = new Object[2];
        Mat image = Imgcodecs.imread(imagePath);

        if (image.empty()) {
            System.out.println("Erreur : Impossible de charger l'image !");
            return null;
        }

        // Convertir en niveaux de gris
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Appliquer un flou pour réduire le bruit
        Imgproc.GaussianBlur(gray, gray, new Size(9, 9), 2, 2);

        // Détecter les cercles avec HoughCircles
        Mat circles = new Mat();
        Imgproc.HoughCircles(
                gray, circles, Imgproc.HOUGH_GRADIENT, 1, 20,
                50, 30, 10, 30 // Paramètres : ajustez si nécessaire
        );

        // Listes des joueurs
        List<Joueur> equipe1 = new ArrayList<>();
        List<Joueur> equipe2 = new ArrayList<>();

        int id1 = 0;
        int id2 = 0;
        double seuilLuminosite = 50.0; // Ajustez ce seuil selon vos besoins

        if (circles.cols() > 0) {
            for (int i = 0; i < circles.cols(); i++) {
                double[] circle = circles.get(0, i);
                if (circle == null) continue;

                int x = (int) Math.round(circle[0]);
                int y = (int) Math.round(circle[1]);
                int radius = (int) Math.round(circle[2]);
                int diametre = radius * 2;

                // Extraire la couleur moyenne dans le cercle
                Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);
                Imgproc.circle(mask, new Point(x, y), radius, new Scalar(255), -1);

                Scalar meanColor = Core.mean(image, mask);

                // Calculer la luminosité moyenne
                double luminosite = 0.299 * meanColor.val[2] + 0.587 * meanColor.val[1] + 0.114 * meanColor.val[0];

                // Ignorer les points trop sombres
                if (luminosite < seuilLuminosite) {
                    continue;
                }

                // Vérifier si c'est rouge (équipe 1) ou bleu (équipe 2)
                int equipe;
                if (meanColor.val[2] > meanColor.val[0]) { // Rouge > Bleu
                    equipe = 1;
                    Ellipse2D position = new Ellipse2D.Double(x - (diametre / 2), y - (diametre / 2), diametre, diametre);
                    equipe1.add(new Joueur(id1, equipe, position));
                    id1++;
                } else if (meanColor.val[0] > meanColor.val[2]) { // Bleu > Rouge
                    equipe = 2;
                    Ellipse2D position = new Ellipse2D.Double(x - (diametre / 2), y - (diametre / 2), diametre, diametre);
                    equipe2.add(new Joueur(id2, equipe, position));
                    id2++;
                }
            }
        }

        equipes[0] = equipe1;
        equipes[1] = equipe2;
        return equipes;
    }


    // Constructeurs
    public Joueur(int id, int equipe, Ellipse2D formeJoueur) {
        this.id = id;
        this.equipe = equipe;
        this.positionCercle = formeJoueur;
        this.isHorsJeu = false;
        this.isGardien = false;
        this.isLastDefense = false;
        this.isMety = false;
    }

    public Joueur() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipe() {
        return equipe;
    }

    public void setEquipe(int equipe) {
        this.equipe = equipe;
    }

    public Ellipse2D getPositionCercle() {
        return positionCercle;
    }

    public void setPositionCercle(Ellipse2D positionCercle) {
        this.positionCercle = positionCercle;
    }

    public Boolean getHorsJeu() {
        return isHorsJeu;
    }

    public void setHorsJeu(Boolean horsJeu) {
        isHorsJeu = horsJeu;
    }

    public Boolean getGardien() {
        return isGardien;
    }

    public void setGardien(Boolean gardien) {
        isGardien = gardien;
    }

    public Boolean getLastDefense() {
        return isLastDefense;
    }

    public void setLastDefense(Boolean lastDefense) {
        isLastDefense = lastDefense;
    }

    public Boolean getMety() {
        return isMety;
    }

    public void setMety(Boolean mety) {
        isMety = mety;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", equipe=" + equipe +
                ", xposition=" + positionCercle.getX() +
                ", yposition=" + (positionCercle.getY()) +
                ", diametre=" + (positionCercle.getHeight()) +
                ", isHorsJeu=" + isHorsJeu +
                ", isGardien=" + isGardien +
                ", isLastDefense=" + isLastDefense +
                '}';

    }
}
