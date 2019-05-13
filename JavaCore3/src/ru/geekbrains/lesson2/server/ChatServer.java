package ru.geekbrains.lesson2.server;

import ru.geekbrains.lesson2.client.AuthException;
import ru.geekbrains.lesson2.client.MessagePatterns;
import ru.geekbrains.lesson2.client.RegException;
import ru.geekbrains.lesson2.client.TextMessage;
import ru.geekbrains.lesson2.server.auth.AuthService;
import ru.geekbrains.lesson2.server.auth.AuthServiceJdbcImpl;
import ru.geekbrains.lesson2.server.persistance.UserRepository;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends JDialog{

    private AuthService authService;
    private static UserRepository userRepository;
    private Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws SQLException, RegException {
        AuthService authService;
        try {
            String url = "jdbc:mysql://localhost/network_chat?useUnicode=true&useJDBCCompliantTimeZoneShift=true" +
                          "&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow";
            Connection conn = DriverManager.getConnection(url, "root", "root" );
            userRepository = new UserRepository(conn);
            authService = new AuthServiceJdbcImpl(userRepository);
            userRepository.getAllUsers();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        ChatServer chatServer = new ChatServer(authService);
        chatServer.start(7777);
    }

    public ChatServer(AuthService authService){
        this.authService = authService;
    }


    private void start(int port) throws SQLException, RegException {
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
    private  User checkAuthentication(String authMessage) throws AuthException {
        String[] authParts = authMessage.split(" ");
        if(authParts.length != 3 || !authParts[0].equals("/auth")){
            System.out.printf("Некорректная авторизация ", authMessage);
            throw new AuthException();
        }else if(authParts[1] == null || authParts[2] == null){
            throw new AuthException();

        }
        User user = new User(-1, authParts[1], authParts[2]);
        userRepository.insert(user);
        return user;
    }
// создать метод checkRegistration
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
            do {
                JOptionPane.showMessageDialog(this, "Повторный вход", "Ошибка", JOptionPane.ERROR_MESSAGE);
                System.out.println("Повторная авторизация");
                //todo проверить подключён ли пользователь, если да. то отправить ошибку (добавить всплывающее окно)
                //обработал, не придумал как при закрытии уведомления оставить всё как есть - сейчас вход всё равно выполняется
            }while (clientHandlerMap.containsKey(login));
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
