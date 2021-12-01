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
public class Nodo{
    Cliente_multidifusion_N ClienteMulticastNodo;
    Cliente_RMI_N ClienteRMI;
    int pto;
    ID mio;
        
    Nodo(String IP, int puerto) throws InterruptedException {
        mio = new ID(IP, puerto);
        ClienteMulticastNodo = new Cliente_multidifusion_N(puerto, this); 
        new Thread(ClienteMulticastNodo).start();
    }

    public int getPto() {
        return pto;
    }

    public void setPto(int pto) {
        this.pto = pto;
    }

    public void buscar(){
        ClienteRMI = new Cliente_RMI_N(pto);
        
    }
    
    public void CrearCarpeta(String puerto){
        String NuevaCarpeta = "Carpetas/" + puerto;
        File carpeta = new File(NuevaCarpeta);
        carpeta.mkdir();
    }
}
