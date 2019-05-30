package ru.geekbrains.lesson1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static <T> T changeElement(T... array){
        List<T> obj = Arrays.asList(array);
        T first = obj.get(0);
        obj.set(0, obj.get(obj.size()-1));
        obj.set((obj.size()-1), first);
        return (T) obj;
    }

    public static <T> ArrayList formatToArrayList(T... array){
        ArrayList<T> obj = new ArrayList<>();
        for(T elem: array){
            obj.add(elem);
        }
        return obj;
    }

    public static void main(String[] args) {

        String[] str = {"a", "b", "c", "d", "e"};

        changeElement(str);
        for(String elem : str){
            System.out.print(elem + " ");
        }
        System.out.printf("%n%n");

        ArrayList list = formatToArrayList(str);
        list.add("f");
        list.add("g");
        System.out.println(list.toString());
        list.remove("c");
        System.out.println(list.toString());

        System.out.println();

        Fruit apple = new Apple();
        Fruit orange = new Orange();

        Box appleBox1 = new Box(apple);
        Box appleBox2 = new Box(apple);

        Box orangeBox = new Box(orange);

        appleBox1.putFruit(orange, 3);
        appleBox1.putFruit(apple, 2);
        appleBox1.putFruit(apple, 3);
        appleBox1.getWeight();

        orangeBox.putFruit(apple, 2);
        orangeBox.putFruit(orange,3);
        orangeBox.putFruit(orange,3);
        orangeBox.getWeight();

        System.out.println(appleBox1.compare(orangeBox));

        appleBox1.putFruit(apple, 4);

        System.out.println(appleBox1.compare(orangeBox));

        appleBox2.putFruit(apple, 2);
        appleBox1.getWeight();
        appleBox2.getWeight();
        appleBox2.getAll(appleBox1);
        appleBox1.getWeight();
        appleBox2.getWeight();
        appleBox1.getAll(orangeBox);
    }
}
