package proyectop2p.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeMethods extends Remote {

    public boolean canConnect() throws RemoteException;
    
}
