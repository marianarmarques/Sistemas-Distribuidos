import java.util.*;
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

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
    ReentrantLock lockBanco = new ReentrantLock();


    // create account and return account id
    public int createAccount(int balance) {
        try {
            this.lockBanco.lock();

            Account c = new Account(balance);
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        }
        finally {
            this.lockBanco.unlock();
        }
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        this.lockBanco.lock();

        Account c = map.remove(id);
        if (c == null) {
            this.lockBanco.unlock();
            return 0;
        }

        c.lockAccount.lock();
        this.lockBanco.unlock();

        int saldo = c.balance();
        c.lockAccount.unlock();

        return saldo;
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        this.lockBanco.lock();

        Account c = map.get(id);
        if (c == null) {
           this.lockBanco.unlock();
            return 0;
        }
        c.lockAccount.lock();
        this.lockBanco.unlock();

        int saldo = c.balance;
        c.lockAccount.unlock();

        return saldo;
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        this.lockBanco.lock();

        Account c = map.get(id);
        if (c == null) {
            this.lockBanco.unlock();
            return false;
        }
        c.lockAccount.lock();
        this.lockBanco.unlock();

        boolean dep = c.deposit(value);

        c.lockAccount.unlock();

        return dep;
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        this.lockBanco.lock();

        Account c = map.get(id);
        if (c == null) {
            this.lockBanco.unlock();
            return false;
        }
        c.lockAccount.lock();
        this.lockBanco.unlock();

        boolean with = c.withdraw(value);

        c.lockAccount.unlock();

        return with;
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        int idMin = Math.min(from, to);
        int idMax = Math.max(from, to);

        this.lockBanco.lock();
        Account cfrom = map.get(idMin);
        Account cto = map.get(idMax);

        if (cfrom == null || cto ==  null) {
            this.lockBanco.unlock();
            return false;
        }

        cfrom.lockAccount.lock();
        cto.lockAccount.lock();
        this.lockBanco.unlock();

        boolean transf = cfrom.withdraw(value) && cto.deposit(value);

        cto.lockAccount.unlock();
        cfrom.lockAccount.unlock();

        return transf;
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total=0;
        List<Account> accLoocked = new ArrayList<>();

        this.lockBanco.lock();
        for (int i : ids) {
            if(map.containsKey(i)) {
                Account a = map.get(i);
                a.lockAccount.lock();
                accLoocked.add(a);
            }
        }
        this.lockBanco.unlock();

        for(Account a : accLoocked) {
            total = a.balance();
            a.lockAccount.unlock();
        }
        return total;
    }

}