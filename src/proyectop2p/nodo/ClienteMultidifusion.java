package proyectop2p.nodo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import proyectop2p.common.Mensaje;
import proyectop2p.common.Tiempo;
import proyectop2p.common.multidifusion.ICliente;

public class ClienteMultidifusion extends ICliente {

    private List<Integer> listaPuertosSN;
    private Mensaje mensaje;
    private boolean conexion;
    private IMultidifusionCallback nodo;

    protected ClienteMultidifusion(
        int port, 
        String networkInterfaceName, 
        String multicastAddr,
        IMultidifusionCallback callback
    ) {
        super(port, networkInterfaceName, multicastAddr);
        listaPuertosSN = new ArrayList<>();
        nodo = callback;
    }

    @Override
    protected void action() throws Exception {
        ByteBuffer b = ByteBuffer.allocate(11);
        byte[] bytes;

        Tiempo t = new Tiempo();
        Thread h = new Thread(t);
        h.start();
        while (true) {
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
                    String opc = datos[1].trim();

                    if (opc.contains("S")) {
                        opc = "S";
                    }

                    if (t.getTiempo() > 0) {
                        System.out.println(t.getTiempo());
                        if (!listaPuertosSN.contains(puerto) && opc.equals("S")) {
                            listaPuertosSN.add(puerto);
                        }
                    } else if (t.getTiempo() == 0 && !conexion && !listaPuertosSN.isEmpty()) {
                        int tam = listaPuertosSN.size();

                        int n = (int) Math.floor(Math.random() * tam);

                        int sn = 9000;

                        System.out.println(sn);
                        nodo.conectar("ip", puerto);

                        while (!conexion) {
                            mensaje.mensaje += "Intendando conexion con: " + sn + "<br>";

                            if (nodo.disponiblidadSuperNodo()) {
                                nodo.crearCarpeta(String.valueOf(sn));
                                nodo.setNC();
                                conexion = true;
                            } else {
                                listaPuertosSN.remove(n);

                                if (listaPuertosSN.isEmpty()) {
                                    break;
                                }

                                sn = listaPuertosSN.get(0);
                                nodo.conectar("", puerto);
                                n = 0;
                            }
                        }

                        if (conexion) {
                            mensaje.mensaje += "<span style=\"color:green\"> Conectado con " + sn + "</span><br>";
                        } else {
                            mensaje.mensaje += "<span style=\"color:red\">No hay super nodos disponibles</span><br>";
                        }
                    }
                }

                if (k.isWritable() && conexion) {
                    nodo.listarArchivos();
                    DatagramChannel ch = (DatagramChannel) k.channel();
                    b.clear();
                    String helperMessage = port + " N" + nodo.getPto();
                    b.put(helperMessage.getBytes());
                    b.flip();
                    ch.send(b, remote);
                }
                Thread.sleep(5000);
            }
        }
    }

}
