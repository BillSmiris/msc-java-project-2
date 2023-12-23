package com.unipi.vsmyris;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{
    protected BlockingQueue<String> queue = null;
    public Producer(BlockingQueue<String> queue){
        this.queue = queue;
    }
    public void run(){
        try {
            queue.put("item 1");
            System.out.println("Producer put item 1");
            Thread.sleep(2000);
            queue.put("item 2");
            System.out.println("Producer put item 2");
            Thread.sleep(2000);
            queue.put("item 3");
            System.out.println("Producer put item 3");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
