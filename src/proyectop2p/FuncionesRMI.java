/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

/**
 *
 * @author erick
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FuncionesRMI extends Remote {
    public void NC(char opc) throws RemoteException;
    public int ConectadosN() throws RemoteException;
    public int getCantidadArchivos() throws RemoteException;
    public void setCantidadArchivos(char opc) throws RemoteException;
    public void setNombre(int pos, String nombre, char opc) throws RemoteException;
    public List<String> getNombre() throws RemoteException;
    public boolean ContieneArchivo(String nombre, int ubicacion) throws RemoteException;
    public void setUbicacion(int pos, int ubicacion, char opc) throws RemoteException;
    public List<Integer> getListaUbicaciones() throws RemoteException;
    public void setMD5(int pos, String md5, char opc) throws RemoteException;
    public List<String> getListaMD5() throws RemoteException;
    public int buscar(String nombre) throws RemoteException;
    public void remover(int p) throws RemoteException;
}
