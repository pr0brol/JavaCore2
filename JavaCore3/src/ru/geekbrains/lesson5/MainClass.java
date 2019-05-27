package ru.geekbrains.lesson5;


import java.util.concurrent.*;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static final CyclicBarrier barrierStart = new CyclicBarrier(CARS_COUNT + 1);
    public static final CyclicBarrier barrierFinish = new CyclicBarrier(CARS_COUNT + 1);
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService executorService = Executors.newFixedThreadPool(CARS_COUNT);
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(CARS_COUNT), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), barrierStart, barrierFinish, CARS_COUNT);
        }

        for (int i = 0; i < cars.length; i++) {
            executorService.submit(cars[i]);
        }

        barrierStart.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        executorService.shutdown();
        barrierFinish.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
