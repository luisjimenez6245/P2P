package proyectop2p.common.Descarga;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorDescarga implements Runnable {

    private final int port;
    private final String path;
    private ServerSocket server;
    private final IDescargaCallback callback;

    public ServidorDescarga(
            int port,
            String path,
            IDescargaCallback callback
    ) {
        this.path = path;
        this.port = port;
        this.callback = callback;
    }

    public void init() {
        try {
            server = new ServerSocket(port);
            callback.setMessage("Se inició el serividor de flujo");
            System.out.println("Se inicio el servidor");
        } catch (IOException ex) {
            Logger.getLogger(ServidorDescarga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendFile(
            File file,
            int buffer,
            DataOutputStream output,
            int peerNumber,
            int totalPeers
    ) throws IOException {
        long fileSize = file.length();

        output.writeLong(fileSize);
        output.flush();
        String fileName = file.getAbsolutePath();
        callback.setMessage("Enviando archivo: " + fileName + "");
        byte[] b = new byte[buffer];
        long bytesToSend = (long) Math.floor(fileSize / totalPeers);
        long start = bytesToSend * peerNumber;
        long stop = bytesToSend + start;
        if ((peerNumber + 1) == totalPeers && fileSize > stop) {
            stop = fileSize;
        }
        callback.setMessage("Enviando archivo start: " + start + "");
        callback.setMessage("Enviando archivo stop: " + stop + "");
        RandomAccessFile rF = new RandomAccessFile(fileName, "r");
        rF.seek(start);
        while (start < stop) {
            int n = rF.read(b);

            output.write(b, 0, n);
            start = start + n;
            System.err.println("bytes:" + n + " totales:" + (start));
            output.flush();
        }
        System.out.println("Archivo: " + fileName + " enviado con éxito");
        rF.close();
    }

    @Override
    public void run() {

        for (;;) {
            try {
                Socket client = server.accept();
                client.setKeepAlive(true);
                InetAddress clientIp = client.getInetAddress();
                int clientPort = client.getPort();
                System.out.println("Conexión establecida desde" + clientIp.getHostName() + ":" + clientPort);
                DataInputStream inputStream = new DataInputStream(client.getInputStream());

                int buffer = inputStream.readInt();

                client.setSendBufferSize(buffer);
                client.setReceiveBufferSize(buffer);
                client.setTcpNoDelay(inputStream.readBoolean());

                System.out.println("El tamaño del buffer de envio de paquetes es:" + client.getSendBufferSize());
                System.out.println("El tamaño del buffer para recibir paquetes es:" + client.getReceiveBufferSize());

                String fileName = inputStream.readUTF();
                int partNumber = inputStream.readInt();
                int totalParts = inputStream.readInt();

                DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

                callback.setMessage("Se van a enviar a " + clientIp.getHostName() + ":" + clientPort + " el archivo: " + path + fileName + " siendo el peer " + partNumber + " de " + totalParts);
                File file = new File(path + fileName);
                if (file.exists()) {
                    sendFile(file, buffer, outputStream, partNumber, totalParts);
                }
                outputStream.close();
                client.close();
            } catch (IOException exception) {
                System.err.println(exception);
            }

        }
    }

}
