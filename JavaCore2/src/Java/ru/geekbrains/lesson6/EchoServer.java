package Java.ru.geekbrains.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    public static void main(String[] args){
        try(Scanner scanner = new Scanner(System.in);
            ServerSocket serverSocket = new ServerSocket(7777)){
            System.out.println("Сервер ожидает подключения!");
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Кто-то подключился: " + socket.getInetAddress());
            System.out.print("Введите ответ клиенту > ");
            Thread InThread = new Thread() {
                @Override
                public void run() {
                    try{
                        while (true){
                            String str = in.readUTF();
                            System.out.println("Новое сообщение > " + str);
                        }
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            };

            Thread OutThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (scanner.hasNextLine()) {
                            System.out.print("Введите ответ клиенту > ");
                            String line = scanner.nextLine();
                            out.writeUTF(line);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };

            InThread.start();

            OutThread.start();
            OutThread.join();

        }catch (IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
