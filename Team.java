package ru.geekbrains.lesson1.course;

public class Team {
    private Participant[] participants;
    public Team(Participant... participants){
        this.participants = participants;
    }

    public Participant[] getParticipants(){
        return participants;
    }
    public void showResults(){
        for(Participant part: participants){
            System.out.println(part.getParticipantName() + " прошёл успешно " + part.getParticipantResult() + " испытаний");
        }
    }
}
