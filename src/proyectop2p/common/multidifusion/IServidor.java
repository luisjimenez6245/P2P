/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.common.multidifusion;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public abstract class IServidor implements Runnable {

    private final int port;
    private final String networkInterfaceName;
    private final String multicastAddr;
    protected InetSocketAddress address;
    protected DatagramChannel channel;
    protected SocketAddress remote;
    protected Selector selector;
    protected NetworkInterface networkInterface;

    protected IServidor(int port, String networkInterfaceName, String multicastAddr) {
        this.port = port;
        this.networkInterfaceName = networkInterfaceName;
        this.multicastAddr = multicastAddr;

    }

    private void initialize() throws SocketException, IOException {
        address = new InetSocketAddress(port);
        networkInterface = NetworkInterface.getByName(networkInterfaceName);
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        InetAddress group = InetAddress.getByName(multicastAddr);
        remote = new InetSocketAddress(group, port);
        channel.join(group, networkInterface);
        channel.configureBlocking(false);
        channel.socket().bind(address);
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    protected void defaultAction(String type) throws InterruptedException, IOException {
        String mensaje = port + " " + type;

        ByteBuffer buf = ByteBuffer.allocate(6);

        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            if (key.isWritable()) {
                DatagramChannel ch = (DatagramChannel) key.channel();
                buf.clear();
                buf.put(mensaje.getBytes());
                buf.flip();
                ch.send(buf, remote);
            }
        }
        Thread.sleep(5000);
    }

    protected abstract void action() throws Exception;

    private void call() {
        try {
            action();
        } catch (Exception ex) {
            Logger.getLogger(IServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            initialize();
            while (true) {
                call();
            }
        } catch (IOException ex) {
            Logger.getLogger(IServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
