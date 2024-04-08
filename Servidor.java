import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {
    public static void main(String[] args) throws SocketException, IOException {
        int port = 8080;
        int delay = 4000;

        try (DatagramSocket socketUDP = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];
            System.out.println("Servidor UDP escuchando en puerto " + port + "...");

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(request);

                System.out.println("Datagrama recibido del host: " + request.getAddress());
                System.out.println("Desde el puerto remoto: " +  request.getPort());
                System.out.println("Datos recibidos: " + new String(request.getData()));

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String strDate = now.format(formatter);
                System.out.println("La hora del servidor es: " + strDate);

                try {
                    System.out.println("Simulamos un retardo de " + delay + " milisegundos");
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DatagramPacket response = new DatagramPacket(
                    strDate.getBytes(), strDate.getBytes().length, request.getAddress(), request.getPort());
                socketUDP.send(response);
                System.out.println("Datos enviados al cliente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}