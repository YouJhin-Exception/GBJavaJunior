package org.youjhin;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс, реализующий управление клиентом в чате.
 * Реализует интерфейс Runnable для запуска в отдельном потоке.
 */
public class ClientManager implements Runnable {

    // Поля класса
    private BufferedReader bufferedReader; // Поток для чтения данных от клиента
    private BufferedWriter bufferedWriter; // Поток для отправки данных клиенту
    private final Socket socket; // Сокет для установки соединения с клиентом
    private String name; // Имя клиента

    // Статическое поле, хранящее список всех клиентов
    protected static final List<ClientManager> clients = new ArrayList<>();

    /**
     * Конструктор класса. Инициализирует потоки ввода/вывода и имя клиента.
     * Добавляет клиента в список и оповещает о подключении нового клиента.
     * @param socket Сокет для соединения с клиентом.
     */
    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            // Инициализация потоков ввода/вывода
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Чтение имени клиента
            name = bufferedReader.readLine();

            // Добавление клиента в список и оповещение о подключении
            clients.add(this);
            System.out.println(name + " подключился к чату");
            broadcastMessage("Server: " + name + " подключился к чату");

        } catch (Exception e) {
            // В случае ошибки при инициализации, закрытие соединения
            closeAllConnection(socket, bufferedWriter, bufferedReader);
        }
    }

    /**
     * Метод запускаемый в потоке. Обрабатывает сообщения от клиента и рассылает их всем участникам чата.
     */
    @Override
    public void run() {
        String messageFromClient;

        // Пока сокет подключен, читаем сообщения от клиента и рассылаем их
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);

            } catch (IOException e) {
                // В случае ошибки, закрываем соединение и выходим из цикла
                closeAllConnection(socket, bufferedWriter, bufferedReader);
                break;
            }
        }
    }

    /**
     * Метод отправки сообщения конкретному клиенту.
     * @param client Клиент, которому отправляется сообщение.
     * @param message Сообщение для отправки.
     * @throws IOException В случае ошибки ввода/вывода при отправке сообщения.
     */
    private void sendMessage(ClientManager client, String message) throws IOException {
        client.bufferedWriter.write(message);
        client.bufferedWriter.newLine();
        client.bufferedWriter.flush();
    }

    /**
     * Проверяет, что клиент не является текущим экземпляром класса, чтобы избежать отправки сообщения самому себе.
     * @param client Клиент для сравнения.
     * @return true, если клиент не является текущим экземпляром, иначе false.
     */
    private boolean isNotSameClient(ClientManager client) {
        return !client.name.equals(name);
    }

    /**
     * Проверяет, является ли сообщение личным (начинается с символа '@').
     * @param message Сообщение для проверки.
     * @return true, если сообщение личное, иначе false.
     */
    private boolean isPrivateMessage(String message) {
        return message.startsWith("@");
    }

    /**
     * Извлекает имя адресата и отправляет ему личное сообщение.
     * @param client Клиент, которому отправляется личное сообщение.
     * @param message Сообщение с личным адресатом.
     * @throws IOException В случае ошибки ввода/вывода при отправке личного сообщения.
     */
    private void sendPersonalMessage(ClientManager client, String message) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(message);
        int spaceIndex = stringBuilder.indexOf(" ");
        if (spaceIndex != -1){
            String targetName = stringBuilder.substring(1, spaceIndex);
            String personalMessage = stringBuilder.substring(spaceIndex + 1);
            if (targetName.equalsIgnoreCase(client.name)) {
                sendMessage(client, "Персональное сообщение от " + name + ": " + personalMessage);
            }
        }
    }

    /**
     * Обрабатывает сообщение, определяя, является ли оно личным или общим, и отправляет его соответствующим образом.
     * @param client Клиент, отправивший сообщение.
     * @param message Сообщение для обработки.
     * @throws IOException В случае ошибки ввода/вывода при обработке сообщения.
     */
    private void processMessage(ClientManager client, String message) throws IOException {
        if (isPrivateMessage(message)) {
            sendPersonalMessage(client, message);
        } else {
            sendMessage(client, message);
        }
    }

    /**
     * Рассылает сообщение всем клиентам, исключая отправителя.
     * @param message Сообщение для рассылки.
     */
    private void broadcastMessage(String message) {
        for (ClientManager client : clients) {
            try {
                if (isNotSameClient(client)) {
                    processMessage(client, message);
                }
            } catch (IOException e) {
                // В случае ошибки, закрываем соединение отправившего сообщение
                closeAllConnection(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    /**
     * Закрывает все соединения и удаляет клиента из списка.
     * @param socket Сокет для закрытия.
     * @param bufferedWriter Поток вывода для закрытия.
     * @param bufferedReader Поток ввода для закрытия.
     */
    private void closeAllConnection(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClient();
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

    /**
     * Удаляет клиента из списка и оповещает об его выходе из чата.
     */
    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " покинул чат");
        broadcastMessage("Server: " + name + " покинул чат");
    }
}