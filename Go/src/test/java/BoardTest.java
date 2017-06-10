import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Some tests for instances of game with one Pawn, two Pawns, three Pawns and special
 * configurations to show up "thinking" of estimation functions. Some tests for functions
 * finding free positions.
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
        Board testBoard = new Board(10);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,6);
        assertEquals(15,testBoard.rank());
        testBoard = new Board(11);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,6);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(12);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,7);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(13);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,8);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(14);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,9);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,10);
        assertEquals(0,testBoard.rank());

        testBoard = new Board(11);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),6,6);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(12);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),7,7);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(13);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),8,8);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(14);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),9,9);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),10,10);
        assertEquals(0,testBoard.rank());

        testBoard = new Board(11);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),6,5);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(12);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),7,5);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(13);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),8,5);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(14);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),9,5);
        assertEquals(0,testBoard.rank());
        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),10,5);
        assertEquals(0,testBoard.rank());
    }

    @Test
    public void rankThreePawn() throws Exception {
        Board testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,6);
        testBoard.setPawn(new Pawn(2),5,7);
        assertEquals(156,testBoard.rank());

        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),5,6);
        testBoard.setPawn(new Pawn(2),7,7);
        assertEquals(186,testBoard.rank());
    }

    @Test
    public void rankSpecialConfigurations() throws Exception {
        Board testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(2),6,6);
        testBoard.setPawn(new Pawn(2),7,7);
        testBoard.setPawn(new Pawn(2),8,8);
        testBoard.setPawn(new Pawn(1),1,1);
        testBoard.setPawn(new Pawn(1),1,0);
        testBoard.setPawn(new Pawn(1),0,1);
        testBoard.setPawn(new Pawn(1),1,1);
        assertEquals(566,testBoard.rank());

        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(2),6,6);
        testBoard.setPawn(new Pawn(2),7,7);
        testBoard.setPawn(new Pawn(2),8,8);
        testBoard.setPawn(new Pawn(1),0,1);
        testBoard.setPawn(new Pawn(1),0,2);
        testBoard.setPawn(new Pawn(1),0,3);
        testBoard.setPawn(new Pawn(1),0,4);
        assertEquals(300,testBoard.rank());

        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(2),6,6);
        testBoard.setPawn(new Pawn(2),7,7);
        testBoard.setPawn(new Pawn(2),0,0);
        testBoard.setPawn(new Pawn(1),0,1);
        testBoard.setPawn(new Pawn(1),0,2);
        testBoard.setPawn(new Pawn(1),0,3);
        testBoard.setPawn(new Pawn(1),0,4);
        assertEquals(91,testBoard.rank());

        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),6,6);
        testBoard.setPawn(new Pawn(1),7,7);
        testBoard.setPawn(new Pawn(1),8,8);
        testBoard.setPawn(new Pawn(2),9,9);
        testBoard.setPawn(new Pawn(2),10,10);
        assertEquals(26,testBoard.rank());

        testBoard = new Board(15);
        testBoard.setPawn(new Pawn(2),5,5);
        testBoard.setPawn(new Pawn(1),6,6);
        testBoard.setPawn(new Pawn(1),7,7);
        testBoard.setPawn(new Pawn(1),8,8);
        testBoard.setPawn(new Pawn(1),7,8);
        testBoard.setPawn(new Pawn(2),9,9);
        testBoard.setPawn(new Pawn(2),10,10);
        testBoard.setPawn(new Pawn(2),12,13);
        assertEquals(-63,testBoard.rank());
    }

    @Test
    public void findCloseMoves() throws Exception {
        Board testBoard = new Board(15);
        assertEquals(225,testBoard.freeCloseMoves().size());
        assertEquals(0,testBoard.freeCloseMoves(2).size());

        testBoard.setPawn(new Pawn(1),9,9);
        assertEquals(224,testBoard.freeCloseMoves().size());
        assertEquals(24,testBoard.freeCloseMoves(2).size());

        testBoard.setPawn(new Pawn(2),8,9);
        assertEquals(223,testBoard.freeCloseMoves().size());
        assertEquals(10,testBoard.freeCloseMoves(1).size());

        assertEquals(28,testBoard.freeCloseMoves(-1).size());
        assertEquals(28,testBoard.freeCloseMoves(0).size());
        assertEquals(28,testBoard.freeCloseMoves(15).size());
        assertEquals(28,testBoard.freeCloseMoves(16).size());
    }
}