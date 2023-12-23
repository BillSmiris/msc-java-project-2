package com.unipi.vsmyris;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceDemoAll {
    static List<Callable<String>> callables = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        callables.clear();
        addCallableTask("Efthimios Alepis");
        addCallableTask("Good morning everyone!");
        addCallableTask("Unipi");
        addCallableTask("Hello students of Informatics!!");
        List<Future<String>> futures = null;
        MyStopWatch stopWatch = new MyStopWatch();
        stopWatch.start();
        futures = executorService.invokeAll(callables);
        for(Future<String> future : futures){
            System.out.println("Result = " + future.get());
        }
        long duration = stopWatch.stop();
        System.out.println("Task took " + String.valueOf(duration) + " to complete");
        executorService.shutdown();
    }

    private static void addCallableTask(String s){
        callables.add(() -> {
            StringBuffer sb = new StringBuffer();
            return (sb.append("Length of string \"").append(s).append("\" is ").
                    append(s.length())).toString();
        });
    }
}
