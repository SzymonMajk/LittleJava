import java.util.Random;
import java.util.Scanner;

/**
 * Holds current game board and provide interface for user to do his turn,
 * could use computer calculations to choose appropriate respond for player
 * move, checks when game should start or and using methods from board object.
 * First player is always user, second player is computer.
 *
 * Created by Szymon on 02.06.2017.
 */
public class Game {

    private Board gameBoard;

    private final int freePawnsSearchRange = 1;

    private final int minMaxDepth = 1;

    private static final int boardSize = 8;

    private int minMax(Board game, PawnCoordinates addPawn, int depth, boolean max) {
        if(max)
            game.setPawn(new Pawn(1),addPawn.x,addPawn.y);
        else
            game.setPawn(new Pawn(2),addPawn.x,addPawn.y);

        if(depth == 0) {
            return game.rank();
        } else {
            int bestCoordsScore;
            int tmpCoordsScore;

            if(max) {
                bestCoordsScore = Integer.MIN_VALUE;
            } else {
                bestCoordsScore = Integer.MAX_VALUE;
            }

            for(PawnCoordinates coords : game.freeCloseMoves(freePawnsSearchRange)) {
                tmpCoordsScore = minMax(game.copyBoard(),coords,depth-1,!max);
                if(max) {
                    if(tmpCoordsScore > bestCoordsScore)
                        bestCoordsScore = tmpCoordsScore;
                } else {
                    if(tmpCoordsScore < bestCoordsScore)
                        bestCoordsScore = tmpCoordsScore;
                }
            }
            return bestCoordsScore;
        }
    }

    /**
     * Estimates integer value for coordinates, using
     * min max algorithm and copy of current gameBoard.
     */
    private int rankComputerMove(Board tryBoard,PawnCoordinates coords) {
        return minMax(tryBoard.copyBoard(),coords,2*minMaxDepth,false);
    }

    /**
     * Find best possible move from near empty spaces, near mean
     * close to already set pawns.
     */
    private void computerSetPawnMinMax() {
        PawnCoordinates bestMoveCoordinates = null;
        int bestCoordsScore = Integer.MIN_VALUE;
        int tmpCoordsScore;

        for(PawnCoordinates coords : gameBoard.freeCloseMoves(freePawnsSearchRange)) {
            tmpCoordsScore = rankComputerMove(gameBoard,coords);
            if(tmpCoordsScore > bestCoordsScore) {
                bestMoveCoordinates = coords;
                bestCoordsScore = tmpCoordsScore;
            }
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

    private void userSetPawnChar() {
        Scanner userCoordinate = new Scanner(System.in);
        char x,y;
        while (true) {
            System.out.println("Set row for your Pawn:");
            x = userCoordinate.next().charAt(0);
            System.out.println("Set column for your Pawn:");
            y = userCoordinate.next().charAt(0);
            System.out.printf("%d, %d",x-65,y-65);
            if((x >= 'A' && x <= boardSize + 'A') || (y >= 'A' && y <= boardSize + 'A'))
                if(gameBoard.setPawn(new Pawn(1),x-65,y-65))
                    break;
                else
                    System.out.printf("Position is not free: %c, %c\n",x,y);
            else if((x >= 'a' && x <= boardSize + 'a') || (y >= 'a' && y <= boardSize + 'a'))
                if(gameBoard.setPawn(new Pawn(1),x-97,y-97))
                    break;
                else
                    System.out.printf("Position is not free: %c, %c\n",x,y);
            else
                System.out.printf("Out of board: %c, %c\n",x,y);
        }
    }

    /*private void userSetPawnNumbers() {
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

            if(gameBoard.setPawn(new Pawn(1),x-1,y-1))
                break;
            else
                System.out.printf("Inapropriate coordinates: %d, %d\n",x,y);
        }
    }*/

    private void proceedGame (int boardSize) {
        gameBoard = new Board(boardSize);
        boolean currentPlayerFirst = true;

        while(true) {
            if(gameBoard.endGame())
                break;

            if(currentPlayerFirst) {
                System.out.println("Player 1 turn:");
                userSetPawnChar();
                System.out.println(gameBoard);
            } else {
                System.out.println("Player 2 turn");
                System.out.println("Computer is thinking...");
                computerSetPawnMinMax();
                System.out.println(gameBoard);
            }

            currentPlayerFirst = !currentPlayerFirst;
        }
    }

    public static void main (String args[]) {
        new Game().proceedGame(boardSize);
    }
}
