package ru.geekbrains.lesson5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    int MAX_CARS;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier barrier;
    private CyclicBarrier barrier1;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrier barrier, CyclicBarrier barrier1, int maxCars) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.MAX_CARS = maxCars;
        this.name = "Участник #" + CARS_COUNT;
        this.barrier = barrier;
        this.barrier1 = barrier1;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            barrier.await();
            Thread.sleep(3);            //без этого не стабильно гонка начинается сразу после готовности машин
        } catch (Exception e) {               //хотя по теории вроде всё правильно
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if(CARS_COUNT == MAX_CARS){winMessage();}
        CARS_COUNT--;
        try {
            barrier1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void winMessage(){System.out.println(this.name + " WIN");}

}
