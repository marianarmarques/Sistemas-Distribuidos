public class Ex02 {

    public static void main(String[] args) throws Exception {
        Thread [] threads = new Thread[10];
        Bank b = new Bank();

        for(int i=0; i<10; i++) {
            threads[i] = new Thread(() -> {
                for(int d=0; d<1000; d++) b.deposit(100);
            });
        }

        for(Thread t : threads) t.start();
        for(Thread t : threads) t.join();

        System.out.println(String.format("Total Account Balance %d.", b.balance()));
    }
}
