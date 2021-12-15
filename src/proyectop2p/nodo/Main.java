package proyectop2p.nodo;

import java.net.SocketException;
import proyectop2p.common.GetLocalAddr;

public class Main {

    public static void main(String[] argv) throws SocketException {
        String networkInterfaceName = "en0";
        String ip = GetLocalAddr.getAddr(networkInterfaceName);
        Nodo nodo = new Nodo(networkInterfaceName, ip, 9000);
        nodo.init();
    }
}
