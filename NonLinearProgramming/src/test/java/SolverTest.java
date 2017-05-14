import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Szymon on 14.05.2017.
 */
public class SolverTest {

    StringBuilder testBuilderSimplex = new StringBuilder();
    StringBuilder testBuilderGeo = new StringBuilder();

    @Before
    public void setUp() throws Exception {
        testBuilderSimplex.append("3\n");
        testBuilderSimplex.append("10 10 10\n");
        testBuilderSimplex.append("5 4 3\n");
        testBuilderSimplex.append("1 1 1\n");
        testBuilderSimplex.append("y\n");
        testBuilderSimplex.append("3\n");
        testBuilderSimplex.append("2 3 1\n");
        testBuilderSimplex.append("1 1 1\n");
        testBuilderSimplex.append("5\n");
        testBuilderSimplex.append("n\n");
        testBuilderSimplex.append("4 1 2\n");
        testBuilderSimplex.append("1 1 1\n");
        testBuilderSimplex.append("11\n");
        testBuilderSimplex.append("n\n");
        testBuilderSimplex.append("3 4 2\n");
        testBuilderSimplex.append("1 1 1\n");
        testBuilderSimplex.append("8\n");
        testBuilderSimplex.append("n\n");

        testBuilderGeo.append("4\n");
        testBuilderGeo.append("40000 40000 10000 10000\n");
        testBuilderGeo.append("4 6 3 12\n");
        testBuilderGeo.append("1 1 1 1\n");
        testBuilderGeo.append("y\n");
        testBuilderGeo.append("2\n");
        testBuilderGeo.append("1 2 1.5 6\n");
        testBuilderGeo.append("1 1 1 1\n");
        testBuilderGeo.append("90000\n");
        testBuilderGeo.append("n\n");
        testBuilderGeo.append("2 2 1.5 4\n");
        testBuilderGeo.append("1 1 1 1\n");
        testBuilderGeo.append("120000\n");
        testBuilderGeo.append("n\n");
    }

    @Test
    public void proceedMonteCarlo() throws Exception {
        Solver s1 = new Solver();
        Solver s2 = new Solver();
        Scanner sc1 = new Scanner(testBuilderSimplex.toString());
        Scanner sc2 = new Scanner(testBuilderGeo.toString());
        Double[] bestTestCoords;

        s1.inputData(sc1);
        bestTestCoords = s1.proceedMonteCarlo().getCoordinates();
        assertEquals(2.0,bestTestCoords[0],0.00000001);
        assertEquals(0.0,bestTestCoords[1],0.00000001);
        assertEquals(1.0,bestTestCoords[2],0.00000001);
        /*for(Double d : bestTestCoords)
            System.out.println(d);*/

        s2.inputData(sc2);
        bestTestCoords = s2.proceedMonteCarlo().getCoordinates();
        assertEquals(30000.0,bestTestCoords[0],3000);
        assertEquals(30000.0,bestTestCoords[1],3000);
        assertEquals(0.0,bestTestCoords[2],1500);
        assertEquals(0.0,bestTestCoords[3],1500);
        /*for(Double d : bestTestCoords)
            System.out.println(d);*/
    }
}