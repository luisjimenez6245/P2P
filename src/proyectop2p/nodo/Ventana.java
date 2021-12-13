package proyectop2p.nodo;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class Ventana extends javax.swing.JFrame {

    public Nodo nodo;

    public Ventana(Nodo nodo) {
        initComponents();
        this.nodo = nodo;

        this.setVisible(true);
        this.setTitle("Nodo " + nodo.ip + ":" + nodo.port);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }

            private void formWindowClosing(WindowEvent evt) {                
                System.exit(0);
            }
        });
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Conectado al Super Nodo: ");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(18, 20, 500, 50);

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
        jLabel2.setBounds(18, 100, 100, 50);

        MensajeLabel.setVerticalAlignment(JLabel.TOP);
        MensajeLabel.setAutoscrolls(true);
        getContentPane().add(MensajeLabel);
        MensajeLabel.setBounds(18, 160, 250, 400);
        MensajeLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Buscar");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(330, 100, 100, 50);

        getContentPane().add(NombreText);
        NombreText.setBounds(300, 160, 100, 50);

        getContentPane().add(EnviarButton);
        EnviarButton.setBounds(300, 220, 100, 20);

        Actualiza_ventana act = new Actualiza_ventana(null, this);
        new Thread(act).start();
    }// </editor-fold>//GEN-END:initComponents

    private void EnviarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnviarButtonActionPerformed
        String nombre_buscar = NombreText.getText();

        int ubicacion = nodo.buscar(nombre_buscar);

        if (ubicacion == -1) {
            escribir_texto("No se encontró el archivo");
        } else {
            escribir_texto("Si se encontró en " + ubicacion);
            descargar(nombre_buscar, ubicacion);
        }
    }

    private void escribir_texto(String mensaje) {
        nodo.mensaje.mensaje += "<br/>" + mensaje;
    }

    private void descargar(String nombre, int ubicacion) {
        Cliente_de_flujo cliente_de_flujo = new Cliente_de_flujo(ubicacion, nombre, nodo.ObtenerDirectorio());
        new Thread(cliente_de_flujo).start();
        escribir_texto(nombre + " descargado con exito");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton EnviarButton;
    public javax.swing.JLabel MensajeLabel;
    public javax.swing.JTextField NombreText;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JScrollPane jScrollPane1;
}