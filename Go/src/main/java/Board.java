import java.util.ArrayList;

/**
 * Deals with creation regarding to provided size, lets to change board states
 * by user or by computer algorithm, rank current board state using integer,
 * checks for winning or loosing condition.
 *
 * Created by Szymon on 02.06.2017.
 */
class Board {

    private ArrayList<ArrayList<Pawn>> board = new ArrayList<ArrayList<Pawn>>();

    private ArrayList<PawnCoordinates> firstPlayerPawns = new ArrayList<PawnCoordinates>();

    private ArrayList<PawnCoordinates> secondPlayerPawns = new ArrayList<PawnCoordinates>();

    private int size;

    private final int freePointEstimation = 5;

    private final int familiarNextPointMultiplication = 2;

    private final int enemyNextPointRetribution = 10;

    private boolean firstPlayer = true;

    private boolean playerPawnInRange(int x, int y, int range) {
        for(int i = x - range; i <= x + range; ++i) {
            for(int j = y - range; j <= y + range; ++j) {
                if(i < 0 || j < 0 || i >= size() || j >= size())
                    continue;
                if(!board.get(i).get(j).isFreeSpace())
                    return true;
            }
        }
        return false;
    }

    private int estimateRow(int x, int y, boolean currentPlayerFirst) {
        int result = 1;

        for(int i = y + 1; i < y + 5; ++i) {
            if(i >= size())
                break;
            Pawn tmp = board.get(x).get(i);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        for(int i = y - 1; i > y - 5; --i) {
            if(i < 0)
                break;
            Pawn tmp = board.get(x).get(i);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        return result;
    }

    private int estimateColumn(int x, int y, boolean currentPlayerFirst) {
        int result = 1;

        for(int i = x + 1; i < x + 5; ++i) {
            if(i >= size())
                break;
            Pawn tmp = board.get(i).get(y);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        for(int i = x - 1; i > x - 5; --i) {
            if (i < 0)
                break;
            Pawn tmp = board.get(i).get(y);
            if (tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if (tmp.isFirstPlayer()) {
                if (currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if (!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        return result;
    }

    private int estimateSlantToRightDown(int x, int y, boolean currentPlayerFirst) {
        int result = 1;

        for(int i = x + 1, j = y + 1; i < x + 5; ++i, ++j) {
            if(i >= size() || j >= size())
                break;
            Pawn tmp = board.get(i).get(j);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        for(int i = x - 1, j = y - 1; i > x - 5; --i, --j) {
            if(i < 0 || j < 0)
                break;
            Pawn tmp = board.get(i).get(j);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        return result;
    }

    private int estimateSlantToRightUp(int x, int y, boolean currentPlayerFirst) {
        int result = 1;

        for(int i = x - 1, j = y + 1; i > x - 5; --i, ++j) {
            if(i < 0 || j >= size())
                break;
            Pawn tmp = board.get(i).get(j);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        for(int i = x + 1, j = y - 1; i < x + 5; ++i, --j) {
            if(i >= size() || j < 0)
                break;
            Pawn tmp = board.get(i).get(j);
            if(tmp.isFreeSpace()) {
                result += freePointEstimation;
            } else if(tmp.isFirstPlayer()) {
                if(currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            } else {
                if(!currentPlayerFirst) {
                    result *= familiarNextPointMultiplication;
                } else {
                    result -= enemyNextPointRetribution;
                }
            }
        }

        return result;
    }

    private int estimatePoint(int x, int y, boolean currentPlayerFirst) {
        int result = 0;
        result += estimateRow(x,y,currentPlayerFirst);
        result += estimateColumn(x,y,currentPlayerFirst);
        result += estimateSlantToRightDown(x,y,currentPlayerFirst);
        result += estimateSlantToRightUp(x,y,currentPlayerFirst);
        return result;
    }

    private int computerRank() {
        int result = 0;
        for(PawnCoordinates coords : secondPlayerPawns)
            result += estimatePoint(coords.x,coords.y,false);
        return result;
    }

    private int playerRank() {
        int result = 0;
        for(PawnCoordinates coords : firstPlayerPawns)
            result += estimatePoint(coords.x,coords.y,true);
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
     * of row or column. Its length must be equal.
     *
     * @return Length of column or row, which is the same value.
     */
    int size() {
        return board.get(0).size();
    }

    /**
     * Look through game board to find coordinates of all free positions.
     *
     * @return list of coordinates of free positions on board.
     */
    ArrayList<PawnCoordinates> freeCloseMoves() {
        ArrayList<PawnCoordinates> allFreeMoves = new ArrayList<PawnCoordinates>();
        for(int i = 0; i < size(); ++i) {
            for(int j = 0; j < size(); ++j) {
                if(board.get(i).get(j).isFreeSpace())
                    allFreeMoves.add(new PawnCoordinates(i,j));
            }
        }
        return allFreeMoves;
    }

    /**
     * Look through game board to find coordinates of all free positions,
     * in range provided in parameter. In case of negative range or range
     * out of board size, default value of 2 set.
     *
     * @param range maximal distance beetwen player pawn on board, if provided
     *              number is invalid, default value set.
     * @return list of coordinates of free positions on board in range provided
     *          in parameter.
     */
    ArrayList<PawnCoordinates> freeCloseMoves(int range) {
        if(range < 1 || range >= size())
            range = 2;
        ArrayList<PawnCoordinates> freeMoves = new ArrayList<PawnCoordinates>();
        for(int i = 0; i < size(); ++i) {
            for(int j = 0; j < size(); ++j) {
                if(board.get(i).get(j).isFreeSpace() && playerPawnInRange(i,j,range))
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

    /**
     * Estimates current computer situation depends on gameboard structure.
     *
     * @return number which value show if the current board is good or bad for
     *      computer player. The bigger number, the better computer situation.
     */
    int rank() {
        return computerRank() - playerRank();
    }

    /**
     * Check loosing condition for current player if player lost, then return true
     * and inform about his loose on standard output, otherwise check win condition,
     * if player won, return true and inform about winning on standard output, at the
     * end check if player could make any possible move, if he can't do any move, the
     * game end up draw, function return true and inform on standard output, otherwise
     * return false and lets continue game.
     *
     * @return true if winning, loosing or draw condition is true, otherwise
     *      false.
     */
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

    /**
     * Change state of current Board by adding additional Pawn on board,
     * color and coordinates are specified by parameters if any of them
     * is invalid, false returns, informs function caller should check
     * his parameters then.
     *
     * @param p color of Pawn, specifing player.
     * @param x index of column for Pawn.
     * @param y index of row for Pawn.
     * @return true if Pawn set in free space if place was not free or any
     *          parameter was invalid false returned.
     */
    boolean setPawn(Pawn p, int x, int y) {
        if(p.isFreeSpace() || x < 0 || y < 0 || x > size || y > size)
            return false;

        if(!board.get(x).get(y).isFreeSpace())
            return false;
        board.get(x).set(y,p);
        if(p.isFirstPlayer())
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
     * Initialize starting game board structure with size depends on parameter, if
     * parameter is inappropriate set default value.
     *
     * @param size user defined board size if value is inappropriate default
     *             value set.
     */
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