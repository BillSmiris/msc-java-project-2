package com.unipi.vsmyris;

public class MyStopWatch {
    private long startTime;
    public  void start(){
        startTime = System.nanoTime();
    }
    public long stop(){
        return System.nanoTime() - startTime;
    }
}
