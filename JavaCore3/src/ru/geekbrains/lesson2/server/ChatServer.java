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
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class ChatServer extends JDialog{

    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());
    private AuthService authService;
    private static UserRepository userRepository;
    private Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws SQLException, RegException, IOException {
        logger.setLevel(Level.INFO);
        logger.getParent().setLevel(Level.INFO);
        logger.getParent().getHandlers()[0].setLevel(Level.INFO);

        FileHandler fileHandler = new FileHandler("log_chat_server.log", true);
        fileHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("Chat_Server: %s %d %s%n", record.getLevel(), record.getMillis(), record.getMessage());
            }
        });
        logger.addHandler(fileHandler);
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
            logger.log(Level.INFO, "Сервер запущен!");
            while (true){
                Socket socket = serverSocket.accept();
                DataInputStream inStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Новый пользователь присоединился!");
                logger.log(Level.INFO, "Новый пользователь присоединился!");

                User user = null;
                String authMessage = inStream.readUTF();

                try {
                    user = checkAuthentication(authMessage);

                }catch (AuthException ex){
                    try{
                        if(Registration(authMessage)){
                            System.out.println(authMessage);
                            outStream.writeUTF(MessagePatterns.REGISTRATION_SUCCESSFUL);
                        }else {
                            outStream.writeUTF(MessagePatterns.AUTH_FAIL);
                            outStream.flush();
                            // socket.close();
                        }

                    }catch (RegException e){
                        outStream.writeUTF(MessagePatterns.REGISTRATION_FAIL);
                        String[] parts = authMessage.split(" ");
                        String msg = String.format("Пользователь %s уже зарегистрирован", parts[1]);
                        System.out.println(msg);
                        logger.log(Level.INFO, msg);
                        outStream.flush();
                    }
                }
                if(user != null && authService.authUser(user)){
                    String msg = String.format("Пользователь %s успешно авторизировался!", user.getLogin());
                    System.out.println(msg);
                    logger.log(Level.INFO, msg);
                    subscribe(user.getLogin(), socket);
                    outStream.writeUTF(MessagePatterns.AUTH_SUCCESSFUL);
                    outStream.flush();
                }else if(user != null && authService.regUser(user)){
                    String msg = String.format("Пользователь %s не зарегистророван!", user.getLogin());
                    System.out.println(msg);
                    logger.log(Level.INFO, msg);
                    outStream.writeUTF(MessagePatterns.REGISTRATION_FAIL);
                    outStream.flush();
                }else {
                    if(user != null){
                        String msg = String.format("Неверная авторизация пользователя %s", user.getLogin());
                        System.out.println(msg);
                        logger.log(Level.INFO, msg);
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
            String msg = String.format("Некорректная авторизация ", authMessage);
            System.out.print(msg);
            logger.log(Level.INFO, msg);
            throw new AuthException();
        }else if(authParts[1] == null || authParts[2] == null){
            throw new AuthException();
        }
        User user = new User(-1, authParts[1], authParts[2]);
        return user;
    }

    private boolean Registration(String regMessage) throws RegException, SQLException {
        String[] regParts = regMessage.split(" ");
        User user = new User(-1, regParts[1], regParts[2]);
        if(regParts.length != 3 || !regParts[0].equals("/reg")){
            String msg = String.format("Некорректная регистрация ", regMessage);
            System.out.print(msg);
            logger.log(Level.INFO, msg);
            throw new RegException();
        }else if(regParts[1] == null || regParts[2] == null || !authService.regUser(user)){
            throw new RegException();
        }
        userRepository.insert(user);
        return true;
    }

    private void sendUserConnectedMessage(String login) throws IOException{
        for(ClientHandler clientHandler: clientHandlerMap.values()){
            if(!clientHandler.getLogin().equals(login)){
                String msg = String.format("Отправка уведомления от %s о %s", clientHandler.getLogin(), login);
                System.out.println(msg);
                logger.log(Level.INFO, msg);
                clientHandler.sendConnectedMessage(login);
            }
        }
    }

    private void sendUserDisconnectedMessage(String login) throws IOException{
        for(ClientHandler clientHandler: clientHandlerMap.values()){
            if(!clientHandler.getLogin().equals(login)){
                String msg = String.format("Отправка уведомления от %s о %s", clientHandler.getLogin(), login);
                System.out.println(msg);
                logger.log(Level.INFO, msg);
                clientHandler.sendDisconnectedMessage(login);
            }
        }
    }

    public void sendMessage(TextMessage msg) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(msg.getUserTo());
        if(userToClientHandler != null){
            userToClientHandler.sendMessage(msg.getUserFrom(), msg.getText());
        }else {
            String str = String.format("Пользователь %s не в сети", msg.getUserTo());
            System.out.println(str);
            logger.log(Level.INFO, str);
        }
    }

    public void subscribe(String login, Socket socket) throws IOException{
        if(clientHandlerMap.containsKey(login)){
            do {
                JOptionPane.showMessageDialog(this, "Повторный вход", "Ошибка", JOptionPane.ERROR_MESSAGE);
                String str = String.format("Повторная авторизация");
                System.out.println(str);
                logger.log(Level.INFO, str);

                //todo проверить подключён ли пользователь, если да. то отправить ошибку (добавить всплывающее окно)
            }while (clientHandlerMap.containsKey(login));
        }else {
            clientHandlerMap.put(login, new ClientHandler(login, socket, this, executorService));
            sendUserConnectedMessage(login);
            for(String key : clientHandlerMap.keySet()){
                if(key == login){ continue;}
                else{
                    String str = String.format("Пользователь " + login + " в сети");
                    clientHandlerMap.get(key).sendMessage("Сервер", str);
                    logger.log(Level.INFO, str);
                }
            }
        }
    }

    public void unsubscribe(String login) throws IOException {
        clientHandlerMap.remove(login);
        for(String key : clientHandlerMap.keySet()){
            if(key == login){ continue;}
            else{
                String str = String.format("Пользователь " + login + " отключился");
                clientHandlerMap.get(key).sendMessage("Сервер", str);
                logger.log(Level.INFO, str);
            }
        }
        sendUserDisconnectedMessage(login);
    }

    public Set<String> getUserList(){
        return Collections.unmodifiableSet(clientHandlerMap.keySet());
    }
}
