import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Barrier {
    private int nThreads;

    private int current;

    private int round;
    private ReentrantLock lock;
    private Condition waitForAll;

    Barrier (int N) {
        this.nThreads = N;
        this.current = 0;
        this.round=0;
        this.lock = new ReentrantLock();
        this.waitForAll = lock.newCondition();
    }

    //Ex1.
    void await1() throws InterruptedException {
        this.lock.lock();
        this.current++;

        if(this.current<this.nThreads) {
            System.out.println(Thread.currentThread().getName() + " is waiting ...");
            while(this.current<this.nThreads) this.waitForAll.await();
        }
        else {
            System.out.println(Thread.currentThread().getName() + " is waking up the others ...");
            this.waitForAll.signalAll();
        }

        System.out.println(Thread.currentThread().getName() + " is leaving ...");
        this.lock.unlock();
    }

    //Ex2.
    void await2() throws InterruptedException {
        this.lock.lock();
        this.current++;
        int myRound = this.round;

        if(this.current<this.nThreads) {
            System.out.println(Thread.currentThread().getName() + " is waiting ...");
            while(myRound==this.round) this.waitForAll.await();
        }
        else {
            System.out.println(Thread.currentThread().getName() + " is waking up the others ...");
            this.waitForAll.signalAll();
            this.round++;
            this.current=0;
        }

        System.out.println(Thread.currentThread().getName() + " is leaving ...");
        this.lock.unlock();
    }

}
