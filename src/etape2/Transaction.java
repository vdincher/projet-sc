package etape2;

import java.util.ArrayList;
import java.util.Collection;

public class Transaction {

	private static Transaction trCourante;
	Collection<SharedObject> objetsInitiauxLecture = new ArrayList<SharedObject>();
	Collection<SharedObject> objetsInitiauxEcriture = new ArrayList<SharedObject>();

	public Transaction() {
	}

	public boolean isActive() {
		return (Transaction.getCurrentTransaction().equals(this));
	}
	
	public void start() {
		trCourante=this;
	}

	public void commit() {
		for (SharedObject s : this.objetsInitiauxEcriture) {
			Client.getObjets().get(s.getID()).unlock();
		}
		for (SharedObject s : this.objetsInitiauxLecture) {
			Client.getObjets().get(s.getID()).unlock();
		}
		trCourante=null;
	}

	public void abort() {
		for (SharedObject s : this.objetsInitiauxEcriture) {
			Client.getObjets().put(s.getID(), s);
		}
		for (SharedObject s : this.objetsInitiauxLecture) {
			Client.getObjets().get(s.getID()).unlock();
		}
		trCourante=null;
	}

	public static Transaction getCurrentTransaction() {
		return trCourante;
	}


}
