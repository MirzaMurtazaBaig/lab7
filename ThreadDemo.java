package task;

class CalculationThread extends Thread {
    private int start, end, sum;

    public CalculationThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
            try {
                Thread.sleep(100); // Simulate delay
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
        System.out.println("Sum from " + start + " to " + end + " is: " + sum);
    }
}

class LoggingThread extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("Logging data... " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Logging thread interrupted");
        }
    }
}

public class ThreadDemo {
    public static void main(String[] args) {
        CalculationThread calcThread = new CalculationThread(1, 10);
        LoggingThread logThread = new LoggingThread();

        calcThread.start();
        logThread.start();

        try {
            calcThread.join(); // Wait for calcThread to finish
            logThread.join(); // Wait for logThread to finish
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("All threads completed.");
    }
}
