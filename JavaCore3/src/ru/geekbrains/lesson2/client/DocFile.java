package ru.geekbrains.lesson2.client;

import java.io.*;

public class DocFile {


    public void writeText(TextMessage textMessage){
        try {
            String fileName = String.format("%s.txt", textMessage.getUserTo());
            File textFile = new File(fileName);
            try(PrintWriter writer = new PrintWriter(new FileWriter(textFile, true))){

                writer.println(textMessage.getCreated() + " " + textMessage.getUserFrom() + " ---> " + textMessage.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readText(String login){
        String textFile = String.format("%s.txt", login);
        try(BufferedReader reader = new BufferedReader(new FileReader(textFile))){
            while (reader.ready()){

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
