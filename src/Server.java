import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

public class Server implements Server_itf {

	static int id=0;
	HashMap<  Integer, ServerObject > objets= new HashMap<>();
	public static void main (String args[]) {
		
			// Création du serveur de nom - rmiregistry
			try {
				Registry registry = LocateRegistry.createRegistry(8080);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//On crée l'instance du serveur
			Server_itf server = new Server();
			
			try {
				Naming.rebind("//localhost:8080/mon_serveur", server);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	@Override
	public int lookup(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		// TODO Auto-generated method stub
		objets.get(id).setNom(name);
	}

	@Override
	public int create(Object o) throws RemoteException {
		// TODO Auto-generated method stub
		
		ServerObject obj=new ServerObject(o,id++);
		objets.put(id, obj);
		return id;
		
	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
