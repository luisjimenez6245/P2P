package proyectop2p.supernodo;

import java.net.SocketException;
import proyectop2p.common.GetLocalAddr;

public class Main {

    public static void main(String[] args) throws SocketException {
        String networkInterfaceName = "en0";
        String ip = GetLocalAddr.getAddr(networkInterfaceName);
        SuperNodo superNodo = new SuperNodo(networkInterfaceName, ip, (int) Math.floor(Math.random() * (9100 - 9000 + 1) + 9000));
        Ventana ventana = new Ventana(superNodo);
        superNodo.init();
        ventana.init();
    }
}
