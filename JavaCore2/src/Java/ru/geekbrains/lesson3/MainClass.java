package Java.ru.geekbrains.lesson3;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        ArrayList<String> wordsList = new ArrayList();
        ArrayList<String> wordOneList = new ArrayList();
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
        wordsList.add("Стул");
        wordsList.add("Комод");
        wordsList.add("Шкаф");
        wordsList.add("Кресло");
        System.out.println("исходный массив слов: " + wordsList);
        for(String word : wordsList){
            if(wordOneList.contains(word)){
                continue;
            }
            else {
                wordOneList.add(word);
            }
        }
        System.out.println("массив уникальных слов: " + wordOneList);
        System.out.println("количество уникальных слов: " + wordOneList.size());
        System.out.println();

        new PhoneBook();
        PhoneBook.add("Petrov", "3-15-10");
        PhoneBook.add("Ivanov", "3-15-11");
        PhoneBook.add("Sidorov", "3-15-12");
        PhoneBook.add("Pushkov", "3-15-13");
        PhoneBook.add("Petrov", "3-15-14");
        PhoneBook.add("Ivanov", "3-15-15");
        PhoneBook.add("Pavlov", "3-15-16");
        PhoneBook.add("Vetrov", "3-15-17");
        PhoneBook.add("Duhov", "3-15-18");
        PhoneBook.add("Petrov", "3-15-19");

        PhoneBook.get("Petrov");
        PhoneBook.get("Pavlov");
        PhoneBook.get("Ivanov");
        PhoneBook.get("Duhov");
    }
}

