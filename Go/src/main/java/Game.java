import java.util.Random;
import java.util.Scanner;

/**
 * Holds current game board and provide interface for user to do his turn,
 * could use computer calculations to choose appropriate respond for player
 * move, checks when game should start or and using methods from board object.
 * First player is always user, second player is computer in assumption.
 *
 * Created by Szymon on 02.06.2017.
 */
public class Game {

    private Board gameBoard;

    private final int freePawnsSearchRange = 2;

    /*TODO minmax for computer*/

    /**
     * Estimates integer value for coordinates, using
     * min max algorithm and current gameBoard.
     */
    private int rankComputerMove(PawnCoordinates coors) {
        /*TODO!!!
        * Create new Board for min max, from existing Board
        * Estimate all possible moves of new Board choosing min
        * Estimate all possible moves of every created Board choosing max
        * ...
        * return sum of the best path from root
        * in advanced use alfa beta to cut worse subtree than current
        *
        * But first estimation of board state needed
        * */
        return coors.x + coors.y + gameBoard.rank();
    }

    /**
     * Find best possible move from near empty spaces, near mean
     * close to already set pawns.
     */
    private void computerSetPawnMinMax() {
        PawnCoordinates bestMoveCoordinates = null;
        int bestCoordsScore = Integer.MIN_VALUE;

        for(PawnCoordinates coords : gameBoard.freeCloseMoves(freePawnsSearchRange)) {
            if(rankComputerMove(coords) > bestCoordsScore)
                bestMoveCoordinates = coords;
        }
        if(bestMoveCoordinates == null)
            computerSetPawnRandom();
        else
            gameBoard.setPawn(
                    new Pawn(2),bestMoveCoordinates.x,bestMoveCoordinates.y);
    }

    /**
     * If impossible to choose with MinMax
     */
    private void computerSetPawnRandom() {
        Random generator = new Random();
        while(true) {
            if(gameBoard.setPawn(new Pawn(2),
                    generator.nextInt() % gameBoard.size(),
                    generator.nextInt() % gameBoard.size()))
                break;
        }
    }

    private void userSetPawn() {
        Scanner userCoordinate = new Scanner(System.in);
        int x,y;
        while(true) {
            while (true) {
                System.out.println("Set row for your Pawn:");
                try {
                    x = Integer.parseInt(userCoordinate.next());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Input must be digit!");
                }
            }

            while (true) {
                System.out.println("Set column for your Pawn:");
                try {
                    y = Integer.parseInt(userCoordinate.next());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Input must be digit!");
                }
            }

            if(gameBoard.setPawn(new Pawn(1),x,y))
                break;
            else
                System.out.printf("Inapropriate coordinates: %d, %d\n",x,y);
        }
    }

    private void proceedGame (int boardSize) {
        gameBoard = new Board(boardSize);

        while(!gameBoard.endGame()) {
            if(gameBoard.currentPlayerFirst()) {
                System.out.println("Player 1 turn:");
                userSetPawn();
                System.out.println(gameBoard);
            } else {
                System.out.println("Player 2 turn");
                System.out.println("Random choose!");
                computerSetPawnMinMax();
                System.out.println(gameBoard);
            }

            gameBoard.switchPlayer();
        }
    }

    public static void main (String args[]) {
        new Game().proceedGame(7);
    }
}
