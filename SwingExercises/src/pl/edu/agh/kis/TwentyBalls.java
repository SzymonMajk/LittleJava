package pl.edu.agh.kis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Klasa przedstawiaj¹ca animacjê dwóch kulek
 * @author Szymon Majkut
 *
 */
public class TwentyBalls extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private boolean initialize = true;

	private final int OFFSET = 5;
	
	Timer timer;
	
	ActionListener updateProBar;
	 
	private ArrayList<Ball> ballsList = new ArrayList<Ball>();
	
	private TwentyBalls() {

		timer = new Timer(40, this);
		timer.setInitialDelay(190);
		timer.start();
	}

	private void reset(int w, int h) {
		
		ballsList.clear();
		
		//Losowe ustawienie pocz¹tkowe kó³ek
		for(int i = 0; i < 20; ++i)
		{
			ballsList.add( new Ball(40+i*40,(int)(100*Math.random()+200),
					10,i % 2 == 0 ? 1 : 0));
		}
		/*
		//Efekciarskie ustawienie pocz¹tkowe kó³ek
		for(int i = 0; i < 20; ++i)
		{
			ballsList.add( new Ball(50+i*40,10+i*15,10,1));
		}*/

		
	}

	private void step(int w, int h) {
	   

		for(Ball tmp : ballsList)
		{
			tmp.positionY += tmp.velocityY;
			tmp.velocityY += 1;
			
			if(tmp.positionY + tmp.esize + OFFSET > getHeight() || tmp.positionY < 0 + OFFSET)
			{
				
				tmp.velocityY = -tmp.velocityY;
					
				//Uwaga do kolejnej iteracji
				if(tmp.velocityY > 0)
				{
					tmp.velocityY -= 1;
				}
				else
				{
					tmp.velocityY += 1;
				}
				
			}
			
		}
	
		for(Ball tmp : ballsList)
		{
			tmp.setBallFrame();
		}
    
	}

	private void render(int w, int h, Graphics2D g2) {
		
		for(Ball tmp : ballsList)
		{
			g2.setColor(Color.BLUE);
			g2.draw(tmp.ellipse);
		}

	}

	/**
	 * Funkcja odpowiada za obs³ugê animacji oraz je¿eli to konieczne, przerysowanie
	 * ca³oœci
	 */
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2.setRenderingHints(rh);
		Dimension size = getSize();

		if (initialize) {
			reset(size.width, size.height);
			initialize = false;
		}
		step(size.width, size.height);
		render(size.width, size.height, g2);
	}

	/**
	 * Funkcja listenera, której zadaniem jest wykonanie przerysowania
	 * gdy nast¹pi odpowiednie zdarzenie
	 */
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	/**
	 * Punkt wyjœcia aplikacji
	 * @param args nieu¿ywane w programie
	 */
	public static void main(String[] args) {
	    JFrame frame = new JFrame("TimerBasedAnimation");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(new TwentyBalls());
	    frame.setSize(900, 500);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
}

/**
 * Klasa pomocnicza przechowuj¹ca informacje na temat pojedynczej pi³ki
 * @author Szymon
 *
 */
class Ball {
	
	/**
	 * Szereg pól publicznych, aby dostêp do nich by³ szybki
	 */
	public Ellipse2D.Float ellipse = new Ellipse2D.Float();
	public double esize = 25;
	public int positionX = 50;
	public int positionY = 50;
	public int velocityY = 0;
	public int movingSystem = 0;
	public int myTime = 0;
	
	/**
	 * Funkcja odpowiadaj¹ca za ustalenie pozycji pi³ki do narysowania
	 */
	public void setBallFrame()
	{
		ellipse.setFrame(positionX, positionY, esize, esize);
	}
	
	/**
	 * Szereg prostych sparametryzowanych konstruktorów
	 */
	Ball(int x, int y, int v, int s)
	{
		positionX = x;
		positionY = y;
		velocityY = v;
		movingSystem = s;
	}
	
	Ball(int s)
	{
		movingSystem = s;
	}
}