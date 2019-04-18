package Java.ru.geekbrains.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) {

        try(Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("LocalHost", 7777)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread InThread = new Thread() {
                @Override
                public void run() {
                    try{
                        while (true){
                            String str = in.readUTF();
                            System.out.println("Ответ сервера > " + str);
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
                            System.out.println("Введите сообщение > ");
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

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}