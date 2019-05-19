package ru.geekbrains.lesson2.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextMessage {

    public static String created;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String userFrom;

    public static String userTo;

    public static String text;

    public TextMessage(String userFrom, String userTo, String text){
        this.created = LocalDateTime.now().format(timeFormatter);
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
    }

    public String getUserFrom(){
        return userFrom;
    }

    public void setUserFrom(String userFrom){
        this.userFrom = userFrom;
    }

    public String getUserTo(){
        return userTo;
    }

    public void setUserTo(String userTo){
        this.userTo = userTo;
    }

    public String getText(){
        return text;
    }

    public void setText(String userName){
        this.text = text;
    }

    public String getCreated(){
        return created;
    }

    public void changeUsers(){
        String tempName = userFrom;
        userFrom = userTo;
        userTo = tempName;
    }
}

