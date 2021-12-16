package proyectop2p.supernodo;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;
import proyectop2p.rmi.FuncionesRMI;
import proyectop2p.rmi.ISupernodeCallback;
import proyectop2p.rmi.ServidorRMI;

public class SuperNodo {

    public String ip;
    public int port;
    private final Id id;
    private final ClienteMultidifusion clienteMultidifusion;
    private final ServidorMultidifusion servidorMultidifusion;

    public final Map<String, Id> mapNodes;
    public final Map<String, Id> mapSuperNodes;
    public final Map<String, ClienteRMI> mapClientRMI;

    public final Map<String, List<Archivo>> mapArchivosSupernodos;
    public final Map<String, List<Archivo>> mapArchivosNodos;

    private Thread threadServidorMultidifusion;
    private Thread threadClienteMultidifusion;
    private ServidorRMI servidorRMI;

    public SuperNodo(String networkInterfaceName, String ip, int port) {
        clienteMultidifusion = new ClienteMultidifusion(ip, port, networkInterfaceName, createMultidifusionCallback());
        servidorMultidifusion = new ServidorMultidifusion(port, networkInterfaceName);

        mapNodes = new HashMap<>();
        mapSuperNodes = new HashMap<>();
        mapClientRMI = new HashMap<>();

        mapArchivosSupernodos = new HashMap<>();
        mapArchivosNodos = new HashMap<>();

        Id idHelper = new Id();
        idHelper.id = ip + ":" + port;
        idHelper.host = ip;
        idHelper.port = port;
        idHelper.isSuperNode = true;
        this.id = idHelper;
        this.ip = ip;
        this.port = port;
        System.out.println("Supernodo: " + ip + ":" + port);
    }

    public void stop() {
        clienteMultidifusion.stop();
        servidorMultidifusion.stop();
    }

    public void init() {
        threadServidorMultidifusion = new Thread(servidorMultidifusion);
        threadServidorMultidifusion.start();
        threadClienteMultidifusion = new Thread(clienteMultidifusion);
        threadClienteMultidifusion.start();
        servidorRMI = new ServidorRMI(id, createISupernodeCallback());
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
            FuncionesRMI stub = (FuncionesRMI) UnicastRemoteObject.exportObject(servidorRMI, 0);
            System.out.println("RMI registro listo.");
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("FuncionesRMI", stub);
        } catch (AlreadyBoundException | RemoteException e) {
            System.out.println("aa");
            System.err.println(e);
        }
    }

    private void deleteSuperNode(String idNode, Id item) {
        System.err.println("port matar: " + idNode);
        if (mapSuperNodes.containsKey(idNode)) {
            mapSuperNodes.remove(idNode);
            System.err.println("se murio el servidor " + idNode);
        }
        if (mapArchivosSupernodos.containsKey(idNode)) {
            mapArchivosSupernodos.remove(idNode);
        }
        if (mapClientRMI.containsKey(idNode)) {
            mapClientRMI.remove(idNode);
        }

    }

    public void deleteNode(String idNode, Id item) {
        if (mapNodes.containsKey(idNode)) {
            mapNodes.remove(idNode);
        }
        if (mapArchivosNodos.containsKey(idNode)) {
            mapArchivosNodos.remove(idNode);
        }

    }

    private boolean addSupernode(String idNode, Id supernode) {
        if (!mapSuperNodes.containsKey(idNode)) {
            if (supernode.isSuperNode) {
                if (!mapSuperNodes.containsKey(idNode)) {
                    mapSuperNodes.put(idNode, supernode);
                    Thread h = new Thread(supernode.tiempo);
                    h.start();
                    System.out.println("intentando rmi");
                    ClienteRMI clienteRMI = new ClienteRMI(
                            supernode.host,
                            supernode.port,
                            id
                    );

                    mapClientRMI.put(idNode, clienteRMI);
                    System.out.println("super nodo conectado por rmi");
                    if (clienteRMI.connect()) {
                        mapArchivosSupernodos.put(idNode, clienteRMI.updateFiles());
                    }
                    Thread t = new Thread(() -> {
                        while (clienteRMI.connect()) {
                            System.out.println("cree cliente para rmi hilo");
                            if (mapArchivosSupernodos.containsKey(idNode)) {
                                mapArchivosSupernodos.put(idNode, clienteRMI.updateFiles());
                            }
                            try {
                                Thread.sleep(60000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SuperNodo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    t.start();
                    return true;
                }
            }
        }
        return false;

    }

    private boolean addNode(String nodeId, Id node) {
        if (!mapNodes.containsKey(nodeId)) {
            if (mapNodes.size() < 2) {
                if (!node.isSuperNode) {
                    node.tiempo.setTiempo(15);
                    Thread t = new Thread(node.tiempo);
                    t.start();
                    if (!mapNodes.containsKey(nodeId)) {
                        mapNodes.put(nodeId, node);
                        return true;
                    }
                }
            }
        } else {
            node = mapNodes.get(nodeId);
            node.tiempo.setTiempo(15);
            return true;
        }
        return false;
    }

    private ISupernodeCallback createISupernodeCallback() {
        return new ISupernodeCallback() {
            @Override
            public boolean connectSupernode(Id supernode) {
                return addSupernode(supernode.id, supernode);
            }

            @Override
            public boolean connectNode(Id idNode) {
                return addNode(idNode.id, idNode);
            }

            @Override
            public List<Archivo> getArchivos() {
                List<Archivo> archivos = new ArrayList<>();
                mapArchivosNodos.forEach((t, u) -> {
                    archivos.addAll(u);
                });
                return archivos;
            }

            @Override
            public void updateSharedFiles(Id idNode, List<Archivo> archivos) {
                if (idNode.isSuperNode) {
                    if (mapSuperNodes.containsKey(idNode.id)) {
                        mapArchivosSupernodos.put(idNode.id, archivos);
                    }
                } else {
                    if (mapNodes.containsKey(idNode.id)) {
                        mapArchivosNodos.put(idNode.id, archivos);
                    }
                }
            }

            @Override
            public Archivo[] searchArchivo(String name) {
                List<Archivo> archivo = new ArrayList<>();
                mapClientRMI.forEach((t, u) -> {
                    u.searchArchivo(name);
                });
                Archivo[] result = new Archivo[archivo.size()];
                result = archivo.toArray(result);
                return result;
            }
        };
    }

    private IMultidifusionCallback createMultidifusionCallback() {
        return new IMultidifusionCallback() {
            @Override
            public void addSuperNode(Id superNode) {
                addSupernode(superNode.id, superNode);
            }

            @Override
            public boolean checkIfSuperNodeExists(String idNode) {
                return mapSuperNodes.containsKey(idNode);
            }

            @Override
            public void cleanSuperNodes() {
                Map<String, Id> helper = new HashMap<>();
                mapSuperNodes.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 1) {
                        helper.put(key, item);
                    }
                });
                helper.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 1) {
                        deleteSuperNode(key, item);
                    }
                });
            }

            @Override
            public void addTimeSuperNode(String idNode) {
                if (mapSuperNodes.containsKey(idNode)) {
                    System.out.println("agrendo timepo a super" + idNode);
                    Id item = mapSuperNodes.get(idNode);
                    item.tiempo.setTiempo();
                }
            }

            @Override
            public void cleanNodes() {
                Map<String, Id> helper = new HashMap<>();
                mapNodes.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 1) {
                        helper.put(key, item);
                    }
                });
                helper.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 1) {
                        deleteNode(key, item);
                    }
                });
            }

            @Override
            public void addTimeNode(String id) {
                if (mapNodes.containsKey(id)) {
                    Id item = mapNodes.get(id);
                    item.tiempo.setTiempo();
                }
            }

        };
    }

}
