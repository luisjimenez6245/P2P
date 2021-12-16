/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.rmi;

import java.util.List;
import proyectop2p.common.Archivo;
import proyectop2p.common.Id;

/**
 *
 * @author luis
 */
public interface ISupernodeCallback {

    boolean connectSupernode(Id id);

    boolean connectNode(Id id);

    List<Archivo> getArchivos();
        List<Archivo> getAllArchivos();


    void updateSharedFiles(Id id, List<Archivo> archivos);
    
    Archivo[] searchArchivo(String name);

}
