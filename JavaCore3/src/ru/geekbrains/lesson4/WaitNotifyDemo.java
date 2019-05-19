package ru.geekbrains.lesson4;

public class WaitNotifyDemo {

    private static volatile char currentLetter = 'A';

    public static void main(String[] args) {

        new Thread(WaitNotifyDemo::printA).start();
        new Thread(WaitNotifyDemo::printB).start();
        new Thread(WaitNotifyDemo::printC).start();

    }

    private synchronized static void printA(){
        for (int i=0; i<3; i++){
            try {
                while (currentLetter != 'A'){
                   WaitNotifyDemo.class.wait();
                }
                System.out.println('A');
                currentLetter = 'B';
                WaitNotifyDemo.class.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private synchronized static void printB(){
        for (int i=0; i<3; i++){
            try {
                while (currentLetter != 'B'){
                    WaitNotifyDemo.class.wait();
                }
                System.out.println('B');
                currentLetter = 'C';
                WaitNotifyDemo.class.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized static void printC(){
        for (int i=0; i<3; i++){
            try {
                while (currentLetter != 'C'){
                    WaitNotifyDemo.class.wait();
                }
                System.out.println('C');
                currentLetter = 'A';
                WaitNotifyDemo.class.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
