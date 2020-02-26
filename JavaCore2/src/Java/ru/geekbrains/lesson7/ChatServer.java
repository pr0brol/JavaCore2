package Java.ru.geekbrains.lesson7;

import Java.ru.geekbrains.lesson4.AuthException;
import Java.ru.geekbrains.lesson4.MessagePatterns;
import Java.ru.geekbrains.lesson4.TextMessage;
import Java.ru.geekbrains.lesson7.auth.AuthService;
import Java.ru.geekbrains.lesson7.auth.AuthServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    private AuthService authService = new AuthServiceImpl();
    private Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start(7777);
    }

    private void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Сервер запущен!");
            while (true){
                Socket socket = serverSocket.accept();
                DataInputStream inStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Новый клиент присоединился!");

                User user = null;
                try {
                    String authMessage = inStream.readUTF();
                    user = checkAuthentication(authMessage);
                }catch (IOException ex){
                    ex.printStackTrace();
                }catch (AuthException ex){
                    outStream.writeUTF(MessagePatterns.AUTH_FAIL);
                    outStream.flush();
                    socket.close();
                }
                if(user != null && authService.authUser(user)){
                    System.out.printf("Пользователь %s успешно авторизировался! %n", user.getLogin());
                    clientHandlerMap.put(user.getLogin(), new ClientHandler(user.getLogin(), socket, this));
                    outStream.writeUTF(MessagePatterns.AUTH_SUCCESSFUL);
                    outStream.flush();
                }else {
                    if(user != null){
                        System.out.printf("Неверная авторизация пользователя %s%n", user.getLogin());
                    }
                    outStream.writeUTF(MessagePatterns.AUTH_FAIL);
                    outStream.flush();
                    socket.close();
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    private  User checkAuthentication(String authMessage) throws AuthException{
        String[] authParts = authMessage.split(" ");
        if(authParts.length != 3 || !authParts[0].equals("/auth")){
            System.out.printf("Некорректная авторизация ", authMessage);
            throw new AuthException();
        }
        return new User(authParts[1], authParts[2]);
    }

    public void sendMessage(TextMessage msg) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(msg.getUserTo());
        if(userToClientHandler != null){
            userToClientHandler.sendMessage(msg.getUserFrom(), msg.getText());
        }else {
            System.out.printf("Пользователь %s не в сети%n", msg.getUserTo());
        }
    }
}
