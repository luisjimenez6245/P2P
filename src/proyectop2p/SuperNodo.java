/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

/**
 *
 * @author erick
 */
public class SuperNodo {

    public final ClienteMultidifusionSuperNodo clienteMulticast;
    private final ServidorMultidifusion servidorMulticast;
    private final ServidorRMI servidorRMI;
    public final ID id;
    

    public SuperNodo(String IP, int puerto) throws InterruptedException {
        id = new ID(IP, puerto);
        servidorMulticast = new ServidorMultidifusion(puerto);
        clienteMulticast = new ClienteMultidifusionSuperNodo(this);
        servidorRMI = new ServidorRMI(this);
    }
    
    public void start(){
        new Thread(servidorMulticast).start();
        new Thread(clienteMulticast).start();
        new Thread(servidorRMI).start();
    }
}
