package proyectop2p.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Methods extends Remote {

    void setNC(char opc) throws RemoteException;

    int conectadosN() throws RemoteException;

    void setListaConectadsN(int n) throws RemoteException;

    List<Integer> getListaConectadosN() throws RemoteException;

    void setCantidadArchivos(int cant) throws RemoteException;

    int getCantidadArchivos() throws RemoteException;

    void setContarArchivos(char opc) throws RemoteException;

    int getContarArchivos() throws RemoteException;

    void setNodoEliminado(int n) throws RemoteException;

    int getNodoEliminado() throws RemoteException;

    void eliminarNodo(int pos) throws RemoteException;

    int getPreviaCantidad() throws RemoteException;

    void setNombre(int pos, String nombre, char opc) throws RemoteException;

    List<String> getNombre() throws RemoteException;

    boolean contieneArchivo(String nombre, int ubicacion) throws RemoteException;

    void setUbicacion(int pos, int ubicacion, char opc) throws RemoteException;

    List<Integer> getListaUbicaciones() throws RemoteException;

    void setMD5(int pos, String md5, char opc) throws RemoteException;

    List<String> getListaMD5() throws RemoteException;

    int buscar(String nombre) throws RemoteException;

    void remover(int p) throws RemoteException;
}
