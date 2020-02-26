package ru.geekbrains.lesson1.Animal;

import ru.geekbrains.lesson1.course.Participant;
import ru.geekbrains.lesson1.enums.Color;

public class Robot implements Participant {

    private boolean isOnDistance;
    private int runDistance = 1000;
    private int jumpHeight = 10;
    private int resultRobot = 0;

    private String name;
    private Color color;
    private int age;

    public Robot(String name, Color color, int age){
        this.name = name;
        this.color = color;
        this.age = age;
    }

    @Override
    public boolean isOnDistance() {return isOnDistance;}

    @Override
    public int run(int distance) {
        if(!isOnDistance) return resultRobot;
        if(distance > runDistance){
            isOnDistance = false;
            return resultRobot;
        }
        System.out.println(String.format("Робот %s пробежал кросс длинной %d", getName(), distance ));
        return resultRobot++;
    }

    @Override
    public int jump(int height) {
        if(!isOnDistance) return resultRobot;
        if(height > jumpHeight){
            isOnDistance = false;
            return resultRobot;
        }
        System.out.println(String.format("Робот %s прыгнул на высоту %d", getName(), height ));
        return resultRobot++;
    }

    @Override
    public int swim(int distance) {
        isOnDistance = false;
        System.out.println("Робот не умеет плавать");
        return resultRobot;
    }

    @Override
    public String getParticipantName() {
        return getName();
    }

    @Override
    public int getParticipantResult() {
        return resultRobot;
    }

    public void voice(){System.out.println("бип-бип");}

    public String getName(){return name;}

    public void setName(String name){
        this.name = name;
    }

    public Color getColor(){return color;}

    public int getAge(){return age;}

    public void setAge(int age){
        this.age = age;
    }
}
