package ru.geekbrains.lesson2.server;

import ru.geekbrains.lesson2.client.TextMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.geekbrains.lesson2.client.MessagePatterns.*;


public class ClientHandler {
    private final String login;
    private final Socket socket;
    private final DataOutputStream outStream;
    private final DataInputStream inStream;
    private ChatServer chatServer;
    private final ExecutorService executorService;


    public ClientHandler(String login, Socket socket, ChatServer chatServer) throws IOException{
        this.login = login;
        this.socket = socket;
        this.inStream = new DataInputStream(socket.getInputStream());
        this.outStream = new DataOutputStream(socket.getOutputStream());
        this.chatServer = chatServer;

        this.executorService = Executors.newFixedThreadPool(10);

        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        String text = inStream.readUTF();
                        System.out.printf("Сообщение от пользователя %s: %s%n", login, text);
                        System.out.println("Новое сообщение " + text);
                        TextMessage msg = parseTextMessage(text, login);
                        if(msg != null){
                            msg.changeUsers();
                            chatServer.sendMessage(msg);
                        }else if(text.equals(DISCONNECTED)){
                            System.out.printf("Пользователь %s отключился%n", login);
                            chatServer.unsubscribe(login);
                            return;
                        }else if(text.equals(USER_LIST_TAG)){
                            System.out.printf("Добавление пользователя %s в список%n", login);
                            sendUserList(chatServer.getUserList());

                        }
                    }catch (IOException ex){
                        ex.printStackTrace();
                        break;
                    }
                }
            }
        });
        this.chatServer = chatServer;
    }

    public String getLogin(){
        return login;
    }

    public void sendMessage(String userFrom, String msg) throws IOException{
        if(socket.isConnected()) {
            outStream.writeUTF(String.format(MESSAGE_SEND_PATTERN, userFrom, msg));
        }
    }

    public void sendConnectedMessage(String login)throws IOException{
        if(socket.isConnected()){
            outStream.writeUTF(String.format(CONNECTED_SEND, login));
        }
    }

    public void sendDisconnectedMessage(String login) throws  IOException{
        if(socket.isConnected()){
            outStream.writeUTF(String.format(DISCONNECTED_SEND, login));
        }
    }


    public void sendUserList(Set<String> users) throws IOException{
        if(socket.isConnected()){
            outStream.writeUTF(String.format(USER_LIST_RESPONSE, String.join(" ", users)));
        }
    }

}

