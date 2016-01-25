

import java.util.HashMap;
import java.util.Random;

public class Processus extends Thread {
	
	HashMap<Integer, TestCompteur> tab;
	
	public Processus(HashMap<Integer, TestCompteur> tableau){
		tab=tableau;
		
	}
	
	public void run(){
		Random rand = new Random();
		for (int i=0;i<=50;i++){
			int nb=rand.nextInt(200)+1;
			tab.get(nb).c.lock_write();
			System.out.println(nb);
			if (tab.get(nb).c.getO()!=null) ((Compteur) tab.get(nb).c.getO()).ecrire();
			tab.get(nb).c.unlock();
		}
		
	}

}
