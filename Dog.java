package ru.geekbrains.lesson1.Animal;

import ru.geekbrains.lesson1.course.Participant;
import ru.geekbrains.lesson1.enums.Color;

public class Dog extends Animal implements Participant {
    private boolean isOnDistance;
    private int runDistance;
    private int jumpHeight;
    private int swimDistance;
    private int resultDog = 0;

    public Dog(String name, Color color, int age, int runDistance, int jumpHeight, int swimDistance){
        super(name, color, age);
        isOnDistance = true;
        this.runDistance = runDistance;
        this.jumpHeight = jumpHeight;
        this.swimDistance = swimDistance;
    }
    public Dog(String name, Color color){
        super(name, color, 0);
    }

    @Override
    public void voice() {
        System.out.println("Гав");
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
        return resultDog;
    }

    @Override
    public int run(int distance) {
        if(!isOnDistance) return resultDog;
        if(distance > runDistance){
            isOnDistance = false;
            return resultDog;
        }
        System.out.println(String.format("Собака %s пробежала кросс длинной %d", getName(), distance ));
        return resultDog++;
    }

    @Override
    public int jump(int height) {
        if(!isOnDistance) return resultDog;
        if(height > jumpHeight){
            isOnDistance = false;
            return resultDog;
        }
        System.out.println(String.format("Собака %s прыгнула на высоту %d", getName(), height ));
        return resultDog++;
    }

    @Override
    public int swim(int distance) {
        if(!isOnDistance) return resultDog;
        if(distance > swimDistance){
            isOnDistance = false;
            return resultDog;
        }
        System.out.println(String.format("Собака %s проплыла расстояние %d", getName(), distance ));
        return resultDog++;
    }
}

