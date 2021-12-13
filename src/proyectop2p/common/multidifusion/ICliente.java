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
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public abstract class ICliente implements Runnable {

    protected String multicastAddr;
    protected int port;
    protected String networkInterfaceName;
    protected InetSocketAddress address;
    protected DatagramChannel channel;
    protected SocketAddress remote;
    protected Selector selector;
    protected NetworkInterface networkInterface;


    protected ICliente(int port, String networkInterfaceName, String multicastAddr) {
        this.port = port;
        this.networkInterfaceName = networkInterfaceName;
        this.multicastAddr = multicastAddr;

    }

    private void initialize() throws IOException {
        remote = new InetSocketAddress(multicastAddr, port);
        networkInterface = NetworkInterface.getByName(networkInterfaceName);
        address = new InetSocketAddress(port);
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        InetAddress group = InetAddress.getByName(multicastAddr);
        channel.join(group, networkInterface);
        channel.configureBlocking(false);
        channel.socket().bind(address);
        Selector sel = Selector.open();
        channel.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    }

    protected abstract void action() throws Exception;

    private void call(){
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
