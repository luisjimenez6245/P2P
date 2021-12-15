package proyectop2p.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SupernodeServidor  extends NodeServidor implements SupernodeMethods {

    private final int port;

    public SupernodeServidor(int port) {
        super();
        this.port = port;
    }

    public void run() {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
            System.out.println("RMI registro listo en el puerto " + port);
            System.out.println(java.rmi.registry.LocateRegistry.getRegistry(port));
        } catch (Exception e) {
            System.out.println("Excepcion RMI del registry:");
            e.printStackTrace();
        } // catch
        try {
            System.setProperty("java.rmi.server.codebase", "file:/tmp/Archivos/");
            SupernodeMethods stub = (SupernodeMethods) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("Methods", stub);
            System.out.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }

  

}
