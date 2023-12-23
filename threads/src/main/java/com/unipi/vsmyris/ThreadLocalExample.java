package com.unipi.vsmyris;

public class ThreadLocalExample implements Runnable{
    private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
    public void run(){
        threadLocal.set((int) (Math.random() * 100D));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(threadLocal.get());
    }
}
