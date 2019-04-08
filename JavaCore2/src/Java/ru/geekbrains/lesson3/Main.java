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


    }
}

