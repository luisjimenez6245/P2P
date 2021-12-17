package proyectop2p.nodo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import proyectop2p.common.Id;

import proyectop2p.common.multidifusion.ICliente;

public class ClienteMultidifusion extends ICliente {

    private final IMultidifusionCallback callback;
    private final List<Id> superNodes;
    private final List<String> blackList;
    private Id selected;
    public boolean connected = false;
    private int time = 0;

    protected ClienteMultidifusion(
            String host,
            int port,
            String networkInterfaceName,
            IMultidifusionCallback callback
    ) {
        super(host, port, networkInterfaceName, "228.1.1.10");
        this.callback = callback;
        remote = new InetSocketAddress("228.1.1.10", port);
        superNodes = new ArrayList<>();
        blackList = new ArrayList<>();
    }

    public void restart() {
        connected = false;
        time = 0;
         blackList.clear();
        superNodes.clear();
    }

    private void isSuperNodo(String hostName, int port) {
        Id id = new Id(hostName, port);
        id.isSuperNode = true;
        superNodes.add(id);
    }

    private boolean tryToConnect(Id superNode) {
        if (!blackList.contains(superNode.id)) {
            callback.setMessage("Intendando conexion con: " + superNode.id + "<br>");
            if (callback.connect(superNode.host, superNode.port)) {
                callback.setMessage("<span style=\"color:green\"> Conectado con " + superNode.host + "</span><br>");
                return true;
            } else {
                blackList.add(superNode.id);
            }
        }
        return false;
    }

    private void tryToConnect() {
        time = 0;
        Id[] helper = new Id[superNodes.size()];
        superNodes.toArray(helper);
        for (int i = 0; i < helper.length; ++i) {
            connected = tryToConnect(helper[i]);
            selected = helper[i];
            if (connected) {
                break;
            }
        }
        blackList.clear();
        superNodes.clear();
    }

    private void searchForSupernodes() throws IOException, InterruptedException {
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
                b.clear();
                bytes = new byte[b.limit()];
                b.get(bytes, 0, b.limit());
                b.flip();
                String[] datos = new String(bytes).split(" ");
                int puerto = Integer.parseInt(datos[0]);
                String hostName = d.getHostString();
                String opc = datos[1].trim();

                if (opc.contains("S")) {
                    isSuperNodo(hostName, puerto);
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
        }
        Thread.sleep(1000);
        time += 1;
        callback.setMessage("Esperando para recolectar informacion de los super nodos ...");

    }

    private void searchForSelectedSupernode() throws IOException, InterruptedException {
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
                b.clear();
                bytes = new byte[b.limit()];
                b.get(bytes, 0, b.limit());
                b.flip();
                String[] datos = new String(bytes).split(" ");
                int puerto = Integer.parseInt(datos[0]);
                String hostName = d.getHostString();
                String opc = datos[1].trim();

                if (opc.contains("S")) {
                    Id idHelper = new Id(hostName, puerto);
                    if (idHelper.id.equals(selected.id)) {
                        callback.updateSelected();
                    }

                }
            }

        }
        callback.cleanSuperNode();

    }

    @Override
    protected void action() throws Exception {
        if (connected) {
            searchForSelectedSupernode();
            Thread.sleep(1000);
            return;
        }
        if (time > 10) {
            tryToConnect();
        } else {
            searchForSupernodes();
        }
    }

}
