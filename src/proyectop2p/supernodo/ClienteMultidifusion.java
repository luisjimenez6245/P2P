package proyectop2p.supernodo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import proyectop2p.common.Tiempo;
import proyectop2p.common.multidifusion.ClientHelper;
import proyectop2p.common.multidifusion.ICliente;

public class ClienteMultidifusion extends ICliente {

    private Map<String, ClientHelper> mapaClientes;

    protected ClienteMultidifusion(int port, String networkInterfaceName, String multicastAddr) {
        super(port, networkInterfaceName, multicastAddr);
        mapaClientes = new HashMap<String, ClientHelper>();
    }

    private void add(String info, int puerto) {
        ClientHelper ch = new ClientHelper();
        ch.info = info;
        ch.port = puerto;
        ch.tiempo = new Tiempo();
        Thread h = new Thread(ch.tiempo);
        h.start();
        mapaClientes.put(info, new ClientHelper());
    }

    private void cleanClients() {
        for (java.util.Map.Entry<String, ClientHelper> item : mapaClientes.entrySet()) {
            ClientHelper ch = item.getValue();
            if (ch.tiempo.getTiempo() == 0) {
                mapaClientes.remove(item.getKey());
            }
        }
    }

    @Override
    protected void action() throws Exception {
        selector.select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        ByteBuffer b = ByteBuffer.allocate(100);
        while (it.hasNext()) {
            SelectionKey k = it.next();
            it.remove();
            if (k.isReadable()) {
                DatagramChannel ch = (DatagramChannel) k.channel();
                b.clear();

                SocketAddress emisor = ch.receive(b);
                b.flip();

                InetSocketAddress d = (InetSocketAddress) emisor;

                int puerto = b.getInt();
                String hostHelper = d.getHostString() + ":" + puerto;
                if (!mapaClientes.containsKey(hostHelper) && puerto != port) {
                    add(hostHelper, puerto);
                } else if (mapaClientes.containsKey(hostHelper)) {
                    mapaClientes.get(hostHelper).tiempo.setTiempo();
                }
            }
        }
        cleanClients();
    }

}
