package proyectop2p.supernodo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;
import proyectop2p.rmi.FuncionesRMI;

public class ClienteRMI {

    private final int port;
    private final Id id;
    private final String host;
    private FuncionesRMI stub;

    public ClienteRMI(String host, int port, Id id) {
        this.port = port;
        this.host = host;
        this.id = id;
    }

    public boolean connect() {
        if (stub != null || init()) {
            try {
                return stub.connectSupernode(id);
            } catch (RemoteException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }

    public List<Archivo> updateFiles() {
        System.out.println("solicitando actualización");
        try {
            List<Archivo> archivos = stub.getFiles();
            
            return archivos;
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
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
