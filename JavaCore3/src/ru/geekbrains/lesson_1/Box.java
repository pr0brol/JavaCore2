package ru.geekbrains.lesson_1;

import java.util.ArrayList;
import java.util.List;

public class Box <T extends Fruit>{

    private List<T> inBox = new ArrayList<>();


    public void putFruit(T newFruit, int count){

            while (count > 0) {
                inBox.add(newFruit);
                count--;
            }

    }

    public double getWeight(){

        return this.inBox.size() * this.inBox.get(0).getFruitWeight();
    }

    public boolean compare(Box<? extends Fruit> box){
        if((this.inBox.size())*this.inBox.get(0).getFruitWeight() == (box.inBox.size()*box.inBox.get(0).getFruitWeight())){
            return true;
        }
        else {
            return false;
        }
    }

    public void getAll(Box<T> box){

        inBox.addAll(box.inBox);
        box.inBox.clear();

    }
}
