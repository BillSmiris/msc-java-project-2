package com.unipi.vsmyris;

import java.util.Random;

public class GoodWaitNotifyDemo {
    private static String message;

    public static void main(String[] args) {
        Object mylock = new Object();
        Random random = new Random();
        int r = random.nextInt(5);
        Thread thread1 = new Thread(() -> {
            synchronized (mylock){
                while (message == null){
                    try {
                        mylock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(Thread.currentThread().getName()+": Received:"+message);
                System.out.println(Thread.currentThread().getName()+": Ok, finished!..");
            }
        });
        thread1.setName("thread1");
        Thread thread2 = new Thread(() -> {
            synchronized (mylock){
                try {
                    Thread.sleep(r*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                message = "A message from thread2";
                mylock.notify();
            }
        });
        thread1.start();
        thread2.start();
    }
}
