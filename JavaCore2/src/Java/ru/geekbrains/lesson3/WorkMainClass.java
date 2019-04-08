package Java.ru.geekbrains.lesson3;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkMainClass {
    public static void main(String[] args) {
        ArrayList<String> wordsList = new ArrayList();
        HashMap<String, Integer> words = new HashMap<>();

        wordsList.add("Стол");
        wordsList.add("Стул");
        wordsList.add("Табурет");
        wordsList.add("Диван");
        wordsList.add("Окно");
        wordsList.add("Дверь");
        wordsList.add("Стол");
        wordsList.add("Диван");
        wordsList.add("Стол");
        wordsList.add("Окно");
        wordsList.add("Табурет");
        wordsList.add("Комод");
        wordsList.add("Шкаф");
        wordsList.add("Кресло");
        System.out.println("исходный массив слов: " + wordsList);

        for(String word : wordsList){
            int tempCount = 0;
            for(String wordIn : wordsList){
                if(word == wordIn){ tempCount++;}
            }
            words.put(word, tempCount);
        }
        System.out.println("массив уникальных слов: " + words.keySet());
        System.out.println("количество уникальных слов: " + words.size());
        for(String word : words.keySet()){
            System.out.printf("Слово %s повторяется %d раз %n", word, words.get(word));
        }
        System.out.println();

        new BookNumber();
        BookNumber.add("Petrov", "3-15-10");
        BookNumber.add("Ivanov", "3-15-11");
        BookNumber.add("Sidorov", "3-15-12");
        BookNumber.add("Pushkov", "3-15-13");
        BookNumber.add("Petrov", "3-15-14");
        BookNumber.add("Ivanov", "3-15-15");
        BookNumber.add("Pavlov", "3-15-16");
        BookNumber.add("Vetrov", "3-15-17");
        BookNumber.add("Duhov", "3-15-18");
        BookNumber.add("Tupicin", "3-15-19");

        BookNumber.get("Petrov");
        BookNumber.get("Pavlov");
        BookNumber.get("Ivanov");
        BookNumber.get("Duhov");
    }
}

