package player;
import java.util.Random;

public class BotPlayer extends Player{
    private Difficulty difficulty;

    public BotPlayer(char symbol, Difficulty difficulty) {
        super(symbol);
        this.difficulty = difficulty;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int[] getMove(char[][] board) {
        int[] move = new int[2];
        System.out.println(this.difficulty);

        switch (this.difficulty) {
            case EASY:
                move = makeEasyMove(board);
                break;
            case HARD:
                move = makeHardMove(board);
                break;
            case EXPERT:
                move = makeExpertMove(board);
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + this.difficulty);
        }

        return move;
    }

    // Easy mode select any available row or column
    private int[] makeEasyMove(char[][] board){
        int[] move = new int[2];
        Random random = new Random();
        // Choose a random move from the available empty spaces
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);

            if (board[row][col] == ' ') {
                move[0] = row;
                move[1] = col;
                break;
            }
        }

        return move;
    }

    //Hard mode does a basic selection option. For example, can the bot or opponent win?
    private int[] makeHardMove(char[][] board){
        int[] move = new int[2];
        Random random = new Random();

        // Check if the player can win in the next move
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    board[row][col] = symbol;
                    if (checkWinner(board, symbol)) {
                        move[0] = row;
                        move[1] = col;
                        board[row][col] = ' ';
                        return move;
                    }
                    board[row][col] = ' ';
                }
            }
        }

        // Check if the opponent can win in the next move and block them
        char opponentSymbol = (symbol == 'X') ? 'O' : 'X';
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    board[row][col] = opponentSymbol;
                    if (checkWinner(board, opponentSymbol)) {
                        move[0] = row;
                        move[1] = col;
                        board[row][col] = ' ';
                        return move;
                    }
                    board[row][col] = ' ';
                }
            }
        }

        // Choose a random move from the available empty spaces
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);

            if (board[row][col] == ' ') {
                move[0] = row;
                move[1] = col;
                break;
            }
        }

        return move;
    }

    // Expert mode implements a minimax algorithm which provides a decision-making strategy and score between the two players its future/present moves
    private int[] makeExpertMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[2];

        // Iterate through all cells
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                // Check cell emptiness
                if (board[row][col] == ' ') {
                    // Make the move
                    board[row][col] = symbol;

                    // Compute minimax for this move
                    int score = minimax(board, 0, false);

                    // Undo the move
                    board[row][col] = ' ';

                    // If the new move's score is greater than the best, update bestScore and move
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = row;
                        move[1] = col;
                    }
                }
            }
        }

        return move;
    }


    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        char opponentSymbol = (symbol == 'X') ? 'O' : 'X';

        // Base cases - if the game is over, score the board
        if (checkWinner(board, symbol)) return 10 - depth;
        if (checkWinner(board, opponentSymbol)) return depth - 10;
        if (checkDraw(board)) return 0;

        // Recursive case - maximize your gains or minimize the opponent's gains
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == ' ') {
                        board[row][col] = symbol;
                        int score = minimax(board, depth + 1, false);
                        board[row][col] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == ' ') {
                        board[row][col] = opponentSymbol;
                        int score = minimax(board, depth + 1, true);
                        board[row][col] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }

            return bestScore;
        }
    }

    private boolean checkDraw(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }



    private boolean checkWinner(char[][] board, char playerSymbol) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == playerSymbol && board[row][1] == playerSymbol && board[row][2] == playerSymbol) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == playerSymbol && board[1][col] == playerSymbol && board[2][col] == playerSymbol) {
                return true;
            }
        }

        // Check diagonals
        if ((board[0][0] == playerSymbol && board[1][1] == playerSymbol && board[2][2] == playerSymbol) ||
                (board[0][2] == playerSymbol && board[1][1] == playerSymbol && board[2][0] == playerSymbol)) {
            return true;
        }

        return false;
    }
}
