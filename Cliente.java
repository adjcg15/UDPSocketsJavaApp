import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
    public static void main(String[] args) 
    throws SocketException, UnknownHostException, IOException {
        String message = new String("Dame la hora local");
        String server = new String("localhost");

        int port = 8080;
        int delay = 5000;

        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress serverHost = InetAddress.getByName(server);

        DatagramPacket request = new DatagramPacket(
            message.getBytes(), message.getBytes().length, serverHost, port);

        socketUDP.setSoTimeout(delay);
        System.out.println("Esperamos datos un maximo de " + delay + " milisegundos...");
        socketUDP.send(request);

        try {
            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(response);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String strText = new String(response.getData(), 0, response.getLength());
            LocalDateTime serverHour = LocalDateTime.parse(strText, formatter);
            LocalDateTime clientHour = LocalDateTime.now();
            double differenceInSeconds = Duration.between(serverHour, clientHour).toMillis() / 1000.0;

            System.out.println("La hora del servidor es: " + serverHour.format(formatter));
            System.out.println("La hora del cliente es: " + clientHour.format(formatter));
            System.out.println("La diferencia entre las dos es: " + differenceInSeconds + " segundos");
        } catch (SocketTimeoutException e) {
            System.out.println("Tiempo expirado para recibir la respuesta: " + e.getMessage());
        }

        socketUDP.close();
    }
}
