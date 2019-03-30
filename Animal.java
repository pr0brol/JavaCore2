package ru.geekbrains.lesson1.Animal;

import ru.geekbrains.lesson1.enums.Color;

public abstract class Animal {
    private String name;
    private Color color;
    private int age;

    public Animal(String name, Color color, int age){
        this.name = name;
        this.color = color;
        this.age = age;
    }
    public Animal(String name, Color color){
        this(name, color, 0);
    }

    public abstract void voice();


    public String getName(){return name;}

    public void setName(String name){
        this.name = name;
    }

    public Color getColor(){return color;}

    public int getAge(){return age;}

    public void setAge(int age){
        this.age = age;
    }

    public String getParticipantName(){return getName();}
}
