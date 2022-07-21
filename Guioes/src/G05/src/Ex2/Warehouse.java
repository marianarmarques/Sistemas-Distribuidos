package Ex2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
    private Map<String, Product> map =  new HashMap<String, Product>();
    private ReentrantLock lock = new ReentrantLock();


    private class Product {
        int quantity = 0;
        private Condition quantityBelowZero = lock.newCondition();
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
            p.quantityBelowZero.signalAll();

        }
        finally {
            this.lock.unlock();
        }
    }

    public void consume(ArrayList<String> items) throws InterruptedException {
        try {
            lock.lock();
            for (int i = 0; i < items.size(); i++) {
                while (this.get(items.get(i)).quantity < 1) {
                    get(items.get(i)).quantityBelowZero.await();
                    i = 0;
                }
            }
            for (String s : items) {
                get(s).quantity--;
            }
        }
        finally {
            lock.unlock();
        }
    }

}
