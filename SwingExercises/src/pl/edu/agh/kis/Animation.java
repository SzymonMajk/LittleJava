package pl.edu.agh.kis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Klasa przedstawiaj¹ca animacjê dwóch kulek
 * @author Szymon Majkut
 *
 */
public class Animation extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Ellipse2D.Float ellipse = new Ellipse2D.Float();
	private Ellipse2D.Float ellipseSin = new Ellipse2D.Float();

	private double esize;

	private boolean initialize = true;

	Timer timer;
	
	ActionListener updateProBar;
	
	private int positionX1 = 50;
	private int positionY1 = 50;
	  
	private int positionX2 = 50;
	private int positionY2 = 50;
	  
	private double velocityX1 = 10;
    private double velocityY1 = 5;
	  
	private double velocityX2 = 10;
	private double velocityY2 = 5;
	  
	private double sinTime = 0;
  
	private Animation() {
		setXY(5, 200, 200);

		timer = new Timer(20, this);
		timer.setInitialDelay(190);
		timer.start();
	}

	private void setXY(double size, int w, int h) {
		esize = size;
		ellipse.setFrame(10, 10, esize, esize);
		ellipseSin.setFrame(10, 10, esize, esize);
	}

	private void reset(int w, int h) {
		setXY(40, w, h);
	}

	private void step(int w, int h) {
	    
		int OFFSET = 20;
	  
	  	//ruch jednostajnie przyœpieszony
	  	velocityY1 += 0.5;
		positionY1 += velocityY1;

		positionX1 += velocityX1;	
		
		if(positionX1+esize + OFFSET > getWidth() || positionX1 < 0 + OFFSET)
		{
			velocityX1 = -velocityX1;
		}
		else if(positionY1+esize + OFFSET > getHeight() || positionY1 < 0 + OFFSET)
		{
			velocityY1 = -velocityY1;
		}		

	  	/*velocityY2 += 0.7;
		positionY2 += velocityY2;*/

		velocityY2 = (int)(Math.cos(sinTime)*10);
		
		if(sinTime < 180)
		{
			sinTime += 0.1;
		}
		else
		{
			
		}

		positionY2 += velocityY2;
		positionX2 += velocityX2;
		
		
		if(positionX2+esize + OFFSET > getWidth() || positionX2 < 0 + OFFSET)
		{
			velocityX2 = -velocityX2;
		}
		else if(positionY2+esize + OFFSET > getHeight() || positionY2 < 0 + OFFSET)
		{
			//velocityY2 = -velocityY2;
			sinTime = 0;
		}
		
	    ellipse.setFrame(positionX1, positionY1, esize, esize);
	    ellipseSin.setFrame(positionX2, positionY2, esize, esize);
	   
    
	}

	private void render(int w, int h, Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.draw(ellipse);
		g2.setColor(Color.RED);
		g2.draw(ellipseSin);
	}

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
	    frame.add(new Animation());
	    frame.setSize(700, 500);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
}