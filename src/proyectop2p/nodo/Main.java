package proyectop2p.nodo;

import java.net.SocketException;
import javax.swing.JOptionPane;
import proyectop2p.common.GetLocalAddr;

public class Main {

    public static void main(String[] argv) throws SocketException {
        String networkInterfaceName = "en0";
        String ip = GetLocalAddr.getAddr(networkInterfaceName);
        int puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto del nodo"));
        Nodo nodo = new Nodo(networkInterfaceName, ip,puerto);
        Ventana ventana = new Ventana(nodo);
        nodo.init();
        ventana.init();
    }
}
