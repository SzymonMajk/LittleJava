import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Deals with creation, let to change board states by user
 * or by computer algorithm, checks for winning or loosing condition.
 *
 * Created by Szymon on 02.06.2017.
 */
public class Board {

    int size;

    int winLength = 5;

    boolean firstPlayer = true;

    ArrayList<ArrayList<Pawn>> board = new ArrayList<ArrayList<Pawn>>();

    ArrayList<PawnCoordinates> firstPlayerPawns = new ArrayList<PawnCoordinates>();

    ArrayList<PawnCoordinates> secondPlayerPawns = new ArrayList<PawnCoordinates>();

    boolean checkUpSlant(int x, int y) {
        if(firstPlayer) {
            for(int i = 0, j = 0; i < winLength; ++i, ++j)
                if(x-i < 0 || y+j >= board.size() ||
                        !board.get(x-i).get(y+j).isFirstPlayer())
                    return false;
        } else {
            for(int i = 0, j = 0; i < winLength; ++i, ++j)
                if(x-i < 0 || y+j >= board.size() ||
                        !board.get(x-i).get(y+j).isSecondPlayer())
                    return false;
        }
        return true;
    }

    boolean checkDownSlant(int x, int y) {
        if(firstPlayer) {
            for(int i = 0, j = 0; i < winLength; ++i, ++j)
                if(x+i >= board.size() || y+j >= board.size() ||
                        !board.get(x+i).get(y+j).isFirstPlayer())
                    return false;
        } else {
            for(int i = 0, j = 0; i < winLength; ++i, ++j)
                if(x+i >= board.size() || y+j >= board.size() ||
                        !board.get(x+i).get(y+j).isSecondPlayer())
                    return false;
        }
        return true;
    }

    boolean checkRow(int x, int y) {
        if(firstPlayer) {
            for(int i = 0; i < winLength; ++i)
                if(y+i >= board.size() ||
                        !board.get(x).get(y+i).isFirstPlayer())
                    return false;

        } else {
            for(int i = 0; i < winLength; ++i)
                if(y+i >= board.size() ||
                        !board.get(x).get(y+i).isSecondPlayer())
                    return false;
        }
        return true;
    }

    boolean checkColumn(int x, int y) {
        if(firstPlayer) {
            for(int i = 0; i < winLength; ++i)
                if(x+i >= board.size() ||
                        !board.get(x+i).get(y).isFirstPlayer())
                    return false;

        } else {
            for(int i = 0; i < winLength; ++i)
                if(x+i >= board.size() ||
                        !board.get(x+i).get(y).isSecondPlayer())
                    return false;
        }
        return true;
    }

    private boolean checkIfPointWin(int x, int y) {
        if(checkUpSlant(x,y) || checkDownSlant(x,y) ||
                checkRow(x,y) || checkColumn(x,y))
            return true;
        return false;
    }

    private boolean checkIfWin() {
        if(firstPlayer) {
            for(PawnCoordinates coords : firstPlayerPawns)
                if(checkIfPointWin(coords.x,coords.y))
                    return true;
                return false;
        } else {
            for(PawnCoordinates coords : secondPlayerPawns)
                if(checkIfPointWin(coords.x,coords.y))
                    return true;
                return false;
        }

    }

    /**
     * Board must be square matrix, the size means then the length
     * of row or column. There are equal.
     *
     * @return Length of column or row, which is the same value.
     */
    public int size() {
        return board.get(0).size();
    }

    /**
     *
     *
     * @return
     */
    public boolean currentPlayerFirst() {
        return firstPlayer;
    }

    /**
     *
     */
    public void switchPlayer() {
        firstPlayer = !firstPlayer;
    }

    boolean endGame() {
        if(checkIfWin()){
            if(firstPlayer)
                System.out.print("First ");
            else
                System.out.print("Second ");
            System.out.println("player won.");
            return true;
        }
        return false;
    }

    boolean setPawn(Pawn p, int x, int y) {
        if(x < 0 || y < 0 || x > size || y > size)
            return false;

        if(!board.get(x).get(y).isFreeSpace())
            return false;
        board.get(x).set(y,p);
        if(firstPlayer)
            firstPlayerPawns.add(new PawnCoordinates(x,y));
        else
            secondPlayerPawns.add(new PawnCoordinates(x,y));
        return true;
    }

    /**
     * Create command line instance of board, allows user to check
     * his actual turn possibilities. Uses standard output to print
     * created String.
     *
     * @return String representation of current board situation.
     */
    @Override
    public String toString() {

        StringBuilder boardBuilder = new StringBuilder();
        char i = 'A';

        for(ArrayList<Pawn> row : board) {
            boardBuilder.append(i++);
            boardBuilder.append("| ");
            for(Pawn p : row) {
                boardBuilder.append(p);
                boardBuilder.append(" | ");
            }
            boardBuilder.append("\n");
        }

        for(i = 'A'; i < 'A'+board.size(); ++i) {
            boardBuilder.append(" | " + i);
        }
        boardBuilder.append(" |\n");

        return boardBuilder.toString();
    }

    /**
     * trr
     *
     * @param size
     */
    Board(int size) {
        this.size = size;
        for(int i = 0; i < size; ++i) {
            ArrayList<Pawn> row = new ArrayList<Pawn>();
            board.add(row);
            for(int j = 0; j < size; ++j) {
                row.add(new Pawn(0));
            }
        }
    }
}

class PawnCoordinates {
    public int x;
    public int y;

    PawnCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}