package etape1;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements Server_itf, Serializable {

	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	static int id=0;
	HashMap<  Integer, ServerObject > objets= new HashMap<Integer,ServerObject>();
	
	public static void main (String args[])  {
		
			// Création du serveur de nom - rmiregistry
			try {
				Registry registry = LocateRegistry.createRegistry(8080);
				Server server = new Server();
				Naming.rebind("//localhost:8080/mon_serveur", server);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//On crée l'instance du serveur
			

		
	}
	
	@Override
	public int lookup(String name) throws RemoteException {
		int nid=-1;
		for (ServerObject o : objets.values()) {
			if (o.getNom().equals(name))
				nid=o.getID();
			break;
		}
		return nid;
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		// TODO Auto-generated method stub
		objets.get(id).setNom(name);
	}

	@Override
	public int create(Object o) throws RemoteException {
		// TODO Auto-generated method stub
		ServerObject obj=new ServerObject(o,id);
		objets.put(id, obj);
		id=id+1;
		return (id-1);
	}

	@Override
	public synchronized  Object lock_read(int id, Client_itf client) throws RemoteException {
		this.objets.get(id).lock_read(client);
		Object o = this.objets.get(id).getO();
		return o;
	}

	@Override
	public synchronized  Object lock_write(int id, Client_itf client) throws RemoteException {
		this.objets.get(id).lock_write(client);
		Object o = this.objets.get(id).getO();
		return o;
	}

}
