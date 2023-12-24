package org.youjhin;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Класс представляет клиента в чате, обеспечивает взаимодействие с сервером.
 */
public class Client {

    // Поля класса
    private final Socket socket; // Сокет для соединения с сервером
    private final String name; // Имя клиента

    private BufferedWriter bufferedWriter; // Поток для отправки данных серверу
    private BufferedReader bufferedReader; // Поток для чтения данных от сервера

    /**
     * Конструктор класса. Инициализирует сокет и имя клиента.
     * Инициализирует потоки ввода/вывода для обмена данными с сервером.
     * @param socket Сокет для соединения с сервером.
     * @param userName Имя клиента.
     */
    public Client(Socket socket, String userName) {
        this.socket = socket;
        name = userName;

        try {
            // Инициализация потоков ввода/вывода
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            // В случае ошибки при инициализации, закрытие соединения
            closeAllConnection(socket, bufferedWriter, bufferedReader);
        }
    }

    /**
     * Метод запускает отдельный поток для прослушивания сообщений от сервера.
     */
    public void listeningMessage() {
        new Thread(() -> {
            String message;
            while (socket.isConnected()) {
                try {
                    message = bufferedReader.readLine();
                    System.out.println(message);
                } catch (IOException e) {
                    // В случае ошибки, закрываем соединение и выходим из цикла
                    closeAllConnection(socket, bufferedWriter, bufferedReader);
                }
            }
        }).start();
    }

    /**
     * Метод отправляет сообщение серверу.
     * В зависимости от введенного сообщения определяет, является ли оно личным или общим.
     */
    public void sendMessage() {
        try {
            sendMessage(name);

            while (socket.isConnected()) {
                String message = readMessageFromConsole();

                if (message.startsWith("@")) {
                    StringBuilder stringBuilder = new StringBuilder(message);
                    int spaceIndex = stringBuilder.indexOf(" ");
                    if (spaceIndex != -1){
                        String targetName = stringBuilder.substring(1, spaceIndex);
                        String personalMessage = stringBuilder.substring(spaceIndex + 1);
                        sendPersonalMessage(targetName, personalMessage);
                    }

                } else {
                    sendMessage(name + ": " + message);
                }
            }
        } catch (IOException e) {
            // В случае ошибки, закрываем соединение
            closeAllConnection(socket, bufferedWriter, bufferedReader);
        }
    }

    /**
     * Метод отправляет личное сообщение конкретному клиенту.
     * @param targetName Имя адресата.
     * @param personalMessage Личное сообщение для отправки.
     * @throws IOException В случае ошибки ввода/вывода при отправке личного сообщения.
     */
    private void sendPersonalMessage(String targetName, String personalMessage) throws IOException {
        sendMessage("@" + targetName + " " + personalMessage);
    }

    /**
     * Метод отправляет сообщение серверу.
     * @param message Сообщение для отправки.
     * @throws IOException В случае ошибки ввода/вывода при отправке сообщения.
     */
    private void sendMessage(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    /**
     * Метод считывает сообщение с консоли.
     * @return Введенное сообщение.
     */
    private String readMessageFromConsole() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Закрывает все соединения.
     * @param socket Сокет для закрытия.
     * @param bufferedWriter Поток вывода для закрытия.
     * @param bufferedReader Поток ввода для закрытия.
     */
    private void closeAllConnection(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}