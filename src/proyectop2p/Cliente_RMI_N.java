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
public class Cliente_RMI_N {
    int pto;
    
    public Cliente_RMI_N(int pto) {
        this.pto = pto;
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
