package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw22 */

	/**
	 * Wykorzystanie enuma Banknotes
	 * @author Szymon Majkut
	 *
	 */
public class Cw22 {
		Banknotes banknote;
		
		public void describe()
		{
			System.out.print("Mamy tutaj... ");
			switch(banknote)
			{
			
			case DYCHA: System.out.println("Dyszke..."); break;
			case DWIEDYCHY: System.out.println("Dwie dychy!"); break;
			case PIECDYCH: System.out.println("Pięć dych?!"); break;
			case STOWA: System.out.println("Setka?!?!?!"); break;
			case DWIESTOWY: System.out.println("To naprawde istnieje?"); break;
			case PIECSTOW: System.out.println("Pięć stów? W jednym banknocie? Chyba żarty!"); break;
			}
		}
		
		public Cw22(Banknotes banknote)
		{
			this.banknote = banknote;
		}
		
		public static void main(String...args)
		{
			Cw22
			 a1 = new Cw22(Banknotes.DYCHA),
			 a2 = new Cw22(Banknotes.DWIESTOWY),
			 a3 = new Cw22(Banknotes.PIECSTOW);
			 
			 a1.describe();
			 a2.describe();
			 a3.describe();
		}
		/* Output (100%match)
		 * Mamy tutaj... Dyszke...
		 * Mamy tutaj... To naprawde istnieje?
		 * Mamy tutaj... Pięć stów? W jednym banknocie? Chyba żarty!
		 *///:~
	}
