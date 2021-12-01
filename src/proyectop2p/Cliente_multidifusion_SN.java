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
import javax.swing.DefaultListModel;

/**
 *
 * @author erick
 */
public class Cliente_multidifusion_SN implements Runnable {

    static private List<String> ListaConectados;
    static private List<Integer> ListaPuertos;
    static private List<String> ListaTiempos;
    SuperNodo sn;

    public Cliente_multidifusion_SN(SuperNodo sn) {
        ListaConectados = new ArrayList<>();
        ListaTiempos = new ArrayList<>();
        ListaPuertos = new ArrayList<>();
        this.sn = sn;
    }

    @Override
    public void run() {
        escuchar();
    }

    private void escuchar() {
        try {
            NetworkInterface ni = NetworkInterface.getByName("lo");
            InetSocketAddress dir = new InetSocketAddress(2000);
            DatagramChannel s = DatagramChannel.open(StandardProtocolFamily.INET);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            InetAddress group = InetAddress.getByName("228.1.1.10");
            s.join(group, ni);
            s.configureBlocking(false);
            s.socket().bind(dir);

            Selector sel = Selector.open();

            s.register(sel, SelectionKey.OP_READ);

            ByteBuffer b = ByteBuffer.allocate(100);

            Tiempo t[] = new Tiempo[100];
            int i = 0;

            while (true) {
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
                        String info = d.getHostString() + ":" + puerto;

                        if (ListaConectados.isEmpty() && puerto != sn.mio.getPuerto()) {
                            ListaConectados.add(info);
                            ListaPuertos.add(puerto);

                            t[i] = new Tiempo();
                            ListaTiempos.add(String.valueOf(t[i].getTiempo()));

                            Thread h = new Thread(t[i]);
                            h.start();
                            i++;
                        } else if (!ListaPuertos.contains(puerto) && puerto != sn.mio.getPuerto()) {
                            ListaConectados.add(info);
                            ListaPuertos.add(puerto);

                            t[i] = new Tiempo();

                            ListaTiempos.add(String.valueOf(t[i].getTiempo()));

                            Thread h = new Thread(t[i]);
                            h.start();
                            i++;
                        } else if(ListaPuertos.contains(puerto)) {
                            int pos = ListaPuertos.indexOf(puerto);

                            t[pos].setTiempo();
                        }
                    }

                    for (int j = 0; j < ListaTiempos.size(); j++) {
                        int tiempo = t[j].getTiempo();

                        if (tiempo == 0) {
                            ListaConectados.remove(j);
                            ListaPuertos.remove(j);
                            ListaTiempos.remove(j);

                        } else {
                            ListaTiempos.set(j, String.valueOf(tiempo));
                        }
                    }

                }//while
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }

    public static List<String> getListaConectados() {
        return ListaConectados;
    }

    public static List<String> getListaTiempos() {
        return ListaTiempos;
    }

    static class Tiempo implements Runnable {

        int t;

        public Tiempo() {
            this.t = 30;
        }

        void setTiempo() {
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
