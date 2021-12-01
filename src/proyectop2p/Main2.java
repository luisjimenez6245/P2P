/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import javax.swing.JOptionPane;

/**
 *
 * @author erick
 */
public class Main2 {
   public static void main(String[] args) throws InterruptedException {
            new VentanaNodo(new Nodo(JOptionPane.showInputDialog("Ingrese la direccion IP"),Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto"))));
    }
}
