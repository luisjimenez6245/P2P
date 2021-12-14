package proyectop2p.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Cliente {

    private int pto, ubicacion, cant, aux;
    private String nombre;
    private String md5;
    private Methods stub;
    private Methods stub2;
    private Registry registry;
    private Registry registry2;
    private List<Integer> PuertosSN;
    private List<String> listaNombres;
    private List<Integer> listaUbicaciones;
    private List<Integer> listaUbicacionesAux;
    private List<String> listaMD5, listaMD5Aux;

    private void conectar(int port) throws RemoteException, NotBoundException {

        /*registry2 = LocateRegistry.getRegistry(port);
        stub2 = (Methods) registry2.lookup("Methods");

        listaNombres = stub2.getNombre();
        listaUbicaciones = stub2.getListaUbicaciones();
        listaMD5 = stub2.getListaMD5();

        int tam = listaUbicacionesAux.size();

        listaNombres.clear();
        listaUbicaciones.clear();
        listaMD5.clear();

        cant = 0;
        for (int i = 0; i < tam; i++) {
            if (stub2.getContarArchivos() > 0) {
                if (stub2.getListaConectadosN().contains(listaUbicacionesAux.get(i))) {
                    listaNombres.add(stub2.getNombre().get(i));
                    listaUbicaciones.add(stub2.getListaUbicaciones().get(i));
                    listaMD5.add(stub2.getListaMD5().get(i));
                    cant++;
                }
            }
        }

        tam = stub.getNombre().size();
        aux = 0;

        listaMD5Aux.clear();
        listaUbicacionesAux.clear();

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < stub2.getListaConectadosN().size(); j++) {
                if (stub.getListaUbicaciones().get(i).equals(stub2.getListaConectadosN().get(j))) {
                    aux++;
                    listaMD5Aux.add(stub.getListaMD5().get(i));
                    listaUbicacionesAux.add(stub.getListaUbicaciones().get(i));
                }
            }

            tam = stub.getNombre().size();
        }*/

    }

    public void run() {
        for (;;) {
            try {
                
                /*
                for (int p : PuertosSN) {

                    for (int i = 0; i < tam; i++) {
                        if (stub2.getContarArchivos() > 0) {
                            if (stub2.getListaConectadosN().contains(listaUbicacionesAux.get(i))) {
                                listaNombres.add(stub2.getNombre().get(i));
                                listaUbicaciones.add(stub2.getListaUbicaciones().get(i));
                                listaMD5.add(stub2.getListaMD5().get(i));
                                cant++;
                            }
                        }
                    }

                    registry = LocateRegistry.getRegistry(pto);
                    stub = (Methods) registry.lookup("Methods");

                    tam = stub.getNombre().size();
                    aux = 0;

                    listaMD5Aux.clear();
                    listaUbicacionesAux.clear();

                    for (int i = 0; i < tam; i++) {
                        for (int j = 0; j < stub2.getListaConectadosN().size(); j++) {
                            if (stub.getListaUbicaciones().get(i).equals(stub2.getListaConectadosN().get(j))) {
                                aux++;
                                listaMD5Aux.add(stub.getListaMD5().get(i));
                                listaUbicacionesAux.add(stub.getListaUbicaciones().get(i));
                            }
                        }

                        tam = stub.getNombre().size();
                    }

                    if (stub2.getListaConectadosN().size() > stub2.getPreviaCantidad()) {
                        int nodo = stub2.getNodoEliminado();
                        tam = stub2.getListaConectadosN().size();

                        for (int i = 0; i < tam; i++) {
                            if (stub2.getListaConectadosN().get(i) == nodo) {
                                stub2.eliminarNodo(i);
                            }
                        }
                    }

                    System.out.println("cant " + cant);
                    System.out.println("aux " + aux);
                    System.out.println("pto " + p);
                    System.out.println(listaNombres);

                    if (aux > cant) {
                        tam = listaMD5Aux.size();
                        int pos;
                        boolean agregado = false;

                        for (int i = 0; i < tam; i++) {
                            for (int j = 0; j < listaMD5.size(); j++) {
                                if (listaMD5.get(j).equals(listaMD5Aux.get(i))) {
                                    if (listaUbicaciones.get(j).equals(listaUbicacionesAux.get(i))) {
                                        agregado = true;
                                        break;
                                    }
                                }
                            }

                            if (!agregado) {
                                pos = 0;
                                System.out.println("c");
                                for (int j = 0; j < stub.getListaMD5().size(); j++, pos++) {
                                    if (stub.getListaMD5().get(j).equals(listaMD5Aux.get(i))) {
                                        if (stub.getListaUbicaciones().get(j).equals(listaUbicacionesAux.get(i))) {
                                            System.out.println("e");
                                            stub.remover(pos);
                                            pos--;
                                        }
                                    }
                                }
                            }

                            agregado = false;
                        }
                    }

                    for (int i = 0; i < cant; i++) {
                        nombre = listaNombres.get(i);
                        ubicacion = listaUbicaciones.get(i);
                        md5 = listaMD5.get(i);

                        tam = stub.getNombre().size();

                        if (cant > aux || aux == 0) {
                            if (!stub.contieneArchivo(nombre, ubicacion)) {
                                stub.setNombre(0, nombre, 'a');
                                stub.setUbicacion(0, ubicacion, 'a');
                                stub.setMD5(0, md5, 'a');
                            }
                        } else {
                            for (int j = 0; j < tam; j++) {
                                if (tam <= stub.getListaMD5().size()) {
                                    if (stub.getListaMD5().get(j).equals(md5)
                                            && stub.getListaUbicaciones().get(j).equals(ubicacion)) {
                                        stub.setNombre(j, nombre, 'r');
                                        stub.setUbicacion(j, ubicacion, 'r');
                                        stub.setMD5(j, md5, 'r');
                                    }
                                }
                            }
                        }
                    }
                }*/
                Thread.sleep(4333);
            } catch (Exception e) {
                System.err.println("Excepción del cliente: " + e.toString());
                e.printStackTrace();
            }
        }
    }

}
