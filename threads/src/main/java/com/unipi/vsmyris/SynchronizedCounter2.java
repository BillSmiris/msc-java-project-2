package com.unipi.vsmyris;

public class SynchronizedCounter2 implements Runnable{
    private static int counter = 0;
    public void run(){
        synchronized (SynchronizedCounter2.class){
            while(counter < 10){
                System.out.println("["+Thread.currentThread().getName()+"] before: "+counter );
                counter++;
                System.out.println("["+Thread.currentThread().getName()+"] after: "+counter );
            }
        }
    }
}
