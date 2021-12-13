/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author erick
 */
public class ClienteRMINodo {
    int pto;
    FuncionesRMI stub;
    Registry registry;

    
    public ClienteRMINodo(int pto) {
        this.pto = pto;
    }

    public void Conectar(){
        try {
            registry = LocateRegistry.getRegistry(pto);
            stub = (FuncionesRMI) registry.lookup("FuncionesRMI");
            
        } catch (Exception e) {
            System.err.println("Excepción del cliente: " +e.toString());
            e.printStackTrace();
        }
    }
       
    public void BuscarArchivo(){
        try {
            Registry registry = LocateRegistry.getRegistry(pto);
            
        } catch (Exception e) {
            System.err.println("Excepción del cliente: " +e.toString());
            e.printStackTrace();
        }
    }
}
