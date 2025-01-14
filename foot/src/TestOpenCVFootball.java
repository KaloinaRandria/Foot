import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

public class TestOpenCVFootball {
    static {
        // Chargement de la bibliothèque native OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // Chemin de l'image (modifie ce chemin avec l'emplacement réel de ton image)
        String imagePath = "E:\\GitHub\\Foot\\bleu.jpg";

        // Charger l'image
        Mat image = Imgcodecs.imread(imagePath);
        if (image.empty()) {
            System.out.println("Erreur : Impossible de charger l'image !");
            return;
        }

        // Afficher l'image originale
        HighGui.imshow("Image originale", image);

        // Convertir l'image en niveaux de gris
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Afficher l'image en niveaux de gris
        HighGui.imshow("Image en niveaux de gris", grayImage);

        // Appliquer un filtre de détection des bords (Canny)
        Mat edges = new Mat();
        Imgproc.Canny(grayImage, edges, 100, 200);

        // Afficher les bords détectés
        HighGui.imshow("Contours détectés", edges);

        // Attendre la fermeture des fenêtres
        HighGui.waitKey(0);
    }
}
