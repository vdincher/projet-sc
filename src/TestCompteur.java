



import java.util.HashMap;

public class TestCompteur  {
	
	private static final long serialVersionUID = 1L;
	SharedObject c;
	
	
	public TestCompteur(){
		Client.init();
		 SharedObject count = Client.lookup("Compteur");
		if (count == null) {
			count = Client.create(new Compteur());
			Client.register("Compteur", count);
		}
		c=count;


	}

	

	

	
	public static void main(String[] args) throws Exception {
		
		
		HashMap<Integer, TestCompteur> tableau=new HashMap<Integer,TestCompteur>();
		HashMap<Integer,Integer> resultat=new HashMap<Integer,Integer>();
		boolean coherant=true;
		
		for (int i=0;i<=200;i++){
			tableau.put(i,new TestCompteur());
		}	
		Thread c1=new Processus(tableau);
		Thread c2=new Processus(tableau);
		//Thread c3=new Processus(tableau);
		//Thread c4=new Processus(tableau);
		
		c1.start();
		c2.start();
		//c3.start();
		//c4.start(); |c3.isAlive()||c4.isAlive()
		
while (c1.isAlive()||c2.isAlive()){

}
System.out.println("je suis un thread");



		
		for (int i=1;i<=199;i++){
			( tableau.get(i).c).lock_read();
			if (tableau.get(i).c.getO()!= null) resultat.put(i,((Compteur) tableau.get(i).c.getO()).lire());
			System.out.println("je lis");
			( tableau.get(i).c).unlock();
		}

		for (int i=1;i<=199;i++){
			coherant = (coherant && (resultat.get(i)==resultat.get(1)));
			
		}

		if (coherant){
			System.out.println("Les résultats sont cohérents, tous les compteurs sont à "+resultat.get(1));
		} else {
			System.out.println("Les résultats sont incohérents");
		}
		
		

	}

}
