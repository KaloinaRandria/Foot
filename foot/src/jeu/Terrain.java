package jeu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    List<Joueur> equipe1;
    List<Joueur> equipe2;
    Ellipse2D ballon;
    Rectangle formeTerrain;

    public void setGardien() {
        Joueur gardien1 = this.getEquipe1().get(0);
        this.getEquipe1().get(0).setGardien(true);
        for (int i = 1; i < this.getEquipe1().size(); i++) {
            if (gardien1.getPositionCercle().getX() > this.getEquipe1().get(i).getPositionCercle().getX()) {
                this.getEquipe1().get(i).setGardien(true);
                this.getEquipe1().get(gardien1.getId()).setGardien(false);
                gardien1 = this.getEquipe1().get(i);
            }
        }

        Joueur gardien2 = this.getEquipe2().get(0);
        this.getEquipe2().get(0).setGardien(true);
        for (int i = 1; i < this.getEquipe2().size(); i++) {
            if (gardien2.getPositionCercle().getX() + gardien2.getPositionCercle().getWidth() < this.getEquipe2().get(i).getPositionCercle().getX() + gardien2.getPositionCercle().getWidth()) {
                this.getEquipe2().get(i).setGardien(true);
                this.getEquipe2().get(gardien2.getId()).setGardien(false);
                gardien2 = this.getEquipe2().get(i);
            }
        }
    }

    public void setLastDefense() {
        int attaquant = equipeAttaquant();
        if (attaquant == 1) {
            Joueur lastDefense = getEquipe2().get(0);
            getEquipe2().get(0).setLastDefense(true);
            if (getEquipe2().get(0).isGardien) {
                lastDefense = getEquipe2().get(1);
                getEquipe2().get(1).setLastDefense(true);
                getEquipe2().get(0).setLastDefense(false);
            }
            for (int i = 0; i < getEquipe2().size(); i++) {
                if (lastDefense.getPositionCercle().getX() + lastDefense.getPositionCercle().getWidth() < getEquipe2().get(i).getPositionCercle().getX()  + getEquipe2().get(i).getPositionCercle().getWidth() && !getEquipe2().get(i).isGardien ){
                    getEquipe2().get(i).setLastDefense(true);
                    getEquipe2().get(lastDefense.getId()).setLastDefense(false);
                    lastDefense = getEquipe2().get(i);
                }
            }

        } else if (attaquant == 2) {
            Joueur lastDefense = getEquipe1().get(0);
            getEquipe1().get(0).setLastDefense(true);
            if (getEquipe1().get(0).isGardien) {
                lastDefense = getEquipe1().get(1);
                getEquipe1().get(1).setLastDefense(true);
                getEquipe1().get(0).setLastDefense(false);
            }
            for (int i = 0; i < getEquipe1().size(); i++) {
                if (lastDefense.getPositionCercle().getX() > getEquipe1().get(i).getPositionCercle().getX() && !getEquipe1().get(i).isGardien){
                    getEquipe1().get(i).setLastDefense(true);
                    getEquipe1().get(lastDefense.getId()).setLastDefense(false);
                    lastDefense = getEquipe1().get(i);
                }
            }
        }
    }

    public void setHorsJeu () {
        int attaquant = equipeAttaquant();
        if (attaquant == 1) {
            for (Joueur joueur2 : getEquipe2()) {
                if (joueur2.isLastDefense) {
                    for (int i = 0; i < getEquipe1().size(); i++) {
                        if (joueur2.getPositionCercle().getX() + joueur2.positionCercle.getWidth() < getEquipe1().get(i).positionCercle.getX() + getEquipe1().get(i).positionCercle.getWidth() ) {
                            getEquipe1().get(i).setHorsJeu(true);
                        }
                    }
                    break;
                }
            }
        }
        else if (attaquant == 2) {
            for (Joueur joueur1 : getEquipe1()) {
                if (joueur1.isLastDefense) {
                    for (int i = 0; i < getEquipe2().size(); i++) {
                        if (joueur1.getPositionCercle().getX() > getEquipe2().get(i).positionCercle.getX() ) {
                            getEquipe2().get(i).setHorsJeu(true);
                        }
                    }
                    break;
                }
            }
        }
    }

    public void setMety () {
        int attaquant = equipeAttaquant();
        if (attaquant == 1) {
            for (int i = 0; i < getEquipe1().size(); i++) {
                if (ballon.getX() + ballon.getWidth() < getEquipe1().get(i).getPositionCercle().getX() + getEquipe1().get(i).positionCercle.getWidth() && !getEquipe1().get(i).isHorsJeu) {

                }
            }
        }
        else if (attaquant == 2) {
            for (Joueur joueur1 : getEquipe1()) {
                if (joueur1.isLastDefense) {
                    for (int i = 0; i < getEquipe2().size(); i++) {
                        if (joueur1.getPositionCercle().getX() > getEquipe2().get(i).positionCercle.getX() ) {
                            getEquipe2().get(i).setHorsJeu(true);
                        }
                    }
                    break;
                }
            }
        }
    }


    public int equipeAttaquant() {
        Point2D centreBallon = getCentreBallon();
        Joueur joueurLePlusProche = getJoueurLePlusProche(centreBallon);
        return joueurLePlusProche.getEquipe();
    }

    private Point2D getCentreBallon() {
        return new Point2D.Double(
                ballon.getCenterX(),
                ballon.getCenterY()
        );
    }

    private Joueur getJoueurLePlusProche(Point2D centreBallon) {
        Joueur joueurLePlusProche = null;
        double distanceMin = Double.MAX_VALUE;

        for (Joueur joueur : getAllJoueurs()) {
            Point2D centreJoueur = getCentreJoueur(joueur);
            double distance = centreBallon.distance(centreJoueur);
            if (distance < distanceMin) {
                distanceMin = distance;
                joueurLePlusProche = joueur;
            }
        }

        return joueurLePlusProche;
    }

    private Point2D getCentreJoueur(Joueur joueur) {
        return new Point2D.Double(
                joueur.getPositionCercle().getCenterX(),
                joueur.getPositionCercle().getCenterY()
        );
    }

    private List<Joueur> getAllJoueurs() {
        List<Joueur> allJoueurs = new ArrayList<>();
        allJoueurs.addAll(getEquipe1());
        allJoueurs.addAll(getEquipe2());
        return allJoueurs;
    }







    public Terrain createTerrain(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        return new Terrain(
                new Rectangle(0,0,image.getWidth(),image.getHeight()));
    }

    public List<Joueur> getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(List<Joueur> equipe1) {
        this.equipe1 = equipe1;
    }

    public List<Joueur> getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(List<Joueur> equipe2) {
        this.equipe2 = equipe2;
    }

    public Ellipse2D getBallon() {
        return ballon;
    }

    public void setBallon(Ellipse2D ballon) {
        this.ballon =  ballon;
    }

    public Rectangle getFormeTerrain() {
        return formeTerrain;
    }

    public void setFormeTerrain(Rectangle formeTerrain) {
        this.formeTerrain = formeTerrain;
    }

    public Terrain(List<Joueur> equipe1, List<Joueur> equipe2, Ellipse2D ballon, Rectangle formeTerrain) {
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.ballon = ballon;
        this.formeTerrain = formeTerrain;
    }

    public Terrain() {
    }

    public Terrain(Rectangle formeTerrain) {
        this.formeTerrain = formeTerrain;
    }
}
