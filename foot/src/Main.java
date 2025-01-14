import affichage.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        SwingUtilities.invokeLater(() -> {
            GamePanel frame = new GamePanel();
            frame.setVisible(true);
        });
        // Lancement de l'application Swing dans le thread principal
    }
}