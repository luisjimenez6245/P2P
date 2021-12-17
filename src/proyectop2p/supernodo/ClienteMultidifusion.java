package proyectop2p.supernodo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import proyectop2p.common.Id;

import proyectop2p.common.multidifusion.ICliente;

public class ClienteMultidifusion extends ICliente {

    private final IMultidifusionCallback callback;

    protected ClienteMultidifusion(
            String host,
            int port,
            String networkInterfaceName,
            IMultidifusionCallback callback
    ) {
        super(host, port, networkInterfaceName, "228.1.1.10");
        this.callback = callback;

    }

    private void addNode(String host, int port_helper) {
        Id ch = new Id(host, port_helper);
        System.err.println("add node");
        if (!ch.id.equals(this.host + ":" + this.port)) {
            callback.addTimeNode(ch.id);
        }
    }

    private void addSuperNode(String host, int port_helper) {
        String url = host + ":" + port_helper;
        if (!url.equals(this.host + ":" + this.port)) {
            if (!callback.checkIfSuperNodeExists(url)) {
                Id ch = new Id();
                ch.host = host;
                ch.id = url;
                ch.port = port_helper;
                ch.isSuperNode = true;

                callback.addSuperNode(ch);
            } else {
                callback.addTimeSuperNode(url);
            }
        } else {
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
        ByteBuffer b = ByteBuffer.allocate(6);
        byte[] bytes;
        while (it.hasNext()) {
            SelectionKey k = it.next();
            it.remove();
            if (k.isReadable()) {
                DatagramChannel ch = (DatagramChannel) k.channel();
                b.clear();
                SocketAddress emisor = ch.receive(b);
                InetSocketAddress d = (InetSocketAddress) emisor;
                b.clear();
                bytes = new byte[b.limit()];
                b.get(bytes, 0, b.limit());
                b.flip();
                String contenido = new String(bytes);
                String[] datos = contenido.split(" ");
                String opc = datos[1].trim();
                int port_helper = d.getPort();
                String hostName = d.getHostString();
                System.out.println(host + ":" + port_helper);
                if (opc.contains("S")) {
                    addSuperNode(hostName, port_helper);
                } else {
                    addNode(hostName, port_helper);
                }

            }
            
        }
        cleanSuperNodes();
        cleanNodes();
        Thread.sleep(1000);
    }

}
