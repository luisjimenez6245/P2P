package proyectop2p.supernodo;

import proyectop2p.common.multidifusion.IServidor;

public class ServidorMultidifusion extends IServidor {

    protected ServidorMultidifusion(int port, String networkInterfaceName, String multicastAddr) {
        super(port, networkInterfaceName, multicastAddr);
    }

    @Override
    protected void action() throws Exception {
        defaultAction();        
    }
    
}
