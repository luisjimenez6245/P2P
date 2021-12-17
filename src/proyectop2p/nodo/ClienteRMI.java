package proyectop2p.nodo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;
import proyectop2p.rmi.FuncionesRMI;

public class ClienteRMI {

    private final String host;
    private final int port;
    private final Id id;
    private FuncionesRMI stub;

    public ClienteRMI(String host, int port, Id id) {
        this.host = host;
        this.id = id;
        this.port = port;
    }

    public boolean connect() {
        if (stub != null || init()) {
            try {
                return stub.connectNode(id);
            } catch (RemoteException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }

    public Id[] requestFile(Archivo archivo) {
        try {
            return stub.requestFile(archivo.md5, id.id);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Id[0];
    }

    public Archivo[] searchFile(String name) {
        try {
            return stub.searchFile(name, id.id);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Archivo[0];
    }

    public void updateFiles(List<Archivo> archivos) {

        try {
            stub.addFiles(id, archivos);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean init() {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            stub = (FuncionesRMI) registry.lookup("FuncionesRMI");
            return true;
        } catch (NotBoundException | RemoteException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

}
