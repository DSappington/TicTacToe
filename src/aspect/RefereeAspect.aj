import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;

import gui.TicTacToeGame;
import player.BotPlayer;

@Aspect
public class RefereeAspect {
    
    @Pointcut("execution(* gui.TicTacToeGame.makeMove(..)) && args(row, col)")
    public void updatePointcut(int row, int col) {}

    @After("updatePointcut(row, col)")
    public void evaluateGameStatus(JoinPoint joinPoint, int row, int col ) {
    	
        System.out.println("Referee Aspect Checking Results");
        TicTacToeGame game = (TicTacToeGame) joinPoint.getTarget();
        game.getBoardUI().enableButtons();

        System.out.println("Current player before switch: " + game.getCurrentPlayer());

        if (game.checkWinner(row, col)) {
            game.endGame("Player " + game.getCurrentPlayer() + " wins!");
            game.switchPlayers();
        	            
        } else if (game.checkDraw()) {
            game.endGame("It's a draw!");
            
        } else {
        	game.switchPlayers();
        	
        }
        
        System.out.println("Current player before switch: " + game.getCurrentPlayer());

        if (game.getPlayerO() instanceof BotPlayer && game.getCurrentPlayer() == game.getPlayerO().getSymbol()) {
            game.makeBotMove();
            game.getBoardUI().disableButtons();

        }
            
        

    }
}