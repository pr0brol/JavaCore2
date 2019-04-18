package Java.ru.geekbrains.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    try(Scanner scanner = new Scanner(System.in);
        ServerSocket serverSocket = new ServerSocket(7777)){

        Thread InThread = new Thread() {
            @Override
            public void run() {
                System.out.println("Сервер ожидает подключения!");
                Socket socket = serverSocket.accept();
                System.out.println("Кто-то подключился: " + socket.getInetAddress());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                while (true) {
                    String str = in.readUTF();
                    if (str.equals("end")) {
                        System.out.println("Работа с клиентом завершена ");
                        break;
                    }
                    System.out.println("Новое сообщение > " + str);
                }
            }
        };

        Thread OutThread = new Thread() {
            @Override
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(7777)) {
                    Socket socket = serverSocket.accept();
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    while (scanner.hasNextLine()) {
                        System.out.println("Введите ответ клиенту > ");
                        String line = scanner.nextLine();
                        out.writeUTF(line);
                    }
                }
            }
        };
    }catch (IOException ex){
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        new EchoServer().InThread().start();
        new EchoServer().OutThread().start();
    }
}
