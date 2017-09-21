package pl.edu.agh.kis.zadania2;


public class Cw3 {


	static void foo(LittleFloat lF)
	{
		lF.f = (float)13.1313;
	}
	
	public static void main(String[] args)
	{
		LittleFloat _float = new LittleFloat();
		
		_float.f = (float)5.152;
		
		System.out.println("Float: " + _float.f);
		
		foo(_float);
		
		System.out.println("Float: " + _float.f);
	}
}
