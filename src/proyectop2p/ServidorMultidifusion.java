/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 *
 * @author erick
 */
public class ServidorMultidifusion implements Runnable {

    private final int pto;

    public ServidorMultidifusion(int pto) {
        this.pto = pto;
    }

    @Override
    public void run() {
        anunciar();
    }

    private void anunciar() {
        String hhost = "228.1.1.10";
        SocketAddress remote = null;

        try {
            try {
                remote = new InetSocketAddress(hhost, 2000);
            } catch (Exception e) {
            }//catch

            NetworkInterface ni = NetworkInterface.getByName("lo");
            DatagramChannel cl = DatagramChannel.open(StandardProtocolFamily.INET);
            cl.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            cl.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            cl.configureBlocking(false);
            InetAddress group = InetAddress.getByName("228.1.1.10");
            cl.join(group, ni);

            Selector sel = Selector.open();
            cl.register(sel, SelectionKey.OP_WRITE);

            ByteBuffer b = ByteBuffer.allocate(100);
            b.putInt(pto);

            while (true) {
                sel.select();
                Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey k = (SelectionKey) it.next();
                    it.remove();

                    if (k.isWritable()) {
                        DatagramChannel ch = (DatagramChannel) k.channel();
                        b.flip();
                        ch.send(b, remote);
                    }
                }//while
                Thread.sleep(5000);
            }
            //System.out.println("Termina envio de datagramas");
        } catch (IOException | InterruptedException e) {
        }//catch
    }
}
