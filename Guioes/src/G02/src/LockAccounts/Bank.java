package LockAccounts;

import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account {
        private int balance;
        ReentrantLock lockAccount = new ReentrantLock();
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

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av;

    public Bank(int n)
    {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    public int balance(int id) {
        try {
            av[id].lockAccount.lock();
            if (id < 0 || id >= slots)
                return 0;
            return av[id].balance();
        }
        finally {
            av[id].lockAccount.unlock();
        }
    }

    // Deposit
    boolean deposit(int id, int value) {
        try {
            av[id].lockAccount.lock();
            if (id < 0 || id >= slots)
                return false;
            return av[id].deposit(value);
        }
        finally {
            av[id].lockAccount.unlock();
        }
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        try {
            av[id].lockAccount.lock();
            if (id < 0 || id >= slots)
                return false;
            return av[id].withdraw(value);
        }
        finally {
            av[id].lockAccount.unlock();
        }
    }

    boolean transfer (int from, int to, int value) {
        if(from<0 || from>=slots || to<0 || to>=slots) return false;

        int menorId = Math.min(from, to);
        int maiorId = Math.max(from, to);

        av[menorId].lockAccount.lock();
        av[maiorId].lockAccount.lock();

        try {
            return av[from].withdraw(value) && av[to].deposit(value);
        }
        finally {
            av[maiorId].lockAccount.unlock();
            av[menorId].lockAccount.unlock();
        }
    }

    int totalBalance() {
        int total = 0;

        for(Account a : av) {
            try {
                a.lockAccount.lock();
                total += a.balance();
            } finally {
                a.lockAccount.unlock();
            }
        }
        return total;
    }
}