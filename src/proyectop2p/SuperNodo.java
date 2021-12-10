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
public class SuperNodo {
    
    ClienteMultidifusionSuperNodo ClienteMulticast;
    private ServidorMultidifusion ServidorMulticast;
    private ServidorRMI ServidorRMI;
    ID mio;

    SuperNodo(String IP, int puerto) throws InterruptedException {
        mio = new ID(IP, puerto);
        ServidorMulticast = new ServidorMultidifusion(puerto);
        ClienteMulticast = new ClienteMultidifusionSuperNodo(this);
        ServidorRMI = new ServidorRMI(this);

        new Thread(ServidorMulticast).start();
        new Thread(ClienteMulticast).start();
        new Thread(ServidorRMI).start();
    }
}
