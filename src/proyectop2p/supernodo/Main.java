package proyectop2p.supernodo;

import java.net.SocketException;
import javax.swing.JOptionPane;
import proyectop2p.common.GetLocalAddr;

public class Main {

    public static void main(String[] args) throws SocketException {
        String networkInterfaceName = "en0";
        String ip = GetLocalAddr.getAddr(networkInterfaceName);
        int puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto de supernodo"));
        SuperNodo superNodo = new SuperNodo(networkInterfaceName, ip, puerto);
        Ventana ventana = new Ventana(superNodo);
        superNodo.init();
        ventana.init();
    }
}
