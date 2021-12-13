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

public class Cliente_de_flujo implements Runnable{
    private int ubicacion;
    private String nombre_ar;
    File directorio;
    
    public Cliente_de_flujo(int ubicacion, String nombre_ar, File directorio){
        this.ubicacion = ubicacion;
        this.nombre_ar = nombre_ar;
        this.directorio = directorio;
    }
    
    @Override
    public void run() {
        try {
            Socket cl = new Socket("localhost", ubicacion);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            dos.writeUTF(nombre_ar);
            dos.flush();
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            DataOutputStream dos_archivo = new DataOutputStream(new FileOutputStream(directorio.getAbsolutePath()+"/"+nombre));
            long recibidos=0;
            int n;
            byte[] b = new byte[1024];
            
            while(recibidos < tam){
                n = dis.read(b);
                dos_archivo.write(b,0,n);
                dos_archivo.flush();
                recibidos = recibidos + n;
            }//While
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
