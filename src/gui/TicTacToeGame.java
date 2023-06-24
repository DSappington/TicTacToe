package gui;

import javax.swing.*;

import player.Difficulty;
import player.Player;
import player.BotPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToeGame {
    private char[][] board;
    private char currentPlayer;

    private Player playerX;
    private Player playerO;
    private TicTacToeBoard boardUI;

    private JLabel scoreLabel;

    private int playerXWins = 0;
    private int playerOWins = 0;
    private int draws = 0;

    private final int BOT_DELAY = 1000;

    public TicTacToeGame() {
        startGame();
    }

    public void initialize() {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("Start New Game");
        JMenuItem resetGameItem = new JMenuItem("Reset Game");

        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearBoard();
                currentPlayer = 'X';
                startGame();
            }
        });

        resetGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearBoard();
            }
        });


        gameMenu.add(newGameItem);
        gameMenu.add(resetGameItem);


        if(this.getPlayerO() instanceof BotPlayer){
            JMenuItem changeDifficultyGameItem = new JMenuItem("Change Difficulty");

            changeDifficultyGameItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeDifficulty();
                }
            });

            gameMenu.add(changeDifficultyGameItem);
        }


        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        boardUI = new TicTacToeBoard(this);
        frame.add(boardUI, BorderLayout.CENTER);

        scoreLabel = new JLabel();
        updateScoreLabel();
        frame.add(scoreLabel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    public void askForNumberOfPlayers() {
        Object[] options = {"1 Player", "2 Players"};
        int chosenOption = JOptionPane.showOptionDialog(
                boardUI,
                "Select the number of players",
                "Number of Players",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Option indexes start from 0, so 0 refers to "1 Player" and 1 refers to "2 Players"
        if (chosenOption == 0) {
            Difficulty chosenDifficulty = askForDifficulty();
            playerO = new BotPlayer('O', chosenDifficulty);
        } else {
            playerO = new Player('O');
        }
    }

    public Difficulty askForDifficulty() {
        Difficulty[] options = Difficulty.values();
        Difficulty chosenDifficulty = (Difficulty) JOptionPane.showInputDialog(boardUI, "Select the difficulty level", "Difficulty Level", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        return chosenDifficulty;
    }

    public void changeDifficulty() {
        Difficulty newDifficulty = askForDifficulty();
        if (playerO instanceof BotPlayer && newDifficulty != null) {
            BotPlayer botPlayer = (BotPlayer) playerO;
            botPlayer.setDifficulty(newDifficulty);
        }
    }



    public void makeMove(int row, int col) {
        board[row][col] = currentPlayer;
        boardUI.updateButton(row, col, currentPlayer);
    }

    public boolean checkWinner(int row, int col) {
        // Check row
        if (board[row][0] == board[row][1] && board[row][1] == board[row][2] && board[row][0] != ' ') {
            return true;
        }

        // Check column
        if (board[0][col] == board[1][col] && board[1][col] == board[2][col] && board[0][col] != ' ') {
            return true;
        }

        // Check diagonal
        if (row == col && board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            return true;
        }

        // Check anti-diagonal
        if (row + col == 2 && board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            return true;
        }

        return false;
    }

    public boolean checkDraw() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void switchPlayers() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public void makeBotMove() {
        final int[] move = playerO.getMove(board);

        // Generate a random delay between 0 and 2000 milliseconds
        Random rand = new Random();
        int delay = rand.nextInt(BOT_DELAY);  // Note: the argument for nextInt is exclusive

        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                makeMove(move[0], move[1]);
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void startGame(){
        board = new char[3][3];
        currentPlayer = 'X';
        playerX = new Player('X');
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        askForNumberOfPlayers();
    }

    public void endGame(String message) {
        JOptionPane.showMessageDialog(boardUI, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        updateScore(message);
        updateScoreLabel();

        clearBoard();
    }

    private void clearBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
        boardUI.clearButtons();
    }

    private void updateScore(String message) {
        if (message.contains("Player X wins")) {
            playerXWins++;
        } else if (message.contains("Player O wins")) {
            playerOWins++;
        } else {
            draws++;
        }
    }

    private void updateScoreLabel() {
        String scoreText = String.format("Player 1: %d wins     |     Player 2: %d wins     |     Draws: %d", playerXWins, playerOWins, draws);
        scoreLabel.setText(scoreText);
    }

    // Setters and Getters

    public void setPlayerXWins(int playerXWins) {
        this.playerXWins = playerXWins;
    }

    public int getPlayerXWins() {
        return playerXWins;
    }

    public void setPlayerOWins(int playerOWins) {
        this.playerOWins = playerOWins;
    }

    public int getPlayerOWins() {
        return playerOWins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getDraws() {
        return draws;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char[][] getBoard() {
        return board;
    }

    public TicTacToeBoard getBoardUI() {
        return boardUI;
    }


    public void setPlayerX(Player playerX) {
        this.playerX = playerX;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public void setPlayerO(Player playerO) {
        this.playerO = playerO;
    }

    public Player getPlayerO() {
        return playerO;
    }
}
