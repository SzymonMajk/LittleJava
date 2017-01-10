package pl.edu.agh.kis;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 * G³ówna klasa uruchamiaj¹ca ca³oœæ aplikacji
 * @author Szymon Majkut
 *
 */
public class Chessboard {
    
	/**
	 * Punkt startowy ca³oœci aplikacji
	 * @param args w tym programie nieu¿ywane
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Chessboard");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    } 
}

/**
 * Klasa odpowiadaj¹ca za utworzenie panelu wraz z odpowiednimi komponentami
 * @author Szymon Majkut
 *
 */
class MyPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int panelSize = 600;
    
    private int squareW = panelSize/10;
    private int squareH = panelSize/10;
    
    private int startX = 0;
    private int startY = 0;

    /**
     * Funkcja odpowiada za przyjêcie poczatkowych rozmiarów okna
     */
    public Dimension getPreferredSize() {
        return new Dimension(panelSize,panelSize);
    }
    
    private void paintFilledSquare(Graphics g, int x, int y, int w, int h, Color c)
    {
    	g.setColor(c);
        g.fillRect(x,y,w,h);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //Tutaj ustalamy skalowanie
        
        panelSize = getWidth() > getHeight() ? getHeight() : getWidth();
        
        squareW = panelSize/12;
        squareH = panelSize/12;
        
        int squareX = startX;
        int squareY = startY;
        
        //Tutaj narysujemy sobie tablicê do gry
        
        for(int i = 0; i < 100; ++i)
        {
        	if(i % 10 == 0)
        	{
        		squareY += squareH;
        		squareX = startX;
        	}
        	
        	squareX += squareW;
        	        	
        	if(i/10 % 2 == 0)
        	{
        		if(i % 2 == 0)
            	{
                    paintFilledSquare(g,squareX,squareY,squareW,squareH,Color.RED);
            	}
            	else
            	{
            		paintFilledSquare(g,squareX,squareY,squareW,squareH,Color.BLUE);
            	}
        	}
        	else
        	{
        		if(i % 2 == 0)
            	{
                    paintFilledSquare(g,squareX,squareY,squareW,squareH,Color.BLUE);
            	}
            	else
            	{
            		paintFilledSquare(g,squareX,squareY,squareW,squareH,Color.RED);
            	}
        	}
       }
        
    	Character symbol = 'A';
    	Integer number = 1;
        
        int symbolX = startX + (int)(0.4*squareW);
        int symbolY = startY + (int)(0.92*squareH);
        
        int numberX = startX + (int)(0.65*squareW);
        int numberY = startY + (int)(0.7*squareH);
        
        //Przygotowanie wielkoœci czcionki
        
    	int fontSize = panelSize/25; //skala zale¿na tu od powierzchni
    	Font f = getFont();
    	setFont(new Font(f.getFontName(), f.getStyle(), fontSize));
    	      
        for(int i = 0; i < 10; ++i)
        {
        	symbolX += squareW;
            g.drawString(symbol.toString(),symbolX,symbolY);
            g.drawString(symbol.toString(),symbolX,symbolY+(int)(10.6*squareW));
            
            numberY += squareH;
            if(number<10)
            {
                g.drawString(number.toString(),numberX,numberY);
            }
            else
            {
                g.drawString(number.toString(),numberX-(int)(0.24*squareW),numberY);
            }
            g.drawString(number.toString(),numberX+(int)(10.5*squareH),numberY);

            ++symbol;
            ++number;
        }
    }  
}