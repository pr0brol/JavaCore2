package ru.geekbrain.lesson1;


import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        String[] str = {"a", "b", "c", "d", "e"};

        ChangeElement<String> changeElement = new ChangeElement<>(str);

        changeElement.change();

        for(String s : str){
            System.out.print(s + " ");
        }
        System.out.println();

        FormatToArrayList<String> formatToArrayList = new FormatToArrayList<>();
        ArrayList list = formatToArrayList.format(str);
        list.add("f");
        for(Object s : list){
            System.out.print(s + " ");
        }

        System.out.println();

        String apple = "apple";
        String orange = "orange";

        Box appleBox1 = new Box("apple");
        Box appleBox2 = new Box("apple");

        Box orangeBox = new Box("orange");

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
