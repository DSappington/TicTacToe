package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeBoard extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton[][] buttons;
    private TicTacToeGame game;
    private JButton changeDifficultyButton;

    public TicTacToeBoard(TicTacToeGame game) {
        this.game = game;
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        initializeButtons(buttonPanel);


        add(buttonPanel, BorderLayout.CENTER);

    }

    private void initializeButtons(JPanel buttonPanel) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
                button.addActionListener(this);
                buttons[row][col] = button;
                buttonPanel.add(button);
            }
        }
    }

    public void updateButton(int row, int col, char symbol) {
        buttons[row][col].setText(String.valueOf(symbol));
        buttons[row][col].setEnabled(false);
    }

    public void clearButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
    }
    
    public void disableButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
            	if(buttons[row][col].getText() == "") {
                    buttons[row][col].setEnabled(false);
            	}
            }
        }
    }
    
    
    public void enableButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
            	if(buttons[row][col].getText() == "") {
                    buttons[row][col].setEnabled(true);
            	}
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeDifficultyButton) {
            game.changeDifficulty();
        } else {
            JButton button = (JButton) e.getSource();

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (button == buttons[row][col]) {
                        game.makeMove(row, col);
                        return;
                    }
                }
            }
        }
    }
}
