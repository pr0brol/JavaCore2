package Java.ru.geekbrains.lesson3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhoneBook {

    static Map<String, ArrayList<String>> phoneBook = new HashMap<>();

    public static void add(String name, String number){

        if(phoneBook.containsKey(name)){
            phoneBook.get(name).add(number);
        }
        else {
            phoneBook.put(name, new ArrayList<>());
            phoneBook.get(name).add(number);
        }
    }

    public static void get(String name){
        for(String pb : phoneBook.keySet()){
            if(pb == name){
                System.out.printf("Абонент %s найден с номером " + phoneBook.get(name), name);
                System.out.println();
                continue;
            }
        }
    }
}
