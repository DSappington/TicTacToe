package player;

public class Player {
    protected char symbol;

    public Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public int[] getMove(char[][] board) {
        return new int[]{-1, -1}; // This should never be called for a basic player.
    }


}