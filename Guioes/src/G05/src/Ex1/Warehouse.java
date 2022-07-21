package Ex1;

/**
 * Exercício proposto pelo o professor, sem otimização, ou seja, lock
 * ao nível do warehouse, com o objetivo de nos focarmos nas variáveis
 * de condição.
 * */

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
    private Map<String, Product> map =  new HashMap<String, Product>();
    private ReentrantLock lock = new ReentrantLock();

    private class Product {
        int quantity = 0;
        private Condition quantityBellowZero = lock.newCondition();
    }

    private Product get(String item) {
        Product p = map.get(item);
        if (p != null) return p;
        p = new Product();
        map.put(item, p);
        return p;
    }

    public void supply(String item, int quantity) {
        try {
            this.lock.lock();

            Product p = get(item);
            p.quantity += quantity;
            p.quantityBellowZero.signalAll();
        }
        finally {
            this.lock.unlock();
        }
    }

    public void consume(Set<String> items) throws InterruptedException {
        try {
            this.lock.lock();

            for (String s : items) {
                while(get(s).quantity<1) get(s).quantityBellowZero.await();
                get(s).quantity--;
            }
        }
        finally {
            this.lock.unlock();
        }
    }

}
