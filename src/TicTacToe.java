import javax.swing.SwingUtilities;

import gui.TicTacToeGame;

public class TicTacToe {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            TicTacToeGame game = new TicTacToeGame();
            game.initialize();
        });
	}

}
