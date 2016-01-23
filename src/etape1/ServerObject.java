package etape1;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

public class ServerObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private Object o;
	private String nom;
	private int ID;
	private enum Statut{
		nl,
		rl,
		wl;
	}
	private Statut statut;
	Collection<Client_itf> lecteurs = new ArrayList<Client_itf>(); 
	Client_itf redacteur;
	private Boolean red;

	public ServerObject( Object o, int id){
		this.o=o;

		this.ID=id;
		this.statut=Statut.nl;
		this.red=false;
	}

	public Object getO() {
		return o;
	}

	public void setO(Object o) {
		this.o = o;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	// invoked by the user program on the client node
	public synchronized void lock_read(Client_itf client) {
		if (this.statut==Statut.wl){
			while (red) {
				this.setO(this.reduce_lock());
			}
			this.statut=Statut.rl;
			System.out.println("On passe en rl");
			this.redacteur=null;
			this.red=false;
		}
		else if (this.statut==Statut.nl) {
			this.statut=Statut.rl;
			System.out.println("On passe en rl");
		}
		lecteurs.add(client);
	}

	// invoked by the user program on the client node
	public synchronized void lock_write(Client_itf client) {

		while (lecteurs.size()>0 || red) {

			if (red) {
				if(redacteur!=client)
				this.invalidate_writer();

			} else {
				this.invalidate_reader(client);

			}
		}
		if (this.statut!=Statut.wl){
			this.statut=Statut.wl;
			System.out.println("On passe en wl");
		}

		this.red=true;
		this.redacteur=client;

	}





	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		Object obj = null;
		try {
			obj = this.redacteur.reduce_lock(ID);
			this.lecteurs.add(this.redacteur);
			red=false;this.redacteur=null;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}



	// callback invoked remotely by the server
	public synchronized void invalidate_reader(Client_itf client) {
		for (Client_itf c : lecteurs) {
			try {
				if (c!=client)
				c.invalidate_reader(ID);

			} catch (RemoteException e) {
				System.out.println("problèmes dans l'invalidate_reader du server.");
				e.printStackTrace();
			}
		}
		lecteurs.clear();
	}

	public  Object invalidate_writer() {
		try {

			this.o=this.redacteur.invalidate_writer(ID);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		red=false;
		this.redacteur=null;
		return this.o;
	}
	public Boolean getRed() {
		return red;
	}

	public  void setRed(Boolean red) {
		this.red = red;
	}
	public Collection<Client_itf> getLecteurs() {
		return lecteurs;
	}

	public void setLecteurs(Collection<Client_itf> lecteurs) {
		this.lecteurs = lecteurs;
	}

	public  Client_itf getRedacteur() {
		return redacteur;
	}
	
	public  void addLecteur(Client_itf client){
		this.lecteurs.add(client);
	}

	public  void setRedacteur(Client_itf redacteur) {
		this.redacteur = redacteur;
	}

}