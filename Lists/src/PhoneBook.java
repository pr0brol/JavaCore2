import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneBook {

    static Map<String, ArrayList<String>> phoneBook = new HashMap<>();
    static List<String> listNumber = new ArrayList<>();


    public static void add(String name, String number){
        phoneBook.putIfAbsent(name, (ArrayList<String>) listNumber);
        if(phoneBook.containsKey(name)){
            listNumber.add(number);
        }
        else {
            phoneBook.putIfAbsent(name, listNumber.add(number));
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
