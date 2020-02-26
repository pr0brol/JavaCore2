package ru.geekbrains.lesson1.Animal;

import ru.geekbrains.lesson1.course.Participant;
import ru.geekbrains.lesson1.enums.Color;

public class Cat extends Animal implements Participant {

    private boolean isOnDistance;
    private int runDistance;
    private int jumpHeight;
    private int resultCat = 0;

    public Cat(String name, Color color, int age, int runDistance, int jumpHeight) {
        super(name, color, age);
        isOnDistance = true;
        this.runDistance = runDistance;
        this.jumpHeight = jumpHeight;
    }

    public Cat(String name, Color color) {super(name, color, 0);}

    @Override
    public void voice() {
        System.out.println("Мяу");
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
        return resultCat;
    }

    @Override
    public int run(int distance) {
        if(!isOnDistance) return resultCat;
        if(distance > runDistance){
            isOnDistance = false;
            return resultCat;
        }
        System.out.println(String.format("Кошка %s пробежала кросс длинной %d", getName(), distance ));
        return resultCat++;
    }

    @Override
    public int jump(int height) {
        if(!isOnDistance) return resultCat;
        if(height > jumpHeight){
            isOnDistance = false;
            return resultCat;
        }
        System.out.println(String.format("Кошка %s прыгнула на высоту %d", getName(), height ));
        return resultCat++;
    }

    @Override
    public int swim(int distance) {
        isOnDistance = false;
        System.out.println("Кошка не умеет плавать");
        return resultCat;
        //  throw new UnsupportedOperationException();
    }
}

