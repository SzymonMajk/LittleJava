import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Some tests for instance of game with one Pawn and some configurations of Two Pawns.
 *
 * Created by Szymon on 07.06.2017.
 */
public class BoardTest {
    @Test
    public void rankOnePawn() throws Exception {
        Board testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),5,5);
        assertEquals(164,testBoard.rank());

        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),1,1);
        assertEquals(89,testBoard.rank());
        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),1,8);
        assertEquals(89,testBoard.rank());
        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),8,1);
        assertEquals(89,testBoard.rank());
        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),8,8);
        assertEquals(89,testBoard.rank());

        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),0,0);
        assertEquals(64,testBoard.rank());
        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),0,9);
        assertEquals(64,testBoard.rank());
        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),9,0);
        assertEquals(64,testBoard.rank());
        testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),9,9);
        assertEquals(64,testBoard.rank());
    }

    @Test
    public void rankTwoPawn() throws Exception {
        /*TODO some combinations*/

        Board testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,6);
        assertEquals(1,testBoard.rank());
    }
}