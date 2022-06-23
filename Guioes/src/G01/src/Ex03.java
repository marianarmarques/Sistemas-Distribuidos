import java.util.concurrent.locks.ReentrantLock;

public class Ex03 {

    public static void main(String[] args) throws Exception {
        Bank b = new Bank();
        Thread [] threads = new Thread[10];
        ReentrantLock l = new ReentrantLock();

        for(int i=0; i<10; i++) {
            threads[i] = new Thread(() -> {
                for(int d=0; d<1000; d++) {
                    try {
                        l.lock();
                        b.deposit(100);
                    }
                    finally {
                        l.unlock();
                    }
                }
            });
        }

        for(Thread t : threads) t.start();
        for(Thread t : threads) t.join();

        System.out.println(String.format("Total Account Balance %d.", b.balance()));
    }
}
