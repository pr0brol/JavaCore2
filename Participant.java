package ru.geekbrains.lesson1.course;

public interface Participant {
    boolean isOnDistance();
    int run(int distance);
    int jump(int height);
    int swim(int distance);
    String getParticipantName();
    int getParticipantResult();
}
