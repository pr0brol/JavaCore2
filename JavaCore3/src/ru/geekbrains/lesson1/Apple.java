package ru.geekbrains.lesson1;

public class Apple extends Fruit {

    public static float weight = 1.0f;
    public static String fruitName = "apple";

    public Apple(){

    }

    public String getFruitName(){return fruitName;}

    public float getFruitWeight(){return weight;}
}