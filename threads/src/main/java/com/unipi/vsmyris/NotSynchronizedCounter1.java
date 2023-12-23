package com.unipi.vsmyris;

public class NotSynchronizedCounter1 implements Runnable{
    private static int counter = 0;
    @Override
    public void run() {
        while(counter < 10){
            System.out.println("["+Thread.currentThread().getName()+"] before: "+counter );
            counter++;
            System.out.println("["+Thread.currentThread().getName()+"] after: "+counter );
        }
    }
}
