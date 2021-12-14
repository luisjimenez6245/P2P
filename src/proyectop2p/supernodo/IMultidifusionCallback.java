/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.supernodo;

import proyectop2p.common.Id;

/**
 *
 * @author luis
 */
public interface IMultidifusionCallback {

    public void addSuperNode(Id superNode);

    public void addNode(Id node);

    public void cleanSuperNodes();

    public void cleanNodes();

    public boolean canConnectNode();

    public boolean checkIfNodeExists(String id);

    public boolean checkIfSuperNodeExists(String id);

    public void addTimeSuperNode(String id);

    public void addTimeNode(String id);

}
