package minesim;

import java.io.File;

import javax.swing.*;

public class OpenGame {
    private static OpenGame instance = null;

    public OpenGame() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("/Windows"));

        int result = jFileChooser.showOpenDialog(new JFrame());

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }

    public static OpenGame getInstance() {
        if (instance == null) {
            instance = new OpenGame();
        }
        return instance;
    }

    public static void main(String[] args) {

    }
}