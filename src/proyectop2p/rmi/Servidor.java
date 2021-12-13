package proyectop2p.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Servidor implements Methods {

    private final int port;
    private int num;
    private int cant;
    private int cont;
    private int previa;
    private int n;
    private List<String> listaNombres;
    private List<Integer> listaUbicaciones;
    private List<String> listaMD5;
    private List<Integer> listaNodos;

    public Servidor(int port) {
        super();
        this.port = port;
    }

    public void run() {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
            System.out.println("RMI registro listo en el puerto " + port);
            System.out.println(java.rmi.registry.LocateRegistry.getRegistry(port));
        } catch (Exception e) {
            System.out.println("Excepcion RMI del registry:");
            e.printStackTrace();
        } // catch
        try {
            System.setProperty("java.rmi.server.codebase", "file:/tmp/Archivos/");
            Methods stub = (Methods) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("Methods", stub);
            System.out.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void setNC(char opc) throws RemoteException {
        if (opc == 'c')
            num++;
        else if (opc == 'd')
            num--;
    }

    @Override
    public int conectadosN() throws RemoteException {
        return num;
    }

    @Override
    public void setNombre(int pos, String nombre, char opc) throws RemoteException {
        if (opc == 'a')
            listaNombres.add(nombre);
        else if (opc == 'r')
            listaNombres.set(pos, nombre);

    }

    @Override
    public boolean contieneArchivo(String nombre, int ubicacion) throws RemoteException {
        int tam = listaNombres.size();

        for (int i = 0; i < tam; i++) {
            if (listaNombres.get(i).equals(nombre) && listaUbicaciones.get(i) == ubicacion)
                return true;
        }

        return false;
    }

    @Override
    public void setUbicacion(int pos, int ubicacion, char opc) throws RemoteException {
        if (opc == 'a')
            listaUbicaciones.add(ubicacion);
        else if (opc == 'r')
            listaUbicaciones.set(pos, ubicacion);

    }

    @Override
    public void setMD5(int pos, String md5, char opc) throws RemoteException {
        if (opc == 'a')
            listaMD5.add(md5);
        else if (opc == 'r')
            listaMD5.set(pos, md5);

    }

    @Override
    public void setCantidadArchivos(int cant) throws RemoteException {
        this.cant = cant;
    }

    @Override
    public int getCantidadArchivos() throws RemoteException {
        return cant;
    }

    @Override
    public void setContarArchivos(char opc) throws RemoteException {
        if (opc == 'i')
            cont++;
        else if (opc == 'd')
            cont--;
    }

    @Override
    public int getContarArchivos() throws RemoteException {
        return cont;
    }

    @Override
    public int buscar(String nombre) throws RemoteException {
        int i = 0;

        for (String item : listaNombres) {

            if (item.equals(nombre))
                return listaUbicaciones.get(i);

            i++;
        }

        return -1;
    }

    @Override
    public List<String> getNombre() throws RemoteException {
        return listaNombres;
    }

    @Override
    public List<Integer> getListaUbicaciones() throws RemoteException {
        return listaUbicaciones;
    }

    @Override
    public List<String> getListaMD5() throws RemoteException {
        return listaMD5;
    }

    @Override
    public void remover(int p) throws RemoteException {
        listaNombres.remove(p);
        listaUbicaciones.remove(p);
        listaMD5.remove(p);
    }

    @Override
    public void setListaConectadsN(int n) throws RemoteException {
        listaNodos.add(n);
        previa++;
    }

    @Override
    public List<Integer> getListaConectadosN() throws RemoteException {
        return listaNodos;
    }

    @Override
    public int getPreviaCantidad() throws RemoteException {
        return previa;
    }

    @Override
    public void setNodoEliminado(int n) throws RemoteException {
        this.n = n;
    }

    @Override
    public int getNodoEliminado() throws RemoteException {
        return n;
    }

    @Override
    public void eliminarNodo(int pos) throws RemoteException {
        listaNodos.remove(pos);
        previa--;
    }

}
