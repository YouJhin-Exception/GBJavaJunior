package org.youjhin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartServer {
    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(1234)){
            Server server = new Server(serverSocket);
            server.runServer();


        }catch (IOException e) {
            e.printStackTrace();
        }



    }
}