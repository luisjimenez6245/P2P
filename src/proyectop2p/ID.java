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
public class ID {
    
    private final int puerto;
    private final String ip;
    
    public ID(String ip, int puerto) {
        this.puerto = puerto;
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public String getIp() {
        return ip;
    }
}
