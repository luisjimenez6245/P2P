package proyectop2p.nodo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;

public class Ventana extends javax.swing.JFrame {

    public Nodo nodo;
    public String mensaje;

    public Ventana(Nodo nodo) {
        this.nodo = nodo;
        mensaje = "";
        nodo.ventanaCallback = createIVentanaCallback();
    }

    private IVentanaCallback createIVentanaCallback() {
        return new IVentanaCallback() {
            @Override
            public void setMessage(String message) {
                System.out.println("LOGGER VENTANA: " + message);
                mensaje = message + "<br/>" + mensaje;
                if (MensajeLabel != null) {
                    MensajeLabel.setText("<html>" + mensaje + "</html>");
                }
            }

            @Override
            public void setSupernode(String supernodeId) {
                jLabel1.setText("Conectado al Super Nodo: " + supernodeId);

            }

            @Override
            public void deleteSupernode() {
                jLabel1.setText("No se está conectado a ningún supernodo");
            }
        };
    }

    public void init() {
        initComponents();

        this.setVisible(true);
        this.setTitle("Nodo " + nodo.ip + ":" + nodo.port);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nodo.stop();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        MensajeLabel = new javax.swing.JLabel();
        NombreText = new javax.swing.JTextField();
        EnviarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);
        setSize(new java.awt.Dimension(550, 600));

        getContentPane().setLayout(null);
        jScrollPane1.setViewportView(MensajeLabel);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("No se está conectado a ningún supernodo");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(18, 10, 500, 50);

        NombreText.setText("Nombre");

        EnviarButton.setText("Enviar");
        EnviarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnviarButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Mensajes");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(18, 50, 100, 50);

        MensajeLabel.setVerticalAlignment(JLabel.TOP);
        MensajeLabel.setAutoscrolls(true);

        jScrollPane1.setAutoscrolls(true);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(18, 100, 350, 400);

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane1.setFont(new java.awt.Font("Franklin Gothic Medium", 2, 12)); // NOI18N
        jScrollPane1.setForeground(new java.awt.Color(0, 102, 102));
        jScrollPane1.setViewportView(MensajeLabel);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Buscar");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(400, 50, 100, 50);

        getContentPane().add(NombreText);
        NombreText.setBounds(400, 100, 100, 50);

        getContentPane().add(EnviarButton);
        EnviarButton.setBounds(400, 160, 100, 20);

    }// </editor-fold>//GEN-END:initComponents

    private void EnviarButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_EnviarButtonActionPerformed
        String busqueda = NombreText.getText();
        nodo.buscar(busqueda);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton EnviarButton;
    public javax.swing.JLabel MensajeLabel;
    public javax.swing.JTextField NombreText;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
