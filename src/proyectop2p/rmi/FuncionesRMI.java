package proyectop2p.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;

public interface FuncionesRMI extends Remote {

    public boolean connectSupernode(Id id) throws RemoteException;

    public boolean connectNode(Id id) throws RemoteException;

    public Map<Id, List<Archivo>> getFilesOtherFromSuperNode() throws RemoteException;

    public List<Archivo> getFiles() throws RemoteException;

    public void addFiles(Id id, List<Archivo> archivos) throws RemoteException;

    public Archivo[] searchFile(String name, String requestId) throws RemoteException;

    public Archivo[] searchFileSupernode(String name, String requestId) throws RemoteException;

    public Id[] requestFile(String name, String requestId) throws RemoteException;

    public void forceUpdate() throws RemoteException;

}
