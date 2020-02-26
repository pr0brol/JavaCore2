package ru.geekbrain.lesson1;

import java.util.ArrayList;

public class Box <T extends Fruit>{

    private T obj;
    String fruitType;
    ArrayList inBox = new ArrayList();
    float weightFruit;

    public Box(Fruit fruit){
        if(fruit.fruitName.equals("fruit")) {
            System.out.println("Коробка создана");
            fruitType = fruit.getFruitName();
            weightFruit = fruit.getFruitWeight();
            obj = (T) fruit;

        }
        else {
            System.out.println("неизвестный фрукт");
        }
    }

    public void putFruit(T newFruit, int count){
        if(newFruit.getFruitName().equals(fruitType)){
            System.out.printf("В коробку добавлено %s фрукта(ов) %s%n", count, newFruit.getFruitName());
            while (count > 0){
                inBox.add(newFruit);
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

            this.putFruit(obj, box.inBox.size());
            box.inBox.clear();
        }else {
            System.out.println("В коробках разное содержимое");
        }
    }
}
