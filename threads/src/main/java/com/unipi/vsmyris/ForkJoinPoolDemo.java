package com.unipi.vsmyris;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolDemo {
    public static void main(String[] args) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        CustomRecursiveAction recursiveAction = new CustomRecursiveAction("Hello Students of Unipi. " +
                "How was your day today?");
        commonPool.invoke(recursiveAction);
    }
}
