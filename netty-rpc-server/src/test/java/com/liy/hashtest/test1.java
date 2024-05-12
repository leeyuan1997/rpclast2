package com.liy.hashtest;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class test1 {

    @Test
    public void testThreadSafte() throws InterruptedException {
         Object lock = new Object();

         AtomicInteger count = new AtomicInteger(0);
         Thread thread1  = new Thread(()->{

                 synchronized (lock) {
                    try {
                        while (true) {
                            while(count.get() == 0) {
                                System.out.println("打印0");
                                count.set(1);
                                lock.notifyAll();
                            }
                            lock.wait();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                 }
         });
        Thread thread2  = new Thread(()->{

            synchronized (lock) {
                try {
                    while (true) {
                        while(count.get() == 1) {
                            System.out.println("打印1");
                            count.set(0);
                            lock.notifyAll();
                        }
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread2.start();
        Thread.sleep(1000);
        thread1.start();
        thread1.interrupt();
    }
@Test
    public void rune() throws InterruptedException{
        Thread thread3 = new Thread(()->{
            synchronized (Thread.currentThread()){
                while (true){
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread3.start();
        Thread.sleep(5000);
        thread3.interrupt();
        while (true){}

    }

    static class DeomCallabel implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000);
            return 3;
        }
    }
}
