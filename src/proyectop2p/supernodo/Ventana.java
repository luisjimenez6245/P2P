package proyectop2p.supernodo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;

public class Ventana extends javax.swing.JFrame {

    public SuperNodo nodo;

    public Ventana(SuperNodo nodo) {
        this.nodo = nodo;
    }

    public void init() {
        setVisible(true);
        setTitle("Super-nodo " + nodo.ip + ":" + nodo.port);
        initComponents();
        Thread t = new Thread(() -> {
            while (true) {

                actualizar(activosListaSN, nodo.mapSuperNodes);
                actualizar(activosListaN, nodo.mapNodes);
                Map<String, List<Archivo>> lista = new HashMap<>();
                lista.putAll(nodo.mapArchivosNodos);
                lista.putAll(nodo.mapArchivosSupernodos);
                actualizarArchivos(archivos, lista, false);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nodo.stop();
                t.interrupt();
                System.exit(0);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane13 = new javax.swing.JScrollPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        activosListaSN = new javax.swing.JList<>();
        activosListaN = new javax.swing.JList<>();
        archivos = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);
        setSize(new java.awt.Dimension(930, 350));

        getContentPane().setLayout(null);

        jScrollPane1.setViewportView(activosListaSN);
        jScrollPane13.setViewportView(activosListaN);
        jScrollPane15.setViewportView(archivos);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Super Nodos");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(18, 40, 100, 50);

        jScrollPane1.setAutoscrolls(true);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(18, 80, 220, 200);

        activosListaSN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        activosListaSN.setFont(new java.awt.Font("Franklin Gothic Medium", 2, 12)); // NOI18N
        activosListaSN.setForeground(new java.awt.Color(0, 102, 102));
        jScrollPane1.setViewportView(activosListaSN);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Nodos");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(276, 40, 100, 50);

        jScrollPane13.setAutoscrolls(true);
        getContentPane().add(jScrollPane13);
        jScrollPane13.setBounds(258, 80, 220, 200);

        activosListaN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        activosListaN.setFont(new java.awt.Font("Franklin Gothic Medium", 2, 12)); // NOI18N
        activosListaN.setForeground(new java.awt.Color(0, 102, 102));
        jScrollPane13.setViewportView(activosListaN);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Archivos");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(516, 40, 100, 50);

        jScrollPane15.setAutoscrolls(true);
        getContentPane().add(jScrollPane15);
        jScrollPane15.setBounds(498, 80, 400, 200);

        archivos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        archivos.setFont(new java.awt.Font("Franklin Gothic Medium", 2, 12)); // NOI18N
        archivos.setForeground(new java.awt.Color(0, 102, 102));
        jScrollPane15.setViewportView(archivos);

    }// </editor-fold>//GEN-END:initComponents

    public void actualizar(javax.swing.JList lista, Map<String, Id> elementos) {
        DefaultListModel modelo = new DefaultListModel();
        lista.removeAll();
        elementos.forEach((key, item) -> {
            String helper = key + " tiempo: " + item.tiempo.getTiempo();
            modelo.addElement(helper);
        });
        lista.setModel(modelo);
    }

    public void actualizarArchivos(javax.swing.JList lista, Map<String, List<Archivo>> elementos,  boolean clean) {
        DefaultListModel modelo = new DefaultListModel();
        lista.removeAll();
        elementos.forEach((key, items) -> {
            String helper = key + " archivo: ";
            items.forEach((t) -> {
                modelo.addElement(helper + t.name + " md5: " + t.md5);
            });
        });
        lista.setModel(modelo);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList<String> activosListaSN;
    public javax.swing.JList<String> activosListaN;
    public javax.swing.JList<String> archivos;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane13;
    public javax.swing.JScrollPane jScrollPane15;
    // End of variables declaration//GEN-END:variables

}
