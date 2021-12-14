package proyectop2p.supernodo;

import java.util.HashMap;
import java.util.Map;
import proyectop2p.common.Id;

public class SuperNodo {

    public String ip;
    public int port;
    public ClienteMultidifusion clienteMultidifusion;
    private final Map<String, Id> mapNodes;
    private final Map<String, Id> mapSuperNodes;

    public SuperNodo(String ip, int port) {
        clienteMultidifusion = new ClienteMultidifusion(port, "lo", createMultidifusionCallback());
        mapNodes = new HashMap<>();
        mapSuperNodes = new HashMap<>();
    }

    public void init() {
        Thread threadServidorMultidifusion = new Thread(
                new ServidorMultidifusion(port, "lo"));
        threadServidorMultidifusion.start();
        Thread threadClienteMultidifusion = new Thread(clienteMultidifusion);
        threadClienteMultidifusion.start();
    }

    private void deleteSuperNode(String id, Id item) {

    }

    private void deleteNode(String id, Id Item) {
        if(mapNodes.containsKey(id)){
            mapNodes.remove(id);
            
        }

    }

    private IMultidifusionCallback createMultidifusionCallback() {
        return new IMultidifusionCallback() {
            @Override
            public void addSuperNode(Id superNode) {
                mapSuperNodes.put(superNode.id, superNode);
            }

            @Override
            public void addNode(Id node) {
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
                mapSuperNodes.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 0) {
                        deleteSuperNode(key, item);
                    }
                });
            }

            @Override
            public void cleanNodes() {
                mapNodes.forEach((key, item) -> {
                    if (item.tiempo.getTiempo() <= 0) {
                        deleteSuperNode(key, item);
                    }
                });
            }

            @Override
            public void addTimeSuperNode(String id) {
                if (mapSuperNodes.containsKey(id)) {
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
