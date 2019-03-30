package ru.geekbrains.lesson1;

import ru.geekbrains.lesson1.Animal.Cat;
import ru.geekbrains.lesson1.Animal.Dog;
import ru.geekbrains.lesson1.Animal.Human;
import ru.geekbrains.lesson1.course.*;
import ru.geekbrains.lesson1.enums.Color;

public class Application {
    public static void main(String[] args) {


        Team teamCat = new Team(
                new Cat("Барсик", Color.BLACK, 5, 100, 2),
                new Cat("Рыжик", Color.RED, 6, 150, 1),
                new Cat("Умка", Color.WHITE, 5, 200, 1),
                new Cat("Кузя", Color.WHITE, 4, 150, 2));

        Team teamDog = new Team(
                new Dog("Черныш", Color.BLACK, 4, 150, 1, 10),
                new Dog("Шарик", Color.RED, 7, 250, 1, 15),
                new Dog("Барбос", Color.YELLOW, 6, 200, 2, 15),
                new Dog("Тузик", Color.WHITE, 5, 250, 2, 20));

        Team teamHuman = new Team(
                new Human("Джон", Color.BLACK, 25, 300, 1, 30),
                new Human("Вася", Color.WHITE, 29, 400, 1, 50),
                new Human("Петя", Color.WHITE, 27, 350, 2, 40),
                new Human("Ваня", Color.WHITE, 22, 500, 1, 35));

        Course course = new Course(new Cross(200), new Wall(2), new Water(10));

        course.doIt(teamCat);
        course.doIt(teamDog);
        course.doIt(teamHuman);
    }
}
