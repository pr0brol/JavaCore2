package ru.geekbrain.lesson1;

import java.util.ArrayList;

public class Box {

    String fruitType;
    float weightFruit = 0;
    float apple = 1.0f;
    float orange = 1.5f;
    ArrayList inBox = new ArrayList();

    public Box(String fruit){
        if(fruit.equals("apple") || fruit.equals("orange")) {
            this.fruitType = fruit;
        }
        else {
            System.out.println("неизвестный фрукт");
        }
    }

    public void putFruit(String fruit, int count){
        if(fruit == fruitType){
            if(fruitType.equals("apple")){
                weightFruit = apple;
            }
            else {
                weightFruit = orange;
            }
            while (count > 0){
                inBox.add(weightFruit);
                count--;
            }
        }
        else {
            System.out.println("Коробка не подходит для данного фрукта");
        }
    }

    public void getWeight(){
        System.out.printf("Коробка с %s весит %s%n", this.fruitType, this.inBox.size() * weightFruit);
    }

    public boolean compare(Box box){
        if((this.inBox.size())*weightFruit == (box.inBox.size()*box.weightFruit)){
            return true;
        }
        else {
            return false;
        }
    }

    public void getAll(Box box){
        if(this.fruitType == box.fruitType){
            box.putFruit(box.fruitType, this.inBox.size());
            this.inBox.clear();
        }else {
            System.out.println("В коробках разное содержимое");
        }
    }
}
