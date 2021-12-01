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
public class Main {
    public static void main(String[] args) throws InterruptedException {
            new VentanaSuperNodo(new SuperNodo(JOptionPane.showInputDialog("Ingrese la direccion IP"),Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto"))));
    }
}
