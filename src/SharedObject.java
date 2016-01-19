import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	private Object o;
	private int ID;
	private enum Statut{
		nl,
		rlc,
		wlc,
		rlt,
		wlt,
		rlt_wlc;
	}
	private Statut statut;
	
	public SharedObject( Object o, int id){
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

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	// invoked by the user program on the client node
	public void lock_read() {
		switch (this.statut) {
		case nl : this.statut=Statut.rlt;
				  this.setO(Client.lock_read(ID));
				  break;
		case wlc : this.statut=Statut.rlt_wlc;
				  this.setO(Client.lock_read(ID));
				  break;
		case rlc : this.statut=Statut.rlt;
				   break;
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		this.statut=Statut.wlt;
		if (statut!=Statut.wlc) {
			this.setO(Client.lock_write(ID));
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch(this.statut) {
		case rlt : this.statut=Statut.rlc;
				   break;
		case wlt : this.statut=Statut.wlc;
				   break;
		case rlt_wlc : this.statut=Statut.wlc;
				   break;
		}
		notify();
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.statut=Statut.rlc;
		return o;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		if (this.statut==Statut.rlt) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.statut=Statut.nl;
	}

	public synchronized Object invalidate_writer() {
		if (this.statut==Statut.wlt) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.statut=Statut.nl;
		return o;
	}
}
