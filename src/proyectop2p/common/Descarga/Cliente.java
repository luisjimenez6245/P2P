package proyectop2p.common.Descarga;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente implements Runnable {

    private final int ubicacion;
    private final String nombreArchivo;
    private final File directorio;
    private final String host;

    public Cliente(
        int ubicacion, 
        String host, 
        String nombreArchivo, 
        File directorio
    ) {
        this.ubicacion = ubicacion;
        this.host = host;
        this.nombreArchivo = nombreArchivo;
        this.directorio = directorio;
    }

    @Override
    public void run() {
        try {
            Socket cl = new Socket(host, ubicacion);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            dos.writeUTF(nombreArchivo);
            dos.flush();
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            FileOutputStream inputStream = new FileOutputStream(directorio.getAbsolutePath()  + "/" + nombre);
            DataOutputStream dosArchivo = new DataOutputStream(inputStream);
            long recibidos = 0;
            int n;
            byte[] b = new byte[1024];

            while (recibidos < tam) {
                n = dis.read(b);
                dosArchivo.write(b, 0, n);
                dosArchivo.flush();
                recibidos = recibidos + n;
            }
            System.out.print("\n\nArchivo recibido.\n");
            dos.close();
            dis.close();
            dosArchivo.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
