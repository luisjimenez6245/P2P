/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erick
 */
public class ServidorRMI implements Runnable, FuncionesRMI{
    
    private final ID id;
    private File directorio;
    private SuperNodo superNodo;
    int num, cont;
    List<String> ListaNombres;
    List<Integer> ListaUbicaciones;
    List<String> ListaMD5;

    public ServidorRMI(SuperNodo nodo) {
        this.superNodo = nodo;
        this.id = nodo.id;
        num = 0;
        cont = 0;
        ListaNombres = new ArrayList<>();
        ListaUbicaciones = new ArrayList<>();
        ListaMD5 = new ArrayList<>();
    }
    
    public void run() {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(id.getPuerto());
            System.out.println("RMI registro listo en el puerto "+ id.getPuerto());
            System.out.println(java.rmi.registry.LocateRegistry.getRegistry(id.getPuerto()));
        } catch (Exception e) {
            System.out.println("Excepcion RMI del registry:");
            e.printStackTrace();
        }//catch
        try {
            System.setProperty("java.rmi.server.codebase", "file:/tmp/Archivos/");
            FuncionesRMI stub = (FuncionesRMI) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(id.getPuerto());
            registry.bind("FuncionesRMI", stub);
            
            System.out.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void NC(char opc) throws RemoteException{
        if(opc == 'c')
            num++;
        else if (opc == 'd')
            num--;
    }
    
    @Override
    public int ConectadosN() throws RemoteException{
        return num;
    }
        
    @Override
    public void setNombre(int pos, String nombre, char opc) throws RemoteException{
        if(opc == 'a')
            ListaNombres.add(nombre);
        else if(opc == 'r')
            ListaNombres.set(pos, nombre);
        
            
    }
    
    @Override
    public boolean ContieneArchivo(String nombre, int ubicacion) throws RemoteException{
        int tam = ListaNombres.size();
        
        for(int i = 0; i < tam; i++){
            if(ListaNombres.get(i).equals(nombre) && ListaUbicaciones.get(i) == ubicacion)
                return true;
        }
        
        return false;
    }
    
    @Override
    public void setUbicacion(int pos, int ubicacion, char opc) throws RemoteException{
        if(opc == 'a')
            ListaUbicaciones.add(ubicacion);
        else if(opc == 'r')
            ListaUbicaciones.set(pos, ubicacion);
        
            
    }
    
    @Override
    public void setMD5(int pos, String md5, char opc) throws RemoteException{
        if(opc == 'a')
            ListaMD5.add(md5);
        else if(opc == 'r')
            ListaMD5.set(pos, md5);
        
    }
    
    @Override
    public void setCantidadArchivos(char opc) throws RemoteException{
        if(opc == 'i')
            cont++;
        else if(opc == 'd')
            cont--;
    }
    
    @Override
    public int getCantidadArchivos() throws RemoteException{
        return cont;
    }
    
    @Override
    public int buscar(String nombre) throws RemoteException{
        int i = 0;
        
        for(String n: ListaNombres){

            if(n.equals(nombre))
                return ListaUbicaciones.get(i);
            
            i++;
        }
        
        return -1;
    }
    
    @Override
    public List<String> getNombre() throws RemoteException{
        return ListaNombres;
    }
        
    @Override
    public List<Integer> getListaUbicaciones() throws RemoteException{
        return ListaUbicaciones;
    }

    @Override
    public List<String> getListaMD5() throws RemoteException{
        return ListaMD5;
    }

    @Override
    public void remover(int p) throws RemoteException{
        ListaNombres.remove(p);
        ListaUbicaciones.remove(p);
        ListaMD5.remove(p);
    }
}
