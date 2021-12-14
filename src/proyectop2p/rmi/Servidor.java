package proyectop2p.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Servidor implements Methods {

    private final int port;
    private int num;
    private int cant;
    private int cont;
    private int previa;
    private int n;
    private List<String> listaNombres;
    private List<Integer> listaUbicaciones;
    private List<String> listaMD5;
    private List<Integer> listaNodos;

    public Servidor(int port) {
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
            Methods stub = (Methods) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("Methods", stub);
            System.out.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }

   

}
