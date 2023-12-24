package org.youjhin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StartClient {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ваше имя: ");

        String name = scanner.nextLine();

        try {
            try (Socket socket = new Socket("127.0.0.1", 1234)) {
                InetAddress address = socket.getInetAddress();
                System.out.println("Inet address " + address);
                String remoteAddress = address.getHostAddress();
                System.out.println("Remote address: " + remoteAddress);
                System.out.println("Local port: " + socket.getLocalPort());

                Client client = new Client(socket, name);
                client.listeningMessage();
                client.sendMessage();

            }

        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}