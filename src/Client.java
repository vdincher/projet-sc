import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.rmi.registry.*;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static Server_itf server;
	private static HashMap< Integer , SharedObject > objets = new HashMap();
	private static Client_itf client;
	
	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		// Récupération d'un stub sur l'objet serveur.
		try {
			Server_itf obj = (Server_itf) Naming.lookup("//localhost:8080/mon_serveur");
			server=obj;
			client = new Client();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name)  {
		SharedObject s=null;
		try {
			int id=server.lookup(name);
			s = objets.get(id);
			if (s==null) {
				objets.put(id, new SharedObject(null, id));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return s;
		}
	}		
	
	// binding in the name server
	public synchronized static void register(String name, SharedObject_itf so) {
		
		try {
			server.register(name, ((SharedObject) so).getID());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// creation of a shared object
	public synchronized static SharedObject create(Object o) {
		
			
		//Demander un ID au serveur
		int id;
		SharedObject s = null;
		try {
			id = server.create(o);
			s = new SharedObject(o,id);
			
		   objets.put(id,s);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return s;
		}
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		Object o=null;
		try {
			o=server.lock_read(id,client);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return o;
		}
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		Object o=null;
		try {
			o=server.lock_write(id,client);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return o;
		}
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		objets.get(id).reduce_lock();
		return objets.get(id).getO();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		objets.get(id).invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		objets.get(id).invalidate_writer();
		return objets.get(id).getO();
	}
}
