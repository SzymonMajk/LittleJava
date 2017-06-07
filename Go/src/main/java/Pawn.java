/**
 * Encapsulate logic for single pawn on board, could check
 * if current place on board is free.
 *
 * Created by Szymon on 02.06.2017.
 */
class Pawn {

    private int color;

    boolean isFreeSpace() {
        return (color != 1 && color != 2);
    }

    boolean isFirstPlayer() { return color == 1; }

    boolean isSecondPlayer() { return color == 2; }

    /**
     * Create safe Pawn object, which could be set in three different
     * states, for both players and for empty Pawn.
     *
     * @param color means: <ol>
     *              <li>1 -> first player color</li>
     *              <li>2 -> computer player color</li>
     *              <li>other -> free space</li></ol>
     */
    Pawn(int color) {
        switch (color) {
            case 1 : this.color = color; break;
            case 2 : this.color = color; break;
        }
    }

    /**
     * Puts x or o character if user or computer have this pawn otherwise
     * puts empty space.
     *
     * @return String representation of player color player or white character
     *      if Pawn space is free.
     */
    @Override
    public String toString() {
        switch (color) {
            case 1 : return "x";
            case 2 : return "o";
            default : return " ";
        }
    }
}