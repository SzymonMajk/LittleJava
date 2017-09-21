package pl.edu.agh.kis.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class Frame1 extends JFrame {

	private JLabel statusbar;

	public Frame1() {
		initUI();
	}

	private void initUI() {
		// initButtonExample();
		initMenu();

		setTitle("Frame 1 - testowe okno");
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// inicjalizacja paska statusu
		statusbar = new JLabel(" Pasek statusu");
		statusbar.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));
		add(statusbar, BorderLayout.SOUTH);

	}

	private void initButtonExample() {
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		// content.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		JButton quitButton = new JButton("Wyjście");
		quitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		// dodanie przycisku do panela u gory
		content.add(quitButton, BorderLayout.NORTH);

		// dodanie przycisku do panela w centrum
		// content.add(quitButton);

		// quitButton.setToolTipText("Naciśnij, żeby zakończyć aplikację");

		setContentPane(content);
		pack();
	}

	private void initMenu() {
		JMenuBar menubar = prepareMenu();
		setJMenuBar(menubar);
	}

	private JMenuBar prepareMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("Plik");
		file.setMnemonic(KeyEvent.VK_P);

		JMenuItem eMenuItem = new JMenuItem("Zakończ");
		eMenuItem.setMnemonic(KeyEvent.VK_Z);
		eMenuItem.setToolTipText("Wyjście z aplikacji");
		eMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		file.add(eMenuItem);
		menubar.add(file);

		JMenu view = new JMenu("Widok");
		view.setMnemonic(KeyEvent.VK_W);

		JCheckBoxMenuItem sbar = new JCheckBoxMenuItem("Wyświetl StatuBar");
		sbar.setState(true);

		sbar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (statusbar.isVisible()) {
					statusbar.setVisible(false);
				} else {
					statusbar.setVisible(true);
				}
			}

		});
		view.add(sbar);
		menubar.add(view);

		return menubar;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// utworzenie okna
				Frame1 frame1 = new Frame1();
				// pokazanie okna
				frame1.setVisible(true);
			}
		});

	}
}
