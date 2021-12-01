/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author erick
 */
public class Servidor_RMI implements Runnable{
    private ID id_mio;
    private File directorio;
    private SuperNodo nodo_yo;
    
    Servidor_RMI(SuperNodo nodo) {
        this.directorio = directorio;
        this.nodo_yo = nodo;
        this.id_mio = nodo.mio;
    }
    
    public void run() {
        try {
            //puerto default del rmiregistry
            java.rmi.registry.LocateRegistry.createRegistry(id_mio.getPuerto());
            System.out.println("RMI registro listo en el puerto "+ id_mio.getPuerto());
            System.out.println(java.rmi.registry.LocateRegistry.getRegistry(id_mio.getPuerto()));
        } catch (Exception e) {
            System.out.println("Excepcion RMI del registry:");
            e.printStackTrace();
        }//catch
        try {
            System.setProperty("java.rmi.server.codebase", "file:/tmp/Archivos/");
            

            System.err.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
