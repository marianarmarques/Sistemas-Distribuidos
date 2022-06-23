package LockBank;

import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account {
        private int balance;
        Account(int balance) { this.balance = balance; }
        int balance() { return balance; }
        boolean deposit(int value) {
            balance += value;
            return true;
        }
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    // LockBank.Bank slots and vector of accounts
    private int slots;
    private Account[] av;
    ReentrantLock l = new ReentrantLock();

    public Bank(int n)
    {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    public int balance(int id) {
        try {
            l.lock();
            if (id < 0 || id >= slots)
                return 0;
            return av[id].balance();
        }
        finally {
            l.unlock();
        }
    }

    // Deposit
    boolean deposit(int id, int value) {
        try {
            l.lock();
            if (id < 0 || id >= slots)
                return false;
            return av[id].deposit(value);
        }
        finally {
            l.unlock();
        }
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        try {
            l.lock();
            if (id < 0 || id >= slots)
                return false;
            return av[id].withdraw(value);
        }
        finally {
            l.unlock();
        }
    }

    boolean transfer (int from, int to, int value) {
        try {
            l.lock();
            if(from<0 || from>=slots || to<0 || to>=slots) return false;

            return av[from].withdraw(value) && av[to].deposit(value);
        }
        finally {
            l.unlock();
        }
    }

    int totalBalance() {
        int total = 0;
        try {
            l.lock();
            for(Account a : av) {
                total += a.balance();
            }
            return total;
        }
        finally {
            l.unlock();
        }
    }
}