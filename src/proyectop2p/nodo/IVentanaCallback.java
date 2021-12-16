/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.nodo;

/**
 *
 * @author luis
 */
public interface IVentanaCallback {

    public void setMessage(String message);

    public void setSupernode(String supernodeId);

    public void deleteSupernode();

}
