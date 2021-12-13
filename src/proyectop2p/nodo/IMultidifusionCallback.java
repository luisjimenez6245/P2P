package proyectop2p.nodo;

public interface IMultidifusionCallback {


    public void crearCarpeta(String nombre);
    public boolean disponiblidadSuperNodo();
    public void conectar(String ip, int puerto);
    public String getHostInfo();
    public void listarArchivos();
    public void setNC();
}
