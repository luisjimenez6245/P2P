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
public class Nodo {

    private final ClienteMultidifusionNodo ClienteMulticastNodo;
    ClienteRMINodo ClienteRMI;
    int pto;
    ID mio;

    public Nodo(String IP, int puerto) throws InterruptedException {
        mio = new ID(IP, puerto);
        ClienteMulticastNodo = new ClienteMultidifusionNodo(puerto, this);
        new Thread(ClienteMulticastNodo).start();
    }

    public int getPto() {
        return pto;
    }

    public void setPto(int pto) {
        this.pto = pto;
    }

    public void buscar() {
        ClienteRMI = new ClienteRMINodo(pto);

    }

    public void CrearCarpeta(String puerto) {
        String NuevaCarpeta = "Carpetas/" + puerto;
        File carpeta = new File(NuevaCarpeta);
        carpeta.mkdir();
    }
}
