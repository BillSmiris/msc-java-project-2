package com.unipi.vsmyris;

public class SynchronizedCounter1 implements Runnable{
    private static int counter = 0;
    public void run(){
        while(counter < 10){
            synchronized (SynchronizedCounter1.class){
                System.out.println("["+Thread.currentThread().getName()+"] before: "+counter );
                counter++;
                System.out.println("["+Thread.currentThread().getName()+"] after: "+counter );
            }
        }
    }
}
