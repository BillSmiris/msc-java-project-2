import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MessagingTest {
    private static Lock lock = new ReentrantLock();
    private static Object l = new Object();
    private static List<Runnable> runnableList = new ArrayList<>();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            try {
                Thread1Func();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            Thread2Func();
        });

        Thread thread3 = new Thread(() -> {
            Thread2Func();
        });

        Thread thread4 = new Thread(() -> {
            Thread2Func();
        });

        thread1.start();
        thread2.start();
        thread3.start();

        // Just to prevent the main thread from terminating immediately
        String userName = new Scanner(System.in).nextLine();

        thread4.start();
    }

    private static void Thread1Func() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (runnableList.isEmpty()) {
                    System.out.println("Thread1 waiting...");
                    l.wait();
                }
                Runnable r = runnableList.remove(0);
                r.run();
            } finally {
                lock.unlock();
            }
        }
    }

    private static void Thread2Func() {
        lock.lock();
        try {
            runnableList.add(() -> {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("ddddd");
            });
            synchronized (l) {
                l.notify();
            }
        } finally {
            lock.unlock();
        }
    }
}
