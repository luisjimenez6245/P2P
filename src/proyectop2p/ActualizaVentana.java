/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author erick
 */
public class ActualizaVentana implements Runnable {

    private final VentanaSuperNodo venSN;
    private final VentanaNodo venN;
    private List<String> elementos;
    private DefaultListModel modelo = new DefaultListModel();

    public ActualizaVentana(VentanaSuperNodo venSN, VentanaNodo venN) {
        this.venSN = venSN;
        this.venN = venN;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        for (;;) {
            try {
                if (venSN != null) {
                    elementos = venSN.nodo.clienteMulticast.getListaConectados();
                    actualizar(venSN.ActivosListaSN, elementos);

                    elementos = venSN.nodo.clienteMulticast.getListaTiempos();
                    actualizar(venSN.TiempoSN, elementos);
                }
                
                if(venN != null){
                    venN.jLabel1.setText("Conectado al Super Nodo: " + venN.nodo.getPto());
                }

                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public void actualizar(javax.swing.JList lista, List<String> elementos) {
        modelo = new DefaultListModel();
        lista.removeAll();

        elementos.forEach(u -> {
            modelo.addElement(u);
        });
        lista.setModel(modelo);
    }
}
