/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;

/**
 *
 * @author erick
 */
public class Actualiza_ventana implements Runnable {

    private VentanaSuperNodo venSN;
    private VentanaNodo venN;
    List<String> elementos;
    DefaultListModel modelo = new DefaultListModel();

    public Actualiza_ventana(VentanaSuperNodo venSN, VentanaNodo venN) {
        this.venSN = venSN;
        this.venN = venN;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (;;) {
            try {
                if (venSN != null) {
                    elementos = venSN.nodo.ClienteMulticast.getListaConectados();
                    actualizar(venSN.ActivosListaSN, elementos);

                    elementos = venSN.nodo.ClienteMulticast.getListaTiempos();
                    actualizar(venSN.TiempoSN, elementos);
                }
                
                if(venN != null){
                    venN.jLabel1.setText("Conectado al Super Nodo: " + venN.nodo.getPto());
                }

                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void actualizar(javax.swing.JList lista, List<String> elementos) {
        modelo = new DefaultListModel();
        lista.removeAll();

        for (String u : elementos) {
            modelo.addElement(u);
        }
        lista.setModel(modelo);
    }
}
