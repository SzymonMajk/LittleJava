package pl.edu.agh.kis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * G³ówna klasa uruchamiaj¹ca ca³oœæ aplikacji
 * @author Szymon Majkut
 *
 */
public class QuadraticEquation {

	static double firstCoef;
	static double secondCoef;
	static double thirdCoef;
	static double startx;
	static double endx;
	
	/**
	 * Punkt startowy ca³oœci aplikacji
	 * @param args kolejne parametry to wspó³czynniki a,b,c oraz zasiêg osi
	 */
    public static void main(String[] args) {
    	
    	/*parsowanie otrzymanyvh ziennych w parametrach*/
    	
    	//póki co wartoœci przypisane
    	firstCoef = 1;
    	secondCoef = 5;
    	thirdCoef = -50;
    	startx = -15;
    	endx = 15;
    	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Swing Quadratic Equation");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new My2Panel());
        f.pack();
        f.setVisible(true);
    } 
	
}

/**
 * Klasa odpowiadaj¹ca za utworzenie panelu wraz z funkcj¹ kwadratow¹, osiami oraz
 * naniesion¹ skal¹
 * @author Szymon Majkut
 *
 */
class My2Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int panelSizeX = 600;
	private int panelSizeY = 600;
	
    private ArrayList<Point> YsList = new ArrayList<Point>();

    private double maxY = -Double.MAX_VALUE;
    
    private double minY = Double.MAX_VALUE;
    
    private final int OFFSET = 20;
    
    private void setBegin()
    {
    	//obliczamy maksymalne wartoœci
        double x = QuadraticEquation.startx;

        while(x <= QuadraticEquation.endx)
        {
        	double y = calculatePoint(x);
        	
        	if(y < minY)
        	{
        		minY = y;
        	}
        	else if(y > maxY)
        	{
        		maxY = y;
        	}
        	
        	x = x + 0.01;
        } 
        
        if(maxY > 0 && Math.abs(minY) < Math.abs(maxY))
        {
        	minY = -maxY;
        }
        else if(maxY > 0 && Math.abs(minY) > Math.abs(maxY))
        {
        	maxY = -minY;
        }
    }
    
    /**
     * Funkcja odpowiada za przyjêcie poczatkowych rozmiarów okna
     */
    public Dimension getPreferredSize() {
        return new Dimension(panelSizeX,panelSizeY);
    }
    
    private double calculatePoint(double x)
    {
    	return QuadraticEquation.firstCoef*(x*x) + 
    			QuadraticEquation.secondCoef*x + QuadraticEquation.thirdCoef;
    }
    
    private void paintFilledSquare(Graphics g, double x, double y, double w, double h, Color c)
    {
    	g.setColor(c);
        g.fillRect((int)x,(int)y,(int)w,(int)h);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
          
        //Najpierw sprawdzamy szerokoœæ okna:
        
        panelSizeX = getWidth()-2*OFFSET;
        panelSizeY = getHeight()-2*OFFSET;
        
        int newZeroX = (int)getWidth()/2;
        int newZeroY = (int)getHeight()/2;

        
        //Przaliczamy dok³adnoœæ punktów w zale¿noœci od wielkoœci okna
                
        double xStep = 
        		(QuadraticEquation.endx - QuadraticEquation.startx)
        		/panelSizeX;
        
        YsList.clear();
        
        double x = QuadraticEquation.startx;
        
        while(x <= QuadraticEquation.endx)
        {
        	YsList.add(new Point(x,(calculatePoint(x))));
        	x += xStep;
        } 
        
        //Teraz rysujemy punkty
        Graphics2D g2 = (Graphics2D) g;
        
        //Rysowanie osi
        
        g2.draw(new Line2D.Float(10, newZeroY, getWidth()-10, newZeroY));
        g2.draw(new Line2D.Float(newZeroX, 10, newZeroX, getHeight()-10));

        double paintXStep = ((QuadraticEquation.endx-QuadraticEquation.startx)/10);
        double paintYStep = (maxY-minY)/10;
        
        double paintOX = QuadraticEquation.startx;
        double paintOY = maxY;
         
        for(int j = 0; j < 11; ++j)
        {
        	java.text.DecimalFormat df=new java.text.DecimalFormat("0.0");
        	
            g2.draw(new Line2D.Float(j*(panelSizeX/10)+20, newZeroY+10, 
            		j*(panelSizeX/10)+20, newZeroY-10));
            g2.drawString(""+ df.format(paintOX), j*(panelSizeX/10)+20, newZeroY-10);
            g2.draw(new Line2D.Float(newZeroX+10, j*(panelSizeY/10)+20, 
            		newZeroX-10, j*(panelSizeY/10)+20));
            g2.drawString(""+ df.format(paintOY), newZeroX+10,  j*(panelSizeY/10)+20);
            
            paintOX += paintXStep;
            paintOY -= paintYStep;
        }
        
        //Rysowanie punktów
        for(Point p : YsList)
        {
            paintFilledSquare(g,p.x*(panelSizeX/(QuadraticEquation.endx-QuadraticEquation.startx))+newZeroX,
            		-p.y*(panelSizeY/(maxY-minY))+newZeroY,1,1,Color.RED);
        }
        
        
    }  
    
    /**
     * Konstruktor, uruchamia funkcjê, odpowiedzialn¹ za wyznaczenie skrajnych wartoœci osi Y
     */
    My2Panel()
    {
    	setBegin();
    }
}

/**
 * Klasa przechowuj¹ca parametry punktu
 * @author Szymon
 *
 */
class Point {
	
	/**
	 * Pole przechowuje wartoœæ na osi OX punktu
	 */
	public double x = 0;
	/**
	 * Pole przechowuje wartoœæ na osi OY punktu
	 */
	public double y = 0;
	
	/**
	 * Konstruktor sparametryzowany, ustalaj¹cy wartoœci na osiach punktu
	 * @param x wartoœæ na osi OX punktu
	 * @param y wartoœæ na osi OY punktu
	 */
	Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}