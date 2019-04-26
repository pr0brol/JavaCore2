package Java.ru.geekbrains.lesson7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static Java.ru.geekbrains.lesson4.MessagePatterns.MESSAGE_SEND_PATTERN;


public class ClientHandler {
    private final String login;
    private final Socket socket;
    private final DataOutputStream outStream;
    private final DataInputStream inStream;
    private final Thread handlerThread;
    private ChatServer chatServer;

    public ClientHandler(String login, Socket socket, ChatServer chatServer) throws IOException{
        this.login = login;
        this.socket = socket;
        this.inStream = new DataInputStream(socket.getInputStream());
        this.outStream = new DataOutputStream(socket.getOutputStream());
        this.chatServer = chatServer;

        this.handlerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        String msg = inStream.readUTF();
                        System.out.printf("Сообщение от пользователя %s: %s%n", login, msg);
                        //todo проверить является ли msg сообщением для пользователся
                        //todo если да, то отправить его
                        String userTo = "";
                        String message = "";
                        sendMessage(userTo, message);
                        chatServer.sendMessage(userTo, login, message);
                    }catch (IOException ex){
                        ex.printStackTrace();
                        break;
                    }
                }
            }
        });
        this.chatServer = chatServer;
        this.handlerThread.start();
    }

    public String getLogin(){
        return login;
    }

    public void sendMessage(String userTo, String msg) throws IOException{
        outStream.writeUTF(String.format(MESSAGE_SEND_PATTERN, userTo, msg));
    }
}
