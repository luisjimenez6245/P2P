package proyectop2p.common.Descarga;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import proyectop2p.common.Id;
import proyectop2p.common.MD5;

public class ClienteDescarga implements Runnable {

    private final Id[] ids;
    private final String fileName;
    private final String md5;
    private final String folder;
    private final IDescargaCallback callback;

    public ClienteDescarga(
            Id[] ids,
            String fileName,
            String md5,
            String folder,
            IDescargaCallback callback
    ) {
        this.ids = ids;
        this.fileName = fileName;
        this.md5 = md5;
        this.folder = folder;
        this.callback = callback;
    }

    @Override
    public void run() {
        if (downloadFromServer()) {
            callback.setMessage("archivo descargado correctamante");
        }
    }

    public boolean downloadFromServer() {
        try {
            for (int i = 0; i < ids.length; ++i) {
                Id item = ids[i];
                execute(item.host, item.port, fileName, i, ids.length);
            }

            String md5FromDownload = MD5.obtenerMD5(folder + fileName);
            callback.setMessage("md5 de descarga:" + md5FromDownload);
            callback.setMessage("md5 original:" + this.md5);
            return (md5FromDownload.equals(this.md5));
        } catch (Exception ex) {

        }
        return false;
    }

    private void execute(
            String host,
            int port,
            String filename,
            int partNumber,
            int totalParts
    ) {
        callback.setMessage("intentando connectar a :" + host + ":" + port + " peer:" + partNumber + " totalpeers:" + totalParts);
        try {
            Socket client = new Socket(host, port);
            int buffer = 1024;
            client.setSendBufferSize(buffer);
            client.setReceiveBufferSize(buffer);
            callback.setMessage("conectado a :" + host + ":" + port + " peer:" + partNumber + " totalpeers:" + totalParts);
            boolean nagle = false;
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            output.writeInt(buffer);
            output.flush();
            client.setTcpNoDelay(nagle);
            output.writeBoolean(nagle);
            output.flush();

            output.writeUTF(filename);
            output.flush();

            output.writeInt(partNumber);
            output.flush();
            output.writeInt(totalParts);
            output.flush();

            callback.setMessage("Comenzando descarga");
            reciveFile(inputStream, buffer, partNumber, totalParts);
            inputStream.close();
            client.close();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }

    private void reciveFile(
            DataInputStream inputStream, int buffer, int peerNumber, int totalPeers
    ) throws IOException, InterruptedException {
        long fileSize = inputStream.readLong();
        byte[] b = new byte[buffer];
        callback.setMessage("El archivo pesa" + fileSize);

        DataOutputStream fileWriter = new DataOutputStream(new FileOutputStream(folder + fileName));
        long bytesToSend = (long) Math.floor(fileSize / totalPeers);
        long start = bytesToSend * peerNumber;
        long stop = (totalPeers * bytesToSend);
        if ((peerNumber + 1) == totalPeers && fileSize > stop) {
            stop = fileSize;
        }
        callback.setMessage("Recibiendo archivo start: " + start + "");
        callback.setMessage("Recibiendo archivo stop: " + stop + "");
        long recivedBytes = start;
        int n;
        while (recivedBytes < stop) {
            n = inputStream.read(b);
            fileWriter.write(b, 0, n);
            recivedBytes = recivedBytes + n;
            fileWriter.flush();
        }
        callback.setMessage("Recibimos el archivo: " + fileName + " con éxito");
        fileWriter.close();
        Thread.sleep(100);
    }

}
