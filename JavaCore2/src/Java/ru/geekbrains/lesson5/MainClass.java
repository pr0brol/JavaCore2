package Java.ru.geekbrains.lesson5;

public class MainClass {
    public static void currentTimeArray(){
        final int size = 10000000;
        float[] testArray = new float[size];
        for(int i=0; i<testArray.length; i++){
            testArray[i] = 1;
        }
        long startTime = System.currentTimeMillis();
        for(int i=0; i<testArray.length; i++){
            testArray[i] = (float) (testArray[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
        }
        long workTime = System.currentTimeMillis() - startTime;
        System.out.println("время выполнения одним потоком: " + workTime);
    }

    public static void currentTimeArrayInThread() throws InterruptedException {
        final int size = 10000000;
        final int half = size / 2;
        float[] testArray = new float[size];
        for(int i=0; i<testArray.length; i++){
            testArray[i] = 1;
        }
        long startTime = System.currentTimeMillis();
        float[] array1 = new float[half];
        float[] array2 = new float[half];
        Thread thread1;
        Thread thread2;
        System.arraycopy(testArray, 0, array1, 0, half);
        System.arraycopy(testArray, half, array2, 0, half);
        thread1 = new Thread(){
            @Override
            public void run(){
                for(int i=0; i<array1.length; i++){
                    array1[i] = (float) (array1[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
                }
            }
        };

        thread2 = new Thread(){
            @Override
            public void run(){
                for(int i=0; i<array2.length; i++){
                    array2[i] = (float) (array2[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
                }
            }
        };
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.arraycopy(array1, 0, testArray, 0, half);
        System.arraycopy(array2, 0, testArray, half, half);
        long workTime = System.currentTimeMillis() - startTime;
        System.out.println("время выполнения двумя потоками: " + workTime);
    }

    public static void main(String[] args) throws InterruptedException {
        currentTimeArray();
        currentTimeArrayInThread();
    }
}
