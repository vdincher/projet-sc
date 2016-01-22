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
	public void lock_read() {
		if (red) {
			this.setO(this.reduce_lock());
		}
		this.statut=Statut.rl;
		System.out.println("On passe en rl");
		this.redacteur=null;
		this.red=false;
	}

	// invoked by the user program on the client node
	public void lock_write() {

		while (!lecteurs.isEmpty() || red) {

			if (red) {

				this.invalidate_writer();

			} else {
				this.invalidate_reader();
				this.lecteurs.clear();
			}
		}
		this.statut=Statut.wl;
		System.out.println("On passe en wl");
		this.red=true;

	}



	public Boolean getRed() {
		return red;
	}

	public void setRed(Boolean red) {
		this.red = red;
	}

	// callback invoked remotely by the server
	public Object reduce_lock() {
		Object obj = null;
		try {
			obj = this.redacteur.reduce_lock(ID);
			this.lecteurs.add(this.redacteur);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public Collection<Client_itf> getLecteurs() {
		return lecteurs;
	}

	public void setLecteurs(Collection<Client_itf> lecteurs) {
		this.lecteurs = lecteurs;
	}

	public Client_itf getRedacteur() {
		return redacteur;
	}

	public void setRedacteur(Client_itf redacteur) {
		this.redacteur = redacteur;
	}

	// callback invoked remotely by the server
	public void invalidate_reader() {
		for (Client_itf c : lecteurs) {
			try {
				c.invalidate_reader(ID);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Object invalidate_writer() {
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
	
}