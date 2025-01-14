package jeu;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.geom.Ellipse2D;
import java.io.File;
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

    public Object[] createJoueurs(File imageFile) {
        String imagePath = imageFile.getPath();
        Object[] equipes = new Object[2];

        Mat image = chargerImage(imagePath);
        if (image == null) {
            System.out.println("Erreur de chargement de l'image");
            return null;
        }

        Mat gray = pretraiterImage(image);
        Mat circles = detecterCercles(gray);

        List<Joueur> equipe1 = new ArrayList<>();
        List<Joueur> equipe2 = new ArrayList<>();

        if (circles.cols() > 0) {
            traiterCercles(image, circles, equipe1, equipe2);
        }

        equipes[0] = equipe1;
        equipes[1] = equipe2;
        return equipes;
    }

    // Charger l'image
    private Mat chargerImage(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath);
        if (image.empty()) {
            return null;
        }
        return image;
    }

    // Convertir l'image en niveaux de gris et appliquer un flou
    private Mat pretraiterImage(Mat image) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(9, 9), 2, 2);
        return gray;
    }

    // Détecter les cercles avec HoughCircles
    private Mat detecterCercles(Mat gray) {
        Mat circles = new Mat();
        Imgproc.HoughCircles(
                gray, circles, Imgproc.HOUGH_GRADIENT, 1, 20,
                50, 30, 10, 30 // Paramètres
        );
        return circles;
    }

    // Traiter les cercles détectés et classer les joueurs par équipe
    private void traiterCercles(Mat image, Mat circles, List<Joueur> equipe1, List<Joueur> equipe2) {
        int idJoueurEquipe1 = 0;
        int idJoueurEquipe2 = 0;
        double seuilLuminosite = 50.0;

        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
            if (circle == null) continue;

            int x = (int) Math.round(circle[0]);
            int y = (int) Math.round(circle[1]);
            int radius = (int) Math.round(circle[2]);

            Scalar meanColor = calculerCouleurMoyenne(image, x, y, radius);
            double luminosite = calculerLuminosite(meanColor);

            if (luminosite < seuilLuminosite) continue;

            classerJoueur(x, y, radius, meanColor, equipe1, equipe2, idJoueurEquipe1, idJoueurEquipe2);
        }
    }

    // Calculer la couleur moyenne à l'intérieur d'un cercle
    private Scalar calculerCouleurMoyenne(Mat image, int x, int y, int radius) {
        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);
        Imgproc.circle(mask, new Point(x, y), radius, new Scalar(255), -1);
        return Core.mean(image, mask);
    }

    // Calculer la luminosité moyenne
    private double calculerLuminosite(Scalar meanColor) {
        return 0.299 * meanColor.val[2] + 0.587 * meanColor.val[1] + 0.114 * meanColor.val[0];
    }

    // Classer un joueur selon sa couleur moyenne
    private void classerJoueur(int x, int y, int radius, Scalar meanColor, List<Joueur> equipe1, List<Joueur> equipe2,
                               int idJoueurEquipe1, int idJoueurEquipe2) {
        int diametre = radius * 2;
        Ellipse2D position = new Ellipse2D.Double(x - (diametre / 2), y - (diametre / 2), diametre, diametre);

        if (meanColor.val[2] > meanColor.val[0]) { // Rouge > Bleu
            equipe1.add(new Joueur(idJoueurEquipe1++, 1, position));
        } else if (meanColor.val[0] > meanColor.val[2]) { // Bleu > Rouge
            equipe2.add(new Joueur(idJoueurEquipe2++, 2, position));
        }
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
}
