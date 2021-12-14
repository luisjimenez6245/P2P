package proyectop2p.nodo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import proyectop2p.rmi.Methods;

public class ClienteRMI {

    private final String host;
    private final int port;
    private Methods stub;

    public ClienteRMI(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void conectar() {
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            stub = (Methods) registry.lookup("Methods");
        } catch (Exception e) {
            System.err.println("Excepción del cliente: " + e.toString());
        }
    }

    public void setNC(char opc) {
    }

    public int conectadosN() {
        return -1;
    }
}
