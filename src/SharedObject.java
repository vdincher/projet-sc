


import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {

	private static final long serialVersionUID = 1L;
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
	
	public SharedObject deepClone() {
		SharedObject s=null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);

			s = (SharedObject) new ObjectInputStream(bais).readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	// invoked by the user program on the client node
	public void lock_read() {
		switch (this.statut) {
		case nl : this.statut=Statut.rlt;
		this.setO(Client.lock_read(ID));
		System.out.println("On passe en rlt");
		break;
		case wlc : this.statut=Statut.rlt_wlc;
		System.out.println("On passe en rlt_wlc");
		break;
		case rlc : this.statut=Statut.rlt;
		System.out.println("On passe en rlt");
		break;
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		if (statut!=Statut.wlc) {
			this.setO(Client.lock_write(ID));
		}
		this.statut=Statut.wlt;
		System.out.println("On passe en wlt");
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch(this.statut) {
		case rlt : this.statut=Statut.rlc;
		System.out.println("On passe en rlc");
		break;
		case wlt : this.statut=Statut.wlc;
		System.out.println("On passe en wlc");
		break;
		case rlt_wlc : this.statut=Statut.wlc;
		System.out.println("On passe en wlc");
		break;
		}
		notify();
	}


	// callback invoked remotely by the server
	public Object reduce_lock() {
		switch (this.statut) {
		case wlt :
			try {
				wait();				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (this.statut) {
			case wlc : this.statut=Statut.rlc;
			System.out.println("On passe en rlc");
			break;
			case rlt_wlc : this.statut=Statut.rlt;
			System.out.println("On passe en rlt");
			break;
			}
			break;
		case wlc : this.statut=Statut.rlc;
		System.out.println("On passe en rlc");
		break;
		case rlt_wlc : this.statut=Statut.rlt;
		System.out.println("On passe en rlt");
		break;
		}
		return o;
	}

	// callback invoked remotely by the server
	public void invalidate_reader() {
		if (this.statut==Statut.rlt) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.statut=Statut.nl;
		System.out.println("On passe en nl");
	}

	public Object invalidate_writer() {
		if (this.statut==Statut.wlt || this.statut == Statut.rlt_wlc) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.statut=Statut.nl;
		System.out.println("On passe en nl");
		return o;
	}
}
