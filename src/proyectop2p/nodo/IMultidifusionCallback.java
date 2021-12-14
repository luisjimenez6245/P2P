package proyectop2p.nodo;

public interface IMultidifusionCallback {

    public void setMessage(String message);
    
    public boolean connect(String host, int port);
    
}
