package Java.ru.geekbrains.lesson4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static Java.ru.geekbrains.lesson4.MessagePatterns.AUTH_PATTERN;
import static Java.ru.geekbrains.lesson4.MessagePatterns.MESSAGE_SEND_PATTERN;

public class Network {
    public Socket socket;
    public DataInputStream inSteam;
    public DataOutputStream outStream;

    private String hostName;
    private int port;
    private MessageReciever messageReciever;

    private String login;

    private Thread receiverThread;

    public Network(String hostName, int port, MessageReciever messageReciever){
        this.hostName = hostName;
        this.port = port;
        this.messageReciever = messageReciever;

        this.receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        String text = inSteam.readUTF();
                        String[] textMass = text.split(" ");
                        if(textMass.length != 3 || textMass[0].equals("/w")){
                            continue;
                        }
                        TextMessage textMessage = new TextMessage(textMass[1], login, textMass[2]);
                        messageReciever.submitMessage(textMessage);
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
        if(response.equals("/auth successful")){
            this.login = login;
            receiverThread.start();
        }else{
            throw new AuthException();
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

    public String getLogin(){
        return login;
    }
}
