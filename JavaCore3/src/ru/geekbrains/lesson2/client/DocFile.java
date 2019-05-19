package ru.geekbrains.lesson2.client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DocFile {

    private static final String MSG_FORMAT = "%s\t%s\t%s\t%s";

    private final PrintWriter printWriter;
    private final File textFile;

    public DocFile(String login) throws FileNotFoundException {
        String fileName = String.format("%s.txt", login);
        textFile = new File(fileName);
        if(!textFile.exists()){
            try {
                textFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(textFile, true)));
    }


    public synchronized void writeText(TextMessage txtMsg){
        String msg = String.format(MSG_FORMAT, txtMsg.getCreated(), txtMsg.getUserFrom(), txtMsg.getUserTo(), txtMsg.getText());
        printWriter.println(msg);
    }

    public List<TextMessage> readText(int count){
        List<String> msgs = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            while (reader.ready()){
                msgs.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<TextMessage> lastMessage = new ArrayList<>();
        if(msgs.size() > count){
            msgs = msgs.subList(msgs.size() - count, msgs.size());
        }
        for(String str: msgs){
            lastMessage.add(parseMsg(str));
        }
        return lastMessage;
    }

    public void flush(){
        printWriter.flush();
    }

    private TextMessage parseMsg(String str){
        String[] parts = str.split("\t", 4);

        return new TextMessage(parts[1], parts[2], parts[3]);
    }
}
