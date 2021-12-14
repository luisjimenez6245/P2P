package proyectop2p.supernodo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import proyectop2p.common.Id;

import proyectop2p.common.Tiempo;
import proyectop2p.common.multidifusion.ICliente;

public class ClienteMultidifusion extends ICliente {

    private final IMultidifusionCallback callback;

    protected ClienteMultidifusion(
            int port,
            String networkInterfaceName,
            IMultidifusionCallback callback
    ) {
        super(port, networkInterfaceName, "228.1.1.10");
        this.callback = callback;

    }

    private void addNode(String host, int port_helper) {
        String url = host + ":" + port_helper;
        if (!callback.checkIfNodeExists(url)) {
            if (callback.canConnectNode()) {
                Id ch = new Id();
                ch.id = url;
                ch.host = host;
                ch.port = port_helper;
                ch.isSuperNode = false;
                ch.tiempo = new Tiempo();
                Thread h = new Thread(ch.tiempo);
                h.start();
                callback.addNode(ch);
            }
        } else {
            callback.addTimeNode(url);
        }
    }

    private void addSuperNode(String host, int port_helper) {
        String url = host + ":" + port_helper;
        if (!callback.checkIfSuperNodeExists(url)) {
            Id ch = new Id();
            ch.host = host;
            ch.id = url;
            ch.port = port_helper;
            ch.isSuperNode = true;
            ch.tiempo = new Tiempo();
            Thread h = new Thread(ch.tiempo);
            h.start();
            callback.addSuperNode(ch);
        } else {
            callback.addTimeSuperNode(url);
        }

    }

    private void cleanSuperNodes() {
        callback.cleanSuperNodes();
    }

    private void cleanNodes() {
        callback.cleanNodes();
    }

    @Override
    protected void action() throws Exception {
        selector.select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        ByteBuffer b = ByteBuffer.allocate(100);
        byte[] bytes;
        while (it.hasNext()) {
            SelectionKey k = it.next();
            it.remove();
            if (k.isReadable()) {
                DatagramChannel ch = (DatagramChannel) k.channel();

                SocketAddress emisor = ch.receive(b);
                InetSocketAddress d = (InetSocketAddress) emisor;
                b.clear();
                bytes = new byte[b.limit()];
                b.get(bytes, 0, b.limit());
                b.flip();

                String[] datos = new String(bytes).split(" ");
                int port_helper = Integer.parseInt(datos[0].trim());
                String opc = datos[1].trim();
                String host = d.getHostString();

                if (opc.contains("S")) {
                    addSuperNode(host, port_helper);
                } else {
                    addNode(host, port_helper);
                }

            }
        }
        cleanSuperNodes();
        cleanNodes();
        Thread.sleep(5000);
    }

}
