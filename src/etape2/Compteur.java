package etape2;

import java.io.Serializable;
import java.util.Random;

public class Compteur implements Serializable {
	private static final long serialVersionUID = 1L;
	
	int c;
	Random rand = new Random();
	
	public Compteur(){
		c=0;
		
	}
	
	public int lire(){
		return c;
	}
	
	public void ecrire(){
		System.out.println("On rentre dans écriture");
		int nombre = rand.nextInt(3);
		switch (nombre){
		case 0: c=c+rand.nextInt(20);
		case 1: c=c*rand.nextInt(5);
		case 2: c=c- rand.nextInt(50);
		}
		
	}

}
