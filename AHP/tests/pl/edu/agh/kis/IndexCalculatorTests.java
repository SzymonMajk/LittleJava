package pl.edu.agh.kis;

import static org.junit.Assert.*;
import org.junit.Test;
import pl.edu.agh.kis.math.IndexCalculator;

public class IndexCalculatorTests {

	@Test
	public void testCheckMeanConsistencyIndex() {
		Double[] data1 = {1.0,1.0,1.0};
		Double[] data2 = {2.0,4.0,2.0};
		Double[] data3 = {2.0,9.0,1.0,(double)1/3,(double)1/6,2.0};
		
		assertEquals(new Double(0.0), 
				IndexCalculator.checkMeanConsistencyIndex(data1));
		assertEquals(new Double(0.0), 
				IndexCalculator.checkMeanConsistencyIndex(data2));
		assertEquals(new Double(1.5214169595039782), 
				IndexCalculator.checkMeanConsistencyIndex(data3));
	}
}