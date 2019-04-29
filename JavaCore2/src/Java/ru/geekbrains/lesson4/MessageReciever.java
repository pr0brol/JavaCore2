package Java.ru.geekbrains.lesson4;

public interface MessageReciever {
    void submitMessage(TextMessage message);
    void userConnected(String login);
    void userDisconnected(String login);
}
