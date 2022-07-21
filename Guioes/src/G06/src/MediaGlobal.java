import java.util.concurrent.locks.ReentrantLock;

public class MediaGlobal {
    private int n;
    private int total;
    ReentrantLock l = new ReentrantLock();

    public MediaGlobal() {
        this.total = 0;
        this.n = 0;
    }


    void acrescenta(int valor) {
        try {
            this.l.lock();
            this.n++;
            this.total += valor;
        }
        finally {
            this.l.unlock();
        }
    }

    int calculaMedia() {
        try {
            l.lock();
            return (this.total / this.n);
        }
        finally {
            l.unlock();
        }
    }
}
