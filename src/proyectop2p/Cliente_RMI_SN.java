/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erick
 */
public class Cliente_RMI_SN implements Runnable {

    int pto, ubicacion, cta, cont, cant, aux;
    String nombre, md5;
    Cliente_multidifusion_SN ClienteMulticast;
    FuncionesRMI stub, stub2;
    Registry registry, registry2;
    List<Integer> PuertosSN, PuertosN;
    List<String> ListaNombres;
    List<Integer> ListaUbicaciones;
    List<String> ListaMD5;

    public Cliente_RMI_SN(int pto, Cliente_multidifusion_SN ClienteMulticast) {
        this.pto = pto;
        this.ClienteMulticast = ClienteMulticast;
        PuertosSN = new ArrayList<>();
        PuertosN = new ArrayList<>();
        ListaNombres = new ArrayList<>();
        ListaUbicaciones = new ArrayList<>();
        ListaMD5 = new ArrayList<>();
        cta = 0;
        cont = 0;
        cant = 0;
        aux = 0;
    }

    public void run() {
        for (;;) {
            try {
                PuertosSN = ClienteMulticast.ListaPuertosSN;
                PuertosN = ClienteMulticast.ListaPuertosN;

                for (int p : PuertosSN) {
                    registry2 = LocateRegistry.getRegistry(p);
                    stub2 = (FuncionesRMI) registry2.lookup("FuncionesRMI");

                    ListaNombres.clear();
                    ListaUbicaciones = stub2.getListaUbicaciones();
                    ListaMD5 = stub2.getListaMD5();

                    int tam = ListaUbicaciones.size();
                    cant = 0;

                    for (int i = 0; i < tam; i++) {
                        if (PuertosN.isEmpty()) {
                            ListaNombres.add(stub2.getNombre().get(i));
                            continue;
                        }
                        for (int j = 0; j < PuertosN.size(); j++) {
                            if (!ListaUbicaciones.get(i).equals(PuertosN.get(j))) {
                                ListaNombres.add(stub2.getNombre().get(i));
                            }
                        }
                    }

                    registry = LocateRegistry.getRegistry(pto);
                    stub = (FuncionesRMI) registry.lookup("FuncionesRMI");

                    cant = stub2.getCantidadArchivos();

                    System.out.println("tam " + tam);
                    System.out.println("cant " + cant);
                    System.out.println(ListaNombres);
                    
                        
                    

                    cta = cont;
                    aux = cant;
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                System.err.println("Excepción del cliente: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
