package com.unipi.vsmyris;

public class Main {
    public static void main(String[] args){
//        long id = Thread.currentThread().getId();
//        String name = Thread.currentThread().getName();
//        int priority = Thread.currentThread().getPriority();
//        Thread.State state = Thread.currentThread().getState();
//        String threadGroupName = Thread.currentThread().getThreadGroup().getName();
//        System.out.println("id="+id+"; name="+name+"; priority="+priority+"; state="+state+"; threadGroupName="+threadGroupName);

//        MySimpleThread mt = new MySimpleThread();
//        mt.start();

//        MySimpleThread2 mt2 = new MySimpleThread2();
//        mt2.start();

//        Thread mt2 = new Thread(new MyRunnable());
//        mt2.start();

//        Thread t = new Thread(){
//            public void run(){
//                System.out.println("Hello from anonymous inner class");
//            }
//        };
//
//        t.start();

//        Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Hello from Inner Runnable");
//            }
//        };
//        Thread t = new Thread(myRunnable);
//        t.start();

//        Thread[] threads = new Thread[5];
//        for(int i = 0; i < threads.length; i++){
//            threads[i] = new Thread(new JoinExample1(), "joinThread-"+i);
//            threads[i].start();
//        }
//
//        for(int i = 0; i < threads.length; i++){
//            threads[i].join();
//        }
//
//        System.out.println("["+Thread.currentThread().getName()+"] All threads have finished.");

//        Thread[] threads = new Thread[3];
//        for(int i = 0; i < threads.length; i++){
//            threads[i] = new Thread(new NotSynchronizedCounter1(), "thread-"+i);
//            threads[i].start();
//        }

//        Thread[] threads = new Thread[3];
//        for(int i = 0; i < threads.length; i++){
//            threads[i] = new Thread(new SynchronizedCounter1(), "thread-"+i);
//            threads[i].start();
//        }
//        Thread[] threads = new Thread[3];
//        for(int i = 0; i < threads.length; i++){
//            threads[i] = new Thread(new SynchronizedCounter2(), "thread-"+i);
//            threads[i].start();
//        }

        ThreadLocalExample runnable = new ThreadLocalExample();
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}