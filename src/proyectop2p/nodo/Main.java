package proyectop2p.nodo;

import java.net.SocketException;
import proyectop2p.common.GetLocalAddr;

public class Main {

    public static void main(String[] argv) throws SocketException {
        String networkInterfaceName = "en0";
        String ip = GetLocalAddr.getAddr(networkInterfaceName);
        Nodo nodo = new Nodo(networkInterfaceName, ip, (int) Math.floor(Math.random() * (9500 - 9900 + 1) + 9500));
        Ventana ventana = new Ventana(nodo);
        nodo.init();
        ventana.init();
    }
}
