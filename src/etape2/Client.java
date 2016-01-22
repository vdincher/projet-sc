package etape2;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.rmi.registry.*;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	
	private static final long serialVersionUID = 1L;
	private static Server_itf server;
	private static HashMap< Integer , SharedObject > objets = new HashMap<Integer,SharedObject>();
	private static Client_itf client;
	
	public Client() throws RemoteException {
		super();
	}

	

///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	public static HashMap<Integer, SharedObject> getObjets() {
		return objets;
	}



	public static void setObjets(HashMap<Integer, SharedObject> objets) {
		Client.objets = objets;
	}



	// initialization of the client layer
	public static void init() {
		// Récupération d'un stub sur l'objet serveur.
		try {
			server = (Server_itf) Naming.lookup("//localhost:8080/mon_serveur");
			System.out.println("connexion serveur/client");
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
			if (id!=-1) {
				s = new SharedObject(null,id);
				objets.put(id,s);
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return s;
		
		
	}		
	
	// binding in the name server
	public synchronized static void register(String name, SharedObject_itf so) {
		
		try {
			server.register(name, ((SharedObject) so).getID());
		} catch (RemoteException e) {
			System.out.println("Problème dans register du client");
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
			System.out.println("création d'un nouvel objet pour serveur");
			s = new SharedObject(o,id);
			
		   objets.put(id,s);
		   System.out.println("l'objet d'id: "+id+" est ajouté dans la collection objets du client");
		} catch (RemoteException e) {
			System.out.println("exception dans le create sharedObject");
			e.printStackTrace();
		} 
			return s;
		
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public synchronized static Object lock_read(int id) {
		Object o=null;
		try {
			
			o=server.lock_read(id,client);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} 
			return o;
		
	}

	// request a write lock from the server
	public synchronized static Object lock_write (int id) {
		Object o=null;
		try {
			o=server.lock_write(id,client);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			return o;
		
	}

	// receive a lock reduction request from the server
	public synchronized Object reduce_lock(int id) throws java.rmi.RemoteException {
		objets.get(id).setO(objets.get(id).reduce_lock());
		return objets.get(id).getO();
	}


	// receive a reader invalidation request from the server
	public synchronized void invalidate_reader(int id) throws java.rmi.RemoteException {
		objets.get(id).invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public synchronized Object invalidate_writer(int id) throws java.rmi.RemoteException {
		System.out.println("i_r ient");
       objets.get(id).setO(objets.get(id).invalidate_writer());
       System.out.println("après i_r ient");
		return objets.get(id).getO();
	}
}
