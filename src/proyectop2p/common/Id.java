/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.common;

import java.io.Serializable;

/**
 *
 * @author luis
 */
public class Id implements Serializable {

    public Id() {
        tiempo = new Tiempo();
    }

    public Id(String host, int port) {
        tiempo = new Tiempo();
        id = host + ":" + port;
        defaultPort = port + 91;
        this.host = host;
        this.port = port;
    }

    public String id;
    public String host;
    public int defaultPort;
    public int port;
    public boolean isSuperNode;
    public Tiempo tiempo;
    public boolean isBlocked;

}
