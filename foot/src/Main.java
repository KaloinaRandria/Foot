import javax.swing.*;
import affichage.Graph;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        // Lancement de l'application Swing dans le thread principal
        SwingUtilities.invokeLater(Graph::new);
    }
}