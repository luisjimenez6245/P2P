package proyectop2p.nodo;

public class Nodo {

    public String ip;
    public int port;
    public Thread threadClienteMultidifusion;
    public final String networkInterfaceName;

    public Nodo(String networkInterfaceName, String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.networkInterfaceName = networkInterfaceName;
    }

    private boolean connectRMI(String host, int port) {
        try {
            System.out.println("intentando conectarse a :" + host + ":" + port);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    public void init() {
        ClienteMultidifusion clienteMultidifusion = new ClienteMultidifusion(
                ip,
                port,
                networkInterfaceName,
                getIMultidifusionCallback()
        );
        threadClienteMultidifusion = new Thread(clienteMultidifusion);
        threadClienteMultidifusion.start();
    }

    private IMultidifusionCallback getIMultidifusionCallback() {
        return new IMultidifusionCallback() {
            @Override
            public void setMessage(String message) {
                System.out.println(message);
            }

            @Override
            public boolean connect(String host, int port) {
                return connectRMI(host, port);
            }

        };
    }
}
