package pl.edu.agh.kis.swing;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SumatorFrame extends JFrame {

	JTextField input1TF;
	JTextField input2TF;
	JButton sumBt;
	JTextField resultTF;
	
	Sumator sumatorWorker;
	
	public SumatorFrame() {
		initUI();
		initSumatorWorker();
		initListeners();
	}

	private void initUI() {
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new GridLayout(3, 1));

		JPanel nbPanel = new JPanel();
		nbPanel.setLayout(new GridLayout(2, 2));

		nbPanel.add(new JLabel("Pierwsza liczba"));
		input1TF = new JTextField();
		input1TF.setPreferredSize(new Dimension(100, 22));
		// dodac tooltip informujacy co jest w tym polu tekstowym
		nbPanel.add(input1TF);

		nbPanel.add(new JLabel("Druga liczba"));
		input2TF = new JTextField();
		input2TF.setPreferredSize(new Dimension(100, 22));
		// dodac tooltip informujacy co jest w tym polu tekstowym
		nbPanel.add(input2TF);
	
		mainPanel.add(nbPanel);

		sumBt = new JButton("Oblicz sumę");
		mainPanel.add(sumBt);
		
		JPanel resPanel = new JPanel();
		resPanel.setLayout(new GridLayout(1, 2));
		resPanel.add(new JLabel("Wynik"));
		resultTF = new JTextField();
		resultTF.setEditable(false);
		resultTF.setPreferredSize(new Dimension(100, 22));
		resPanel.add(resultTF);
		mainPanel.add(resPanel);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		
		setContentPane(mainPanel);
		// ustala minimalny rozmiar okna
		// setMinimumSize(new Dimension(320, 200));
		pack();
		
		setTitle("Sumator - oblicznie sumy 2-ch liczb");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}

	private void initSumatorWorker() {
		sumatorWorker = new Sumator(input1TF, input2TF, resultTF);
	}
	
	
	private void initListeners() {
		sumBt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Action: " + ae.getActionCommand());
				if(ae.getSource().equals(sumBt)) {
					sumatorWorker.calculate();
				}
			}
		});
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// utworzenie okna
				SumatorFrame sumator = new SumatorFrame();
				// pokazanie okna
				sumator.setVisible(true);
			}
		});

	}

}
