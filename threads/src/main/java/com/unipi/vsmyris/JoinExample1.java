package com.unipi.vsmyris;

import java.util.Random;

public class JoinExample1 implements Runnable{
    private Random rand = new Random(System.currentTimeMillis());

    @Override
    public void run() {
        for(int i = 0; i < 100000000; i++){
            rand.nextInt();
        }
        System.out.println("["+Thread.currentThread().getName()+"] finished.");
    }
}
