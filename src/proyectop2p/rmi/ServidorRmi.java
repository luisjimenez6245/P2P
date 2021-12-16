package proyectop2p.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;

public class ServidorRMI implements FuncionesRMI {

    private final Id id;
    private final ISupernodeCallback callback;

    public ServidorRMI(Id id, ISupernodeCallback callback) {
        super();
        this.id = id;
        this.callback = callback;
    }

    @Override
    public boolean connectSupernode(Id id) throws RemoteException {
        System.err.println("super nodo conectado por rmi: " + id.id);
        callback.connectSupernode(id);
        return true;
    }

    @Override
    public boolean connectNode(Id id) throws RemoteException {
        return callback.connectNode(id);
    }

    @Override
    public Map<Id, List<Archivo>> getFilesOtherFromSuperNode() throws RemoteException {
        List<Archivo> archivos = callback.getArchivos();
        Map<Id, List<Archivo>> map = new HashMap<>();
        map.put(id, archivos);
        return map;
    }

    @Override
    public List<Archivo> getFiles() throws RemoteException {
        return callback.getArchivos();
    }

    @Override
    public void addFiles(Id id, List<Archivo> archivos) throws RemoteException {
        callback.updateSharedFiles(id, archivos);
    }

    @Override
    public Id[] requestFile(String md5) throws RemoteException {
        List<Archivo> archivos = callback.getArchivos();
        List<Id> ids = new ArrayList<>();
        archivos.forEach((t) -> {
            if (t.md5.equals(md5)) {
                if (!ids.contains(t.id)) {
                    ids.add(t.id);
                }
            }
        });
        Id[] results = new Id[ids.size()];
        results = ids.toArray(results);
        return results;
    }

    @Override
    public Archivo[] searchFile(String name) throws RemoteException {
        return searchFile(name, true);
    }

    @Override
    public Archivo[] searchFileSupernode(String name) throws RemoteException {
        return searchFile(name, false);
    }

    private Archivo[] searchFile(String name, boolean shouldAsk) {
        List<Archivo> archivos = callback.getArchivos();
        List<Archivo> ids = new ArrayList<>();
        archivos.forEach((t) -> {
            if (t.name.equals(name)) {
                if (!ids.contains(t)) {
                    ids.add(t);
                }
            }
        });
        Archivo[] results = new Archivo[ids.size()];
        results = ids.toArray(results);
        if (shouldAsk) {
            if (results.length == 0) {
                return callback.searchArchivo(name);
            }
        }
        return results;
    }
}
