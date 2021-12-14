package proyectop2p.supernodo;

import proyectop2p.common.multidifusion.IServidor;

public class ServidorMultidifusion extends IServidor {

    protected ServidorMultidifusion(int port, String networkInterfaceName) {
        super(port, networkInterfaceName, "228.1.1.10");
    }

    @Override
    protected void action() throws Exception {
        defaultAction("S");        
    }
    
}
