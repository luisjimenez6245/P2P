package proyectop2p.supernodo;

import java.util.HashMap;
import java.util.Map;
import proyectop2p.common.Id;

public class SuperNodo {

    public String ip;
    public int port;
    private final ClienteMultidifusion clienteMultidifusion;
    private final ServidorMultidifusion servidorMultidifusion;
    public final Map<String, Id> mapNodes;
    public final Map<String, Id> mapSuperNodes;
    private Thread threadServidorMultidifusion;
    private Thread threadClienteMultidifusion;

    public SuperNodo(String networkInterfaceName, String ip, int port) {
        clienteMultidifusion = new ClienteMultidifusion(ip, port, networkInterfaceName, createMultidifusionCallback());
        servidorMultidifusion = new ServidorMultidifusion(port, networkInterfaceName);
        mapNodes = new HashMap<>();
        mapSuperNodes = new HashMap<>();
        this.ip = ip;
        this.port = port;
        System.out.println("SuperNodo: " + ip + ":" + port);
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
    }

    private void deleteSuperNode(String id, Id item) {
        System.err.println("port matar: " + id);
        if (mapSuperNodes.containsKey(id)) {
            mapSuperNodes.remove(id);
            System.err.println("se murio el servidor " + id);

        }

    }

    private void deleteNode(String id, Id item) {
        if (mapNodes.containsKey(id)) {
            mapNodes.remove(id);

        }

    }

    private IMultidifusionCallback createMultidifusionCallback() {
        return new IMultidifusionCallback() {
            @Override
            public void addSuperNode(Id superNode) {
                System.err.println("connectado supernodo con:" + superNode.id);
                mapSuperNodes.put(superNode.id, superNode);
            }

            @Override
            public void addNode(Id node) {
                System.err.println("connectado nodo con:" + node.id);
                mapSuperNodes.put(node.id, node);
            }

            @Override
            public boolean checkIfNodeExists(String id) {
                return mapNodes.containsKey(id);
            }

            @Override
            public boolean checkIfSuperNodeExists(String id) {
                return mapSuperNodes.containsKey(id);
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
            public void cleanNodes() {
                Map<String, Id> helper = new HashMap<>();
                mapNodes.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 0) {
                        helper.put(key, item);
                    }
                });
                helper.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 0) {
                        deleteNode(key, item);
                    }
                });
            }

            @Override
            public void addTimeSuperNode(String id) {
                if (mapSuperNodes.containsKey(id)) {
                    System.out.println("agrendo timepo a super" + id);
                    Id item = mapSuperNodes.get(id);
                    item.tiempo.setTiempo();
                }
            }

            @Override
            public void addTimeNode(String id) {
                if (mapNodes.containsKey(id)) {
                    Id item = mapNodes.get(id);
                    item.tiempo.setTiempo();
                }
            }

            @Override
            public boolean canConnectNode() {
                return 2 > mapNodes.size();
            }
        };
    }

}
