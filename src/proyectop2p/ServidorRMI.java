/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.io.File;


/**
 *
 * @author erick
 */
public class ServidorRMI implements Runnable{
    
    private final ID id;
    private File directorio;
    private SuperNodo nodo_yo;
    
    public ServidorRMI(SuperNodo nodo) {
        this.nodo_yo = nodo;
        this.id = nodo.mio;
    }
    
    public void run() {
        try {
            //puerto default del rmiregistry
            java.rmi.registry.LocateRegistry.createRegistry(id.getPuerto());
            System.out.println("RMI registro listo en el puerto "+ id.getPuerto());
            System.out.println(java.rmi.registry.LocateRegistry.getRegistry(id.getPuerto()));
        } catch (Exception e) {
            System.out.println("Excepcion RMI del registry:");
        }//catch
        try {
            System.setProperty("java.rmi.server.codebase", "file:/tmp/Archivos/");
            

            System.err.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
        }
    }
}
