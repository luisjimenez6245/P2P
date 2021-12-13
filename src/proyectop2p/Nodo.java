/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import proyectop2p.common.Mensaje;

/**
 *
 * @author erick
 */
public class Nodo {

    private final ClienteMultidifusionNodo ClienteMulticastNodo;
    ClienteRMINodo ClienteRMI;
    ServidorDeFlujo ServidorFlujo;
    int pto, aux;
    boolean primera;
    ID mio;
    Mensaje mensaje;
    File directorio;
    List<String> Nombres;

    public Nodo(String IP, int puerto) throws InterruptedException {
        mio = new ID(IP, puerto);
        ClienteMulticastNodo = new ClienteMultidifusionNodo(puerto, this);
        new Thread(ClienteMulticastNodo).start();
    }

    public Nodo getNodo() {
        return this;
    }

    public int getPto() {
        return pto;
    }

    public void setPto(int pto) {
        this.pto = pto;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void Conectar() {
        ClienteRMI = new ClienteRMINodo(pto);
        ClienteRMI.Conectar();
    }

    public boolean DisponibilidadSN() {
        boolean d = false;
        try {
            int num = ClienteRMI.stub.ConectadosN();

            if (num < 2) {
                d = true;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return d;
    }

    public void setNC() {
        try {
            ClienteRMI.stub.NC('c');

        } catch (RemoteException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CrearCarpeta(String puerto) {
        String NuevaCarpeta = "Carpetas/" + puerto;
        File carpeta = new File(NuevaCarpeta);
        carpeta.mkdir();

        directorio = carpeta;

        ServidorFlujo.setDirectorio(directorio);
    }

    public void ListarArchivos() {
        try {
            File[] archivos = this.directorio.listFiles();
            String nombre;
            int ubicacion = mio.getPuerto();

            int cant = archivos.length;
            
            System.out.println("aux " + aux);
            System.out.println("cant " + cant);
            if (aux > cant) {
                System.out.println("a");
                boolean agregado = false;
                int pos = 0;

                for (int i = 0; i < aux; i++,pos++) {
                    for (int j = 0; j < cant; j++) {
                        if (ClienteRMI.stub.getNombre().get(i).equals(archivos[j].getName())) {
                            agregado = true;
                            break;
                        }
                    }

                    if (!agregado) {
                        System.out.println("b");
                        ClienteRMI.stub.remover(pos);
                        aux--;
                        pos--;
                    }
                }
            }

            for (File archivo : archivos) {
                int tam = ClienteRMI.stub.getNombre().size();

                nombre = archivo.getName();

                String m = md5.ObtenerMD5();

                if (cant > aux && aux > 0) {
                    if (!ClienteRMI.stub.ContieneArchivo(nombre, ubicacion)) {
                        ClienteRMI.stub.setNombre(0, nombre, 'a');
                        ClienteRMI.stub.setUbicacion(0, ubicacion, 'a');
                        ClienteRMI.stub.setMD5(0, m, 'a');
                    }
                }

                if (primera) {
                    ClienteRMI.stub.setNombre(0, nombre, 'a');
                    ClienteRMI.stub.setUbicacion(0, ubicacion, 'a');
                    ClienteRMI.stub.setMD5(0, m, 'a');
                } else {
                    for (int i = 0; i < tam; i++) {
                        if (tam <= ClienteRMI.stub.getListaMD5().size()) {
                            if (ClienteRMI.stub.getListaMD5().get(i).equals(m) && ClienteRMI.stub.getListaUbicaciones().get(i).equals(ubicacion)) {
                                ClienteRMI.stub.setNombre(i, nombre, 'r');
                                ClienteRMI.stub.setUbicacion(i, ubicacion, 'r');
                                ClienteRMI.stub.setMD5(i, m, 'r');
                            }
                        }
                    }
                }

                
            }

            aux = cant;
            primera = false;
        } catch (RemoteException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int buscar(String nombre) {
        int ubicacion = 0;

        try {
            ubicacion = ClienteRMI.stub.buscar(nombre);
        } catch (RemoteException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ubicacion;
    }

    public File ObtenerDirectorio() {
        return directorio;
    }
}