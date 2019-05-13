package ru.geekbrains.lesson2.client;

public interface MessageReciever {
    void submitMessage(TextMessage message);
    void userConnected(String login);
    void userDisconnected(String login);
}
