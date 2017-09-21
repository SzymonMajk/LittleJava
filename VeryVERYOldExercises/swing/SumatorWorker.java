package pl.edu.agh.kis.swing;

import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class SumatorWorker extends SwingWorker<Double, Void> {

	Double nb1,nb2;
	JTextField resultTF;
	
	public SumatorWorker(Double nb1, Double nb2, JTextField resultTF) {
		this.nb1 = nb1;
		this.nb2 = nb2;
		this.resultTF = resultTF;
	}
	
	@Override
	protected Double doInBackground() throws Exception {
		Double sum = nb1 + nb2;
		return sum;
	}

	
	public void done() {
		Double sum;
		try {
			sum = get();
			resultTF.setText("" + sum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
}
