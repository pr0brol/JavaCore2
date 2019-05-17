package ru.geekbrains.lesson2.client;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import static ru.geekbrains.lesson2.client.MessagePatterns.*;

public class Network implements Closeable {
    public Socket socket;
    public DataInputStream inSteam;
    public DataOutputStream outStream;


    private String hostName;
    private int port;
    private MessageReciever messageReciever;
    public DocFile docFile;

    private String login;

    private Thread receiverThread;

    public Network(String hostName, int port, MessageReciever messageReciever){
        this.hostName = hostName;
        this.port = port;
        this.messageReciever = messageReciever;

        this.receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try{
                        String text = inSteam.readUTF();
                        System.out.println("Новое сообщение " + text);

                        TextMessage msg = parseTextMessage(text, login);
                        docFile = new DocFile();


                        if(msg != null) {
                            messageReciever.submitMessage(msg);
                            docFile.writeText(msg);
                            continue;
                        }

                        String login = parseConnectedMessage(text);

                        if(login != null){
                            messageReciever.userConnected(login);
                         //   docFile.readText(login);
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

    public void  authorize(String login, String password) throws IOException, AuthException, RegException{
        socket = new Socket(hostName, port);
        outStream = new DataOutputStream(socket.getOutputStream());
        inSteam = new DataInputStream(socket.getInputStream());

        sendMessage(String.format(AUTH_PATTERN, login, password));
        String response = inSteam.readUTF();
        if(response.equals(AUTH_SUCCESSFUL)){
            this.login = login;
            receiverThread.start();
        }
        else if (response.equals(REGISTRATION)){
            this.login = login;
            receiverThread.start();

        }else{
            throw new AuthException();
        }
    }

    public void sendTextMessage(TextMessage message){
        sendMessage(String.format(MESSAGE_SEND_PATTERN, message.getUserTo(), message.getText()));
        docFile.writeText(message);
    }

    private void sendMessage(String msg){
        try{
            outStream.writeUTF(msg);
            if(docFile != null) {
                docFile.writeText(new TextMessage("test", "test2", msg + "1"));
            }
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
        this.receiverThread.interrupt();
        sendMessage(DISCONNECTED);
    }
}

