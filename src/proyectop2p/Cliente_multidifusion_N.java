/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author erick
 */
public class Cliente_multidifusion_N implements Runnable{
    int pto, puerto;
    private List<Integer> ListaPuertos;
    private List<String> ListaIP;
    Nodo nodo;
    
    public Cliente_multidifusion_N(int puerto, Nodo nodo) {
        this.puerto = puerto;
        this.pto = 2000;
        ListaPuertos = new ArrayList<>();
        ListaIP = new ArrayList<>();
        this.nodo = nodo;
    }
    
    @Override
    public void run() {
        escuchar();
    }

    private void escuchar() {
        try {
            NetworkInterface ni = NetworkInterface.getByName("lo");
            InetSocketAddress dir = new InetSocketAddress(pto);
            DatagramChannel s = DatagramChannel.open(StandardProtocolFamily.INET);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            InetAddress group = InetAddress.getByName("228.1.1.10");
            s.join(group, ni);
            s.configureBlocking(false);
            s.socket().bind(dir);

            Selector sel = Selector.open();

            s.register(sel, SelectionKey.OP_READ);

            ByteBuffer b = ByteBuffer.allocate(10);
            
            Tiempo t = new Tiempo();
            
            Thread h = new Thread(t);
            h.start();
            
            while (t.getTiempo() > 0) {
                sel.select();
                Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey k = (SelectionKey) it.next();
                    it.remove();

                    if (k.isReadable()) {
                        DatagramChannel ch = (DatagramChannel) k.channel();
                        b.clear();
                        
                        SocketAddress emisor = ch.receive(b);
                        b.flip();
                        
                        InetSocketAddress d = (InetSocketAddress) emisor;
                        
                        int puerto = b.getInt();
                        String IP = d.getHostString();
                        
                        if (ListaPuertos.isEmpty()){
                            ListaPuertos.add(puerto);
                            ListaIP.add(IP);
                        } else if (!ListaPuertos.contains(puerto)){
                            ListaPuertos.add(puerto);
                            ListaIP.add(IP);
                        }
                    }
                }//while
            }
            
            int tam = ListaPuertos.size();
                        
            int n = (int) Math.floor(Math.random()*tam);
                        
            int sn = ListaPuertos.get(n);
            System.out.println(sn);
            
            nodo.setPto(sn); 
            nodo.CrearCarpeta(String.valueOf(puerto));
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }
    
    static class Tiempo implements Runnable {
        int t;

        public Tiempo() {
            this.t = 30;
        }

        int getTiempo() {
            return this.t;
        }

        @Override
        public void run() {
            try {
                while (t > 0) {
                    t--;

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
            }
        }
    }
}
