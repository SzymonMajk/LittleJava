import java.util.Random;
import java.util.Scanner;

/**
 * Holds current game board and provide interface for user to do his turn,
 * could use computer calculations to choose appropriate respond for player
 * move, checks when game should start or and using methods from board object.
 *
 * Created by Szymon on 02.06.2017.
 */
public class Game {

    Board gameBoard;

    /*TODO minmax for computer*/

    private void computerSetPawn() {
        Random generator = new Random();
        while(!gameBoard.setPawn(new Pawn(2),
                generator.nextInt() % gameBoard.size(),
                generator.nextInt() % gameBoard.size())) {
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

    void proceedGame (int boardSize) {
        gameBoard = new Board(boardSize);

        while(!gameBoard.endGame()) {
            if(gameBoard.currentPlayerFirst()) {
                System.out.println("Player 1 turn:");
                userSetPawn();
                System.out.println(gameBoard);
            } else {
                System.out.println("Player 2 turn");
                System.out.println("Random choose!");
                computerSetPawn();
                System.out.println(gameBoard);
            }

            gameBoard.switchPlayer();
        }
    }

    public static void main (String args[]) {
        new Game().proceedGame(7);
    }
}
