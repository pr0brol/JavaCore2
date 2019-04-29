package Java.ru.geekbrains.lesson7;

import Java.ru.geekbrains.lesson4.AuthException;
import Java.ru.geekbrains.lesson4.MessagePatterns;
import Java.ru.geekbrains.lesson4.TextMessage;
import Java.ru.geekbrains.lesson7.auth.AuthService;
import Java.ru.geekbrains.lesson7.auth.AuthServiceImpl;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends JDialog{

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
                System.out.println("Новый пользователь присоединился!");

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
                    subscribe(user.getLogin(), socket);
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

    private void sendUserConnectedMessage(String login) throws IOException{
        for(ClientHandler clientHandler: clientHandlerMap.values()){
            if(!clientHandler.getLogin().equals(login)){
                System.out.printf("Отправка уведомления от %s о %s%n", clientHandler.getLogin(), login);
                clientHandler.sendConnectedMessage(login);
            }
        }
    }

    private void sendUserDisconnectedMessage(String login) throws IOException{
        for(ClientHandler clientHandler: clientHandlerMap.values()){
            if(!clientHandler.getLogin().equals(login)){
                System.out.printf("Отправка уведомления от %s о %s%n", clientHandler.getLogin(), login);
                clientHandler.sendDisconnectedMessage(login);
            }
        }
    }

    private void sendUserRequestMessage(String login) throws IOException{
        for(ClientHandler clientHandler: clientHandlerMap.values()){
            if(!clientHandler.getLogin().equals(login)){
                System.out.println("Отправка уведомления о пользователях");
                clientHandler.sendRequestMessage(login);
            }
        }
    }

    public void sendMessage(TextMessage msg) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(msg.getUserTo());
        if(userToClientHandler != null){
            userToClientHandler.sendMessage(msg.getUserFrom(), msg.getText());
        }else {
            System.out.printf("Пользователь %s не в сети%n", msg.getUserTo());
        }
    }

    public void subscribe(String login, Socket socket) throws IOException{
        if(clientHandlerMap.containsKey(login)){
            JOptionPane.showMessageDialog(this, "Повторный вход", "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.out.println("Повторная авторизация");
            return;
            //todo проверить подключён ли пользователь, если да. то отправить ошибку (добавить всплывающее окно)
            //обработал, не придумал как при закрытии уведомления оставить всё как есть - сейчас вход всё равно выполняется
        }else {
            clientHandlerMap.put(login, new ClientHandler(login, socket, this));
            sendUserConnectedMessage(login);
            for(String key : clientHandlerMap.keySet()){
                if(key == login){ continue;}
                else{
                    clientHandlerMap.get(key).sendMessage("Сервер", "Пользователь " + login + " в сети");
                }
            }
        }
    }

    public void unsubscribe(String login) throws IOException {
        clientHandlerMap.remove(login);
        for(String key : clientHandlerMap.keySet()){
            if(key == login){ continue;}
            else{
                clientHandlerMap.get(key).sendMessage("Сервер", "Пользователь " + login + " отключился");
            }
        }
        sendUserDisconnectedMessage(login);
    }

    public void sendUsersMessage(String login) throws IOException{
        for(String key : clientHandlerMap.keySet()){
            clientHandlerMap.get(key).sendMessage("Сервер", key);
        }
        sendUserRequestMessage(login);
    }
}
