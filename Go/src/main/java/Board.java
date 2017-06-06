import java.util.ArrayList;

/**
 * Deals with creation regarding to provided size, lets to change board states
 * by user or by computer algorithm, checks for winning or loosing condition.
 *
 * Created by Szymon on 02.06.2017.
 */
class Board {

    private int size;

    private boolean firstPlayer = true;

    private ArrayList<ArrayList<Pawn>> board = new ArrayList<ArrayList<Pawn>>();

    private ArrayList<PawnCoordinates> firstPlayerPawns = new ArrayList<PawnCoordinates>();

    private ArrayList<PawnCoordinates> secondPlayerPawns = new ArrayList<PawnCoordinates>();

    private int estimateFirstPlayerPoint(int x, int y) {
        int result = 0;
        /*TODO*/
        result = -x - y;
        return result;
    }

    private int estimateSecondPlayerPoint(int x, int y) {
        int result = 0;
        /*TODO*/
        result = x + y;
        return result;
    }

    private int computerRank() {
        int result = 0;
        for(PawnCoordinates coords : secondPlayerPawns)
            result += estimateSecondPlayerPoint(coords.x,coords.y);
        return result;
    }

    private int playerRank() {
        int result = 0;
        for(PawnCoordinates coords : firstPlayerPawns)
            result += estimateFirstPlayerPoint(coords.x,coords.y);
        return result;
    }

    private boolean checkUpSlant(int x, int y, int length) {
        if(firstPlayer) {
            for(int i = 0, j = 0; i < length; ++i, ++j)
                if(x-i < 0 || y+j >= board.size() ||
                        !board.get(x-i).get(y+j).isFirstPlayer())
                    return false;
        } else {
            for(int i = 0, j = 0; i < length; ++i, ++j)
                if(x-i < 0 || y+j >= board.size() ||
                        !board.get(x-i).get(y+j).isSecondPlayer())
                    return false;
        }
        return true;
    }

    private boolean checkDownSlant(int x, int y, int length) {
        if(firstPlayer) {
            for(int i = 0, j = 0; i < length; ++i, ++j)
                if(x+i >= board.size() || y+j >= board.size() ||
                        !board.get(x+i).get(y+j).isFirstPlayer())
                    return false;
        } else {
            for(int i = 0, j = 0; i < length; ++i, ++j)
                if(x+i >= board.size() || y+j >= board.size() ||
                        !board.get(x+i).get(y+j).isSecondPlayer())
                    return false;
        }
        return true;
    }

    private boolean checkRow(int x, int y, int length) {
        if(firstPlayer) {
            for(int i = 0; i < length; ++i)
                if(y+i >= board.size() ||
                        !board.get(x).get(y+i).isFirstPlayer())
                    return false;

        } else {
            for(int i = 0; i < length; ++i)
                if(y+i >= board.size() ||
                        !board.get(x).get(y+i).isSecondPlayer())
                    return false;
        }
        return true;
    }

    private boolean checkColumn(int x, int y, int length) {
        if(firstPlayer) {
            for(int i = 0; i < length; ++i)
                if(x+i >= board.size() ||
                        !board.get(x+i).get(y).isFirstPlayer())
                    return false;

        } else {
            for(int i = 0; i < length; ++i)
                if(x+i >= board.size() ||
                        !board.get(x+i).get(y).isSecondPlayer())
                    return false;
        }
        return true;
    }

    private boolean checkPointNeighbor(int x, int y, int length) {
        return (checkUpSlant(x,y,length) || checkDownSlant(x,y,length) ||
                checkRow(x,y,length) || checkColumn(x,y,length));
    }

    private boolean checkIfWin() {
        if(firstPlayer) {
            for(PawnCoordinates coords : firstPlayerPawns)
                if(checkPointNeighbor(coords.x,coords.y,5))
                    return true;
                return false;
        } else {
            for(PawnCoordinates coords : secondPlayerPawns)
                if(checkPointNeighbor(coords.x,coords.y,5))
                    return true;
                return false;
        }

    }

    private boolean checkIfLose() {
        if(firstPlayer) {
            for(PawnCoordinates coords : firstPlayerPawns)
                if(checkPointNeighbor(coords.x,coords.y,6))
                    return true;
            return false;
        } else {
            for(PawnCoordinates coords : secondPlayerPawns)
                if(checkPointNeighbor(coords.x,coords.y,6))
                    return true;
            return false;
        }
    }

    private boolean possibleMove() {
        for(ArrayList<Pawn> row : board) {
            for(Pawn p : row) {
                if(p.isFreeSpace())
                    return true;
            }
        }
        return false;
    }

    /**
     * Board must be square matrix, the size means then the length
     * of row or column. There are equal.
     *
     * @return Length of column or row, which is the same value.
     */
    int size() {
        return board.get(0).size();
    }

    /*TODO near, not all free*/
    ArrayList<PawnCoordinates> freeCloseMoves() {
        ArrayList<PawnCoordinates> freeMoves = new ArrayList<PawnCoordinates>();
        for(int i = 0; i < size(); ++i) {
            for(int j = 0; j < size(); ++j) {
                if(board.get(i).get(j).isFreeSpace())
                    freeMoves.add(new PawnCoordinates(i,j));
            }
        }
        return freeMoves;
    }

    /**
     * Inform if the first player is current player.
     *
     * @return True if first player turn, otherwise false.
     */
    boolean currentPlayerFirst() {
        return firstPlayer;
    }

    /**
     * Change first player to second and otherwise. Do not check for
     * win or loose conditions.
     */
    void switchPlayer() {
        firstPlayer = !firstPlayer;
    }

    int rank() {
        return computerRank() + playerRank();
    }

    boolean endGame() {
        if(checkIfLose()) {
            if(firstPlayer)
                System.out.print("First ");
            else
                System.out.print("Second ");
            System.out.println("player lost.");
            return true;
        } else if(checkIfWin()){
            if(firstPlayer)
                System.out.print("First ");
            else
                System.out.print("Second ");
            System.out.println("player won.");
            return true;
        } else if(!possibleMove()) {
            System.out.println("Draw!");
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

    Board(int size) {
        if(size < 5 || size > 19)
            this.size = 5;
        else
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
    int x;
    int y;

    PawnCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}