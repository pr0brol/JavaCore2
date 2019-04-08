package Java.ru.geekbrains.lesson3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookNumber {

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
        for(String phone : phoneBook.keySet()){
            if(phone == name){
                System.out.printf("Абонент %s найден с номером(-ами) " + phoneBook.get(name), name);
                System.out.println();
                continue;
            }
        }
    }
}
