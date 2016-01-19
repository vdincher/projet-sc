import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

public class ServerObject implements Serializable {
	
	private Object o;
	private String nom;
	private int ID;
	private enum Statut{
		nl,
		rl,
		wl;
	}
	private Statut statut;
    Collection<Client> lecteurs = new ArrayList<Client>(); 
    Client redacteur;
	
	public ServerObject( Object o, int id){
		this.o=o;
		
		this.ID=id;
		this.statut=Statut.nl;
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
		while (redacteur!=null) {
			this.reduce_lock();
		}
		this.statut=Statut.rl;
		this.redacteur=null;
	}

	// invoked by the user program on the client node
	public void lock_write() {
		while (!lecteurs.isEmpty() || redacteur!=null) {
			if (redacteur!=null) {
				this.invalidate_writer();
			} else {
				this.invalidate_reader();
			}
		}
		this.statut=Statut.wl;
		this.lecteurs.clear();
	}

	// invoked by the user program on the client node
	//public synchronized void unlock() {
	//	switch (this.statut) {
	//	case rl : 
	//	case wl : redacteur=null;
	//			  this.statut=Statut.nl;
	//			  break;
	//	}
	//}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		try {
			this.redacteur.reduce_lock(ID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}

	public Collection<Client> getLecteurs() {
		return lecteurs;
	}

	public void setLecteurs(Collection<Client> lecteurs) {
		this.lecteurs = lecteurs;
	}

	public Client getRedacteur() {
		return redacteur;
	}

	public void setRedacteur(Client redacteur) {
		this.redacteur = redacteur;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		for (Client c : lecteurs) {
			try {
				c.invalidate_reader(ID);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized Object invalidate_writer() {
		try {
			this.redacteur.invalidate_writer(ID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.o;
	}
	
}