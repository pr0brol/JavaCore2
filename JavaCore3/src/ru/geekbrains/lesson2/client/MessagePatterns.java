package ru.geekbrains.lesson2.client;

public class MessagePatterns {
    public static final String AUTH_PATTERN = "/auth %s %s";
    public static final String AUTH_SUCCESSFUL = "/auth successful";
    public static final String AUTH_FAIL = "/auth fail";
    public static final String MESSAGE_SEND_PATTERN = "/w %s %s";
    public static final String CONNECTED = "/connected";
    public static final String CONNECTED_SEND = CONNECTED + " %s";
    public static final String DISCONNECTED = "/disconnected";
    public static final String DISCONNECTED_SEND = DISCONNECTED + " %s";
    public static final String REQUEST = "/request";
    public static final String REGISTRATION = "/registration %s";


    public static TextMessage parseTextMessage(String text, String userTo){
        String[] parts = text.split(" ", 3);
        if(parts.length == 3 && parts[0].equals("/w")){
            return new TextMessage(parts[1], userTo, parts[2]);
        }else{
            System.out.println("Неизвестный формат сообщения " + text);
            return null;
        }
    }

    public static String parseConnectedMessage(String text){
        String[] parts = text.split(" ");
        if(parts.length == 2 && parts[0].equals(CONNECTED)){
            return parts[1];
        }else {
            return null;
        }
    }

    public static String parseDisconnectedMessage(String text){
        String[] parts = text.split(" ");
        if(parts.length == 2 && parts[0].equals(DISCONNECTED)){
            return parts[1];
        }else {
            return null;
        }
    }

    public static String parseRequestMessage(String text){
        String[] parts = text.split(" ");
        if(parts.length == 2 && parts[0].equals(REQUEST)){
            return parts[1];
        }else {
            System.out.println("Неизвестный формат сообщения " + text);
            return null;
        }
    }

    public static String parseRegistationMessage(String text){
        String[] parts = text.split(" ");
        if(parts.length == 2 && parts[0].equals(REGISTRATION)){
            return parts[1];
        }else {
            System.out.println("Неизвестный формат сообщения " + text);
            return null;
        }
    }
}

