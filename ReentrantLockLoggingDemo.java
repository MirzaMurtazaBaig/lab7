package ft;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockLoggingDemo {

    // Shared Buffer with ReentrantLock
    static class Buffer {
        private final LinkedList<Integer> buffer = new LinkedList<>();
        private final int capacity = 5;
        private final ReentrantLock lock = new ReentrantLock(true); // Fair lock
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                lock.lock();
                try {
                    while (buffer.size() == capacity) {
                        logAction("Producer waiting: Buffer is full");
                        notFull.await(); // Wait until not full
                    }
                    buffer.add(value);
                    logAction("Produced: " + value);
                    value++;
                    notEmpty.signal(); // Signal consumer
                } finally {
                    lock.unlock();
                }
                Thread.sleep(500);
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                lock.lock();
                try {
                    while (buffer.isEmpty()) {
                        logAction("Consumer waiting: Buffer is empty");
                        notEmpty.await(); // Wait until not empty
                    }
                    int value = buffer.removeFirst();
                    logAction("Consumed: " + value);
                    notFull.signal(); // Signal producer
                } finally {
                    lock.unlock();
                }
                Thread.sleep(500);
            }
        }

        // Logging actions to a file
        private void logAction(String action) {
            try (FileWriter writer = new FileWriter("thread_logs.txt", true)) {
                writer.write(Thread.currentThread().getName() + ": " + action + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Logger Utility (Optional if reused)
    static class ThreadLogger {
        public static synchronized void log(String message) {
            try (FileWriter writer = new FileWriter("thread_logs.txt", true)) {
                writer.write(Thread.currentThread().getName() + ": " + message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Main Method
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                ThreadLogger.log("Producer started");
                buffer.produce();
            } catch (InterruptedException e) {
                ThreadLogger.log("Producer interrupted");
            }
        }, "Producer");

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                ThreadLogger.log("Consumer started");
                buffer.consume();
            } catch (InterruptedException e) {
                ThreadLogger.log("Consumer interrupted");
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            ThreadLogger.log("Main thread interrupted");
        }

        ThreadLogger.log("All threads completed");
    }
}
