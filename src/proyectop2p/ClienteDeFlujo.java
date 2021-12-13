/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p;

/**
 *
 * @author erick
 */
import java.io.*;
import java.net.Socket;

public class ClienteDeFlujo implements Runnable {
    private int ubicacion;
    private String nombreArchivo;
    File directorio;

    public ClienteDeFlujo(int ubicacion, String nombreArchivo, File directorio) {
        this.ubicacion = ubicacion;
        this.nombreArchivo = nombreArchivo;
        this.directorio = directorio;
    }

    @Override
    public void run() {
        try {
            Socket cl = new Socket("localhost", ubicacion);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            dos.writeUTF(nombreArchivo);
            dos.flush();
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            DataOutputStream dos_archivo = new DataOutputStream(
                    new FileOutputStream(directorio.getAbsolutePath() + "/" + nombre));
            long recibidos = 0;
            int n;
            byte[] b = new byte[1024];

            while (recibidos < tam) {
                n = dis.read(b);
                dos_archivo.write(b, 0, n);
                dos_archivo.flush();
                recibidos = recibidos + n;
            }
            System.out.print("\n\nArchivo recibido.\n");
            dos.close();
            dis.close();
            dos_archivo.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
