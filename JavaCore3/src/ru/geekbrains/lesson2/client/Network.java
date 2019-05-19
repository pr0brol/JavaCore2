package ru.geekbrains.lesson2.client;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.geekbrains.lesson2.client.MessagePatterns.*;

public class Network implements Closeable {
    public Socket socket;
    public DataInputStream inSteam;
    public DataOutputStream outStream;


    private String hostName;
    private int port;
    private MessageReciever messageReciever;
    private final ExecutorService executorService;

    private String login;

    public Network(String hostName, int port, MessageReciever messageReciever){
        this.hostName = hostName;
        this.port = port;
        this.messageReciever = messageReciever;
        this.executorService = Executors.newFixedThreadPool(10);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try{
                        String text = inSteam.readUTF();
                        System.out.println("Новое сообщение " + text);
                        TextMessage msg = parseTextMessage(text, login);

                        if(msg != null) {
                            messageReciever.submitMessage(msg);
                            continue;
                        }

                        String login = parseConnectedMessage(text);

                        if(login != null){
                            messageReciever.userConnected(login);
                            continue;
                        }

                        login = parseDisconnectedMessage(text);
                        if(login != null){
                            messageReciever.userDisconnected(login);
                            continue;
                        }

                        Set<String> users = parseUserList(text);
                        if(users != null){
                            messageReciever.updateUserList(users);
                        }

                    }catch (IOException ex){
                        ex.printStackTrace();
                        if(socket.isClosed()){
                            break;
                        }
                    }
                }
            }
        });
    }

    public void  authorize(String login, String password) throws IOException, AuthException{
        socket = new Socket(hostName, port);
        outStream = new DataOutputStream(socket.getOutputStream());
        inSteam = new DataInputStream(socket.getInputStream());

        sendMessage(String.format(AUTH_PATTERN, login, password));
        String response = inSteam.readUTF();
        if(response.equals(AUTH_SUCCESSFUL)){
            this.login = login;
           // this.thread.start();
        }else{
            throw new AuthException();
        }
    }

    public void  registration(String login, String password) throws IOException, RegException{

        socket = new Socket(hostName, port);
        outStream = new DataOutputStream(socket.getOutputStream());
        inSteam = new DataInputStream(socket.getInputStream());

        sendMessage(String.format(REG_PATTERN, login, password));
        String response = inSteam.readUTF();
        if(response.equals(REGISTRATION_SUCCESSFUL)){
            this.login = login;
        }else{
            throw new RegException();
        }
    }

    public void sendTextMessage(TextMessage message){
        sendMessage(String.format(MESSAGE_SEND_PATTERN, message.getUserTo(), message.getText()));
    }

    private void sendMessage(String msg){
        try{
            outStream.writeUTF(msg);
            outStream.flush();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void requestConnectedUserList(){
        sendMessage(USER_LIST_TAG);
    }

    public String getLogin(){
        return login;
    }

    @Override
    public void close(){
        sendMessage(DISCONNECTED);
    }
}

