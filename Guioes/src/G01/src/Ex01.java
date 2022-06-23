public class Ex01 {

    public static void main(String[] args) throws Exception {
        Thread threads[] = new Thread[10];

        for(int i =0; i<10; i++) {
            threads[i] = new Thread(new Increment());
        }

        for(Thread t : threads) t.start();
        for(Thread t : threads) t.join();

        System.out.println("fim");
    }
}
