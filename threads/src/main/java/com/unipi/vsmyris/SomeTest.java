package com.unipi.vsmyris;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class SomeTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<?> future = executorService.submit(()-> {
            try {
                sleep(5000);
                System.out.println("1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        future.cancel(true);
        future = executorService.submit(()-> {
            try {
                sleep(1000);
                System.out.println("2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
    }
}
