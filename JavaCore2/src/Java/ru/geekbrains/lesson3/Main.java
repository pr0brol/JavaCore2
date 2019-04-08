package Java.ru.geekbrains.lesson3;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
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

        new Book();
        Book.add("Petrov", "3-15-10");
        Book.add("Ivanov", "3-15-11");
        Book.add("Sidorov", "3-15-12");
        Book.add("Pushkov", "3-15-13");
        Book.add("Petrov", "3-15-14");
        Book.add("Ivanov", "3-15-15");
        Book.add("Pavlov", "3-15-16");
        Book.add("Vetrov", "3-15-17");
        Book.add("Duhov", "3-15-18");
        Book.add("Tupicin", "3-15-19");

        Book.get("Petrov");
        Book.get("Pavlov");
        Book.get("Ivanov");
        Book.get("Duhov");
    }
}

