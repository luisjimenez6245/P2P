package proyectop2p.nodo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;

public class Nodo {

    private final Id id;
    public Id selectedId;
    public String ip;
    public int port;
    public Thread threadClienteMultidifusion;
    public final String networkInterfaceName;
    private ClienteRMI clienteRMI;
    private ClienteMultidifusion clienteMultidifusion;
    public IVentanaCallback ventanaCallback;

    public Nodo(
            String networkInterfaceName,
            String ip,
            int port
    ) {
        this.ip = ip;
        this.port = port;
        this.id = new Id(ip, port);
        this.networkInterfaceName = networkInterfaceName;
    }

    private List<Archivo> listFilesForFolder(File folder) {
        List<Archivo> result = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                Archivo helper = new Archivo(id, fileEntry.getName(), folder.getAbsolutePath() + "/" + fileEntry.getName());
                ventanaCallback.setMessage(helper.toReadbleString());
                result.add(helper);
            }
        }
        return result;
    }

    private void readFilesFromFolder() {
        File folder = new File("/Users/luis/pruebas");
        List<Archivo> archivos = listFilesForFolder(folder);
        clienteRMI.updateFiles(archivos);
    }

    private boolean connectRMI(String host, int port) {
        try {
            System.out.println("intentando conectarse a :" + host + ":" + port);
            selectedId = new Id(host, port);
            clienteRMI = new ClienteRMI(host, port, id);
            boolean b = clienteRMI.connect();
            try {
                readFilesFromFolder();
            } catch (Exception e) {
                System.out.println(e);
            }
            return b;
        } catch (Exception ex) {

        }
        return false;
    }

    public void stop() {
    }

    public void init() {
        clienteMultidifusion = new ClienteMultidifusion(
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
                if (ventanaCallback != null) {
                    ventanaCallback.setMessage(message);
                    System.out.println(message);
                }
            }

            @Override
            public boolean connect(String host, int port) {
                return connectRMI(host, port);
            }

            @Override
            public void updateSelected() {
                boolean canConnect = clienteRMI.connect();
                if (!canConnect) {
                    ventanaCallback.setMessage("Se ha desconectado de " + selectedId.id);
                    clienteMultidifusion.connected = false;
                }
            }

        };
    }
}
