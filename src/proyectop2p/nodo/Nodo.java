package proyectop2p.nodo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import proyectop2p.common.Archivo;
import proyectop2p.common.Descarga.ClienteDescarga;
import proyectop2p.common.Descarga.IDescargaCallback;
import proyectop2p.common.Descarga.ServidorDescarga;
import proyectop2p.common.Id;

public class Nodo {

    private final Id id;
    public Id selectedId;
    public String ip;
    public int port;
    private final String folder;
    public Thread threadClienteMultidifusion;
    public final String networkInterfaceName;
    private ClienteRMI clienteRMI;
    private ClienteMultidifusion clienteMultidifusion;
    public IVentanaCallback ventanaCallback;
    private ServidorDescarga servidorDescarga;

    public Nodo(
            String networkInterfaceName,
            String ip,
            int port
    ) {
        this.ip = ip;
        this.port = port;
        this.id = new Id(ip, port);
        folder = "/Users/luis/pruebas/" + port + "/";
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
        File folderName = new File(folder);
        List<Archivo> archivos = listFilesForFolder(folderName);
        clienteRMI.updateFiles(archivos);
    }

    private boolean connectRMI(String host, int port) {
        try {
            System.out.println("intentando conectarse a :" + host + ":" + port);
            selectedId = new Id(host, port);
            Thread t = new Thread(selectedId.tiempo);
            t.start();
            clienteRMI = new ClienteRMI(host, port, id);
            boolean b = clienteRMI.connect();
            if (b) {
                try {
                    readFilesFromFolder();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return b;
        } catch (Exception ex) {

        }
        return false;
    }

    private void downloadFile(Id[] ids, Archivo archivo) {
        if (ids.length == 0) {
            ventanaCallback.setMessage("Ocurrió un error al descargar el archivo");
        }
        ClienteDescarga cliente = new ClienteDescarga(ids, archivo.name, archivo.md5, folder, getDescargaCallback());
        if (cliente.downloadFromServer()) {
            ventanaCallback.setMessage("Descargado correctamente");
            boolean b = clienteRMI.connect();
            if (b) {
                try {
                    readFilesFromFolder();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } else {
            ventanaCallback.setMessage("Ocurrió un error al descargar: " + archivo.toReadbleString());
        }
    }

    private void oneFile(Archivo archivo) {

        downloadFile(clienteRMI.requestFile(archivo), archivo);

    }

    private void multipleFiles(Archivo[] archivos) {

        String[] md5s = new String[archivos.length];
        for (int i = 0; i < archivos.length; ++i) {
            md5s[i] = archivos[i].md5;

        }

        String md5 = (String) JOptionPane.showInputDialog(
                null,
                "Se tiene mas de un archivo\n"
                + "Selecciona uno de los siguientes: ", "MD5",
                JOptionPane.QUESTION_MESSAGE,
                null,
                md5s, md5s[0]);
        if (md5 != null) {
            for (int i = 0; i < archivos.length; ++i) {
                if (archivos[i].md5.equals(md5)) {
                    downloadFile(clienteRMI.requestFile(archivos[i]), archivos[i]);
                }

            }

        }
    }

    public void buscar(String name) {
        if (clienteRMI != null) {
            ventanaCallback.setMessage("Buscando archivo con el nombre: " + name);
            Archivo[] archivos = clienteRMI.searchFile(name);
            if (archivos.length == 0) {
                ventanaCallback.setMessage("No sé encontró ningún archivo con este nombre");
                return;
            }
            if (archivos.length == 1) {
                ventanaCallback.setMessage("Sé encontró el archivo con md5: " + archivos[0].md5);
                oneFile(archivos[0]);
                return;

            }
            ventanaCallback.setMessage("Se encontraron varios archivos iguales");
            multipleFiles(archivos);
        } else {
            ventanaCallback.setMessage("No se peude buscar archivo");
        }
    }

    public void stop() {
    }

    public void init() {

        try {

            File f = new File(folder);
            f.mkdir();
        } catch (Exception ex) {
        }
        servidorDescarga = new ServidorDescarga(id.defaultPort, folder, getDescargaCallback());
        servidorDescarga.init();
        Thread t = new Thread(servidorDescarga);
        t.start();
        clienteMultidifusion = new ClienteMultidifusion(
                ip,
                port,
                networkInterfaceName,
                getIMultidifusionCallback()
        );
        threadClienteMultidifusion = new Thread(clienteMultidifusion);
        threadClienteMultidifusion.start();
    }

    private IDescargaCallback getDescargaCallback() {
        return new IDescargaCallback() {
            @Override
            public void setMessage(String message) {
                if (ventanaCallback != null) {
                    ventanaCallback.setMessage(message);
                }
            }
        };
    }

    private IMultidifusionCallback getIMultidifusionCallback() {
        return new IMultidifusionCallback() {
            @Override
            public void setMessage(String message) {
                if (ventanaCallback != null) {
                    ventanaCallback.setMessage(message);
                }
            }

            @Override
            public boolean connect(String hostName, int portHelper) {
                boolean canConnect = connectRMI(hostName, portHelper);
                if (canConnect) {
                    ventanaCallback.setSupernode(hostName + ":" + portHelper);
                }
                return canConnect;
            }

            @Override
            public void updateSelected() {
                if (selectedId != null && selectedId.tiempo.getTiempo() > 1) {
                    boolean canConnect = clienteRMI.connect();
                    selectedId.tiempo.setTiempo(15);
                    if (!canConnect) {
                        ventanaCallback.setMessage("Se ha desconectado de " + selectedId.id);
                        clienteMultidifusion.restart();
                        selectedId = null;
                        ventanaCallback.deleteSupernode();
                    } else {
                        readFilesFromFolder();
                    }
                } else {
                    ventanaCallback.setMessage("Se ha desconectado de " + selectedId.id);
                    clienteMultidifusion.restart();
                    selectedId = null;
                    ventanaCallback.deleteSupernode();
                }
            }

            @Override
            public void cleanSuperNode() {
                if (selectedId != null && selectedId.tiempo.getTiempo() < 1) {
                    ventanaCallback.setMessage("Se ha desconectado de " + selectedId.id);
                    clienteMultidifusion.restart();
                    selectedId = null;
                    ventanaCallback.deleteSupernode();
                }
            }

        };
    }
}
