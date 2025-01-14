package jeu;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

public class Ballon {

    public Ellipse2D createBallon(File imageFile) throws IOException {
        String imagePath = imageFile.getPath();
        Mat image = Imgcodecs.imread(imagePath);

        if (image.empty()) {
            System.out.println("Erreur : Impossible de charger l'image !");
            return null;
        }

        Mat gray = preprocessImage(image);
        Mat circles = detectCircles(gray);

        return findBallPosition(image, circles);
    }

    private Mat preprocessImage(Mat image) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(9, 9), 2, 2);
        return gray;
    }

    private Mat detectCircles(Mat gray) {
        Mat circles = new Mat();
        Imgproc.HoughCircles(
                gray, circles, Imgproc.HOUGH_GRADIENT, 1, 20,
                50, 30, 5, 50 // Paramètres ajustables pour le ballon
        );
        return circles;
    }

    private Ellipse2D findBallPosition(Mat image, Mat circles) {
        if (circles.cols() > 0) {
            for (int i = 0; i < circles.cols(); i++) {
                double[] circle = circles.get(0, i);
                if (circle == null) continue;

                int x = (int) Math.round(circle[0]);
                int y = (int) Math.round(circle[1]);
                int radius = (int) Math.round(circle[2]);
                int diametre = radius * 2;

                Scalar meanColor = extractMeanColor(image, x, y, radius);

                if (isBallColor(meanColor)) {
                    return createEllipse(x, y, diametre);
                }
            }
        }

        System.out.println("Aucun ballon détecté !");
        return null;
    }

    private Scalar extractMeanColor(Mat image, int x, int y, int radius) {
        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);
        Imgproc.circle(mask, new Point(x, y), radius, new Scalar(255), -1);
        return Core.mean(image, mask);
    }

    private boolean isBallColor(Scalar meanColor) {
        double maxDarknessThreshold = 50.0; // Seuil maximum pour une couleur sombre
        return meanColor.val[0] < maxDarknessThreshold &&
                meanColor.val[1] < maxDarknessThreshold &&
                meanColor.val[2] < maxDarknessThreshold;
    }

    private Ellipse2D createEllipse(int x, int y, int diametre) {
        return new Ellipse2D.Double(
                x - (diametre / 2.0),
                y - (diametre / 2.0),
                diametre,
                diametre
        );
    }

}
