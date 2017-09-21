package pl.edu.agh.kis.swing;

import javax.swing.JTextField;

public class Sumator {

	private JTextField nb1TF;
	private JTextField nb2TF;
	private JTextField resultTF;

	public Sumator(JTextField nb1TF, JTextField nb2TF, JTextField resultTF) {
		this.nb1TF = nb1TF;
		this.nb2TF = nb2TF;
		this.resultTF = resultTF;
	}

	public void calculate() {
		Double nb1 = getNumberFromTF(nb1TF);
		Double nb2 = getNumberFromTF(nb2TF);

		// sprawdzenie poprawnosci danych
		if (nb1 == null) {
			showParseError(nb1TF);
		}
		if (nb2 == null) {
			showParseError(nb2TF);
		}

		if (nb1 != null && nb2 != null) {
			// wersja 1 - wykowywanie w watku Event Dispaching
			double sum = nb1 + nb2;
			showResult(sum);
			
			// wersja 2 - wykonywanie w osobnym watku (w tle)
//			SumatorWorker sumatorWorker = new SumatorWorker(nb1, nb2, resultTF);
//			sumatorWorker.execute();
		}

	}

	private Double getNumberFromTF(JTextField tf) {
		String txt = tf.getText();
		try {
			double d = Double.parseDouble(txt);
			return d;
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private void showParseError(JTextField tf) {
		tf.setText("Niepoprawna liczba typu double");
	}

	private void showResult(double res) {
		resultTF.setText("" + res);
	}

}
