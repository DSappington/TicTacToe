import javax.swing.*;

import gui.TicTacToeGame;



public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToeGame game = new TicTacToeGame();
            game.initialize();
        });
    }
}