package etape2;

import java.util.ArrayList;
import java.util.Collection;

public class Transaction {

	static Transaction trCourante = new Transaction();
	Collection<SharedObject> objetsInitiauxLecture = new ArrayList<SharedObject>();
	Collection<SharedObject> objetsInitiauxEcriture = new ArrayList<SharedObject>();

	
	
	public void start() {
		trCourante=this;
	}

	public void add_read(SharedObject s) {
		s.lock_read();
		objetsInitiauxLecture.add(s.deepClone());
	}

	public void add_write(SharedObject s) {
		System.out.println("avant");
		s.lock_write();
		System.out.println("apres");
		objetsInitiauxEcriture.add(s.deepClone());
	}

	public void commit() {
		for (SharedObject s : this.objetsInitiauxEcriture) {
			Client.getObjets().get(s.getID()).unlock();
		}
		for (SharedObject s : this.objetsInitiauxLecture) {
			Client.getObjets().get(s.getID()).unlock();
		}
		this.stop();
	}

	public void abort() {
		for (SharedObject s : this.objetsInitiauxEcriture) {
			Client.getObjets().put(s.getID(), s);
		}
		for (SharedObject s : this.objetsInitiauxLecture) {
			Client.getObjets().get(s.getID()).unlock();
		}
		this.stop();
	}

	public static Transaction getCurrentTransaction() {
		return trCourante;
	}

	private void stop() {
		trCourante=null;
	}


}
