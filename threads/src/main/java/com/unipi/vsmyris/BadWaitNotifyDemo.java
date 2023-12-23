package com.unipi.vsmyris;

import java.util.Random;

public class BadWaitNotifyDemo {
    private static String message;

    public static void main(String[] args) {
        Random random = new Random();
        int r = random.nextInt(5);
        Thread thread1 = new Thread(() -> {
            int counter = 0;
            while (message == null){
                counter++;
                System.out.println(Thread.currentThread().getName()+": waiting..."+counter);
            }
            System.out.println(Thread.currentThread().getName()+": Received:"+message);
            System.out.println(Thread.currentThread().getName()+": Ok, finished!..");
        });
        thread1.setName("thread1");
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(r*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            message = "A message from thread2";
        });

        thread1.start();
        thread2.start();
    }
}
