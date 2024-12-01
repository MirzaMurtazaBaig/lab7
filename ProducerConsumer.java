package task;

import java.util.LinkedList;

class Buffer {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int capacity = 5;

    public synchronized void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            while (list.size() == capacity) {
                wait(); // Wait if buffer is full
            }
            list.add(value++);
            System.out.println("Produced: " + value);
            notify(); // Notify consumer
            Thread.sleep(500);
        }
    }

    public synchronized void consume() throws InterruptedException {
        while (true) {
            while (list.isEmpty()) {
                wait(); // Wait if buffer is empty
            }
            int value = list.removeFirst();
            System.out.println("Consumed: " + value);
            notify(); // Notify producer
            Thread.sleep(500);
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Thread producer = new Thread(() -> {
            try {
                buffer.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
