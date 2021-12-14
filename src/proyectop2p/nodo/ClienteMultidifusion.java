package proyectop2p.nodo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import proyectop2p.common.multidifusion.ICliente;

public class ClienteMultidifusion extends ICliente {

    private final IMultidifusionCallback callback;
    private final List<String> blackList;

    protected ClienteMultidifusion(
            int port,
            String networkInterfaceName,
            IMultidifusionCallback callback
    ) {
        super(port, networkInterfaceName, "228.1.1.10");
        this.callback = callback;
        blackList = new ArrayList<>();
    }

    private void isSuperNodo(String host, int port) {
        String url = host + port;
        if (!blackList.contains(url)) {
            callback.setMessage("Intendando conexion con: " + host + "<br>");
            if (callback.connect(host, port)) {
                callback.setMessage("<span style=\"color:green\"> Conectado con " + host + "</span><br>");
                
            } else {
                blackList.add(url);
            }
        }
    }

    @Override
    protected void action() throws Exception {
        ByteBuffer b = ByteBuffer.allocate(11);
        byte[] bytes;
        selector.select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey k = it.next();
            it.remove();

            if (k.isReadable()) {
                DatagramChannel ch = (DatagramChannel) k.channel();
                b.clear();

                SocketAddress emisor = ch.receive(b);
                InetSocketAddress d = (InetSocketAddress) emisor;
                d.getAddress().getHostName();
                b.clear();
                bytes = new byte[b.limit()];
                b.get(bytes, 0, b.limit());
                b.flip();
                String[] datos = new String(bytes).split(" ");
                int puerto = Integer.parseInt(datos[0]);
                String host = d.getHostString();
                String opc = datos[1].trim();

                if (opc.contains("S")) {
                    isSuperNodo(host, puerto);
                    if (k.isWritable()) {
                        DatagramChannel chWritable = (DatagramChannel) k.channel();
                        b.clear();
                        String helperMessage = puerto + " N";
                        b.put(helperMessage.getBytes());
                        b.flip();
                        chWritable.send(b, remote);
                    }
                }

            }

            Thread.sleep(5000);
        }
    }

}
