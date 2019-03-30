package ru.geekbrains.lesson1.Animal;

import ru.geekbrains.lesson1.course.Participant;
import ru.geekbrains.lesson1.enums.Color;

public class Human extends Animal implements Participant {
    private boolean isOnDistance;
    private int runDistance;
    private int jumpHeight;
    private int swimDistance;
    private int resultHuman = 0;

    public Human(String name, Color color, int age, int runDistance, int jumpHeight, int swimDistance){
        super(name, color, age);
        isOnDistance = true;
        this.runDistance = runDistance;
        this.jumpHeight = jumpHeight;
        this.swimDistance = swimDistance;
    }
    public Human(String name, Color color){
        super(name, color, 0);
    }

    @Override
    public void voice() {
        System.out.println("Слышь");
    }


    @Override
    public boolean isOnDistance() {
        return isOnDistance;
    }

    @Override
    public String getParticipantName() {
        return getName();
    }

    @Override
    public int getParticipantResult() {
        return resultHuman;
    }

    @Override
    public int run(int distance) {
        if(!isOnDistance) return resultHuman;
        if(distance > runDistance){
            isOnDistance = false;
            return resultHuman;
        }
        System.out.println(String.format("Человек %s пробежал кросс длинной %d", getName(), distance ));
        return resultHuman++;
    }

    @Override
    public int jump(int height) {
        if(!isOnDistance) return resultHuman;
        if(height > jumpHeight){
            isOnDistance = false;
            return resultHuman;
        }
        System.out.println(String.format("%s прыгнул на высоту %d", getName(), height ));
        return resultHuman++;
    }

    @Override
    public int swim(int distance) {
        if(!isOnDistance) return resultHuman;
        if(distance > swimDistance){
            isOnDistance = false;
            return resultHuman;
        }
        System.out.println(String.format("%s проплыла расстояние %d", getName(), distance ));
        return resultHuman++;
    }
}

