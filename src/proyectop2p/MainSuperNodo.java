/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import javax.swing.*;

/**
 *
 * @author erick
 */
public class MainSuperNodo {

    public static void main(String[] args) throws InterruptedException {
        String ip = JOptionPane.showInputDialog("Ingrese la direccion IP");
        int port =   Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto"));
        SuperNodo nodo = new SuperNodo(ip, port);
        new VentanaSuperNodo(nodo);
    }
}
