package proyectop2p.nodo;

public class Nodo {

    public String ip;
    public int port;
    public Thread threadClienteMultidifusion;

    public Nodo(String ip, int port) {
        this.ip = ip;
        this.port = port;

    }

    private boolean connectRMI(String host, int port) {
        return false;
    }

    public void init() {
        ClienteMultidifusion clienteMultidifusion = new ClienteMultidifusion(
                port,
                "lo",
                getIMultidifusionCallback()
        );
        threadClienteMultidifusion = new Thread(clienteMultidifusion);
        threadClienteMultidifusion.start();
    }

    private IMultidifusionCallback getIMultidifusionCallback() {
        return new IMultidifusionCallback() {
            @Override
            public void setMessage(String message) {
                
            }

            @Override
            public boolean connect(String host, int port) {
                return connectRMI(host, port);
            }

        };
    }
}
