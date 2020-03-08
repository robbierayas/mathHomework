package mathHomework.threads;

import java.util.Map;

public class RIPEMD160Thread extends Thread {

    static final Object mutex = new Object();
    volatile static Map<String, String> map;
    static volatile int i = 1;
    private volatile boolean alive = true;

    public RIPEMD160Thread(Map<String, String> messages) {
        map = messages;
    }

    public void terminate() {
        this.alive = false;
    }

    @Override
    public void run() {
//        RIPEMD160.ripemd160("");

        synchronized (mutex) {

            for (Map.Entry<String, String> entry : map.entrySet()) {

                if (entry.getValue() == null) {
                    System.out.println(entry.getKey() + "\" : \"Printed by " + Thread.currentThread().getName() + '\"');

                    map.put(entry.getKey(), Thread.currentThread().getName());

                    if (Thread.currentThread().getName().contains("0") && i == 1) {
                        try {
                            mutex.notifyAll();

                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (Thread.currentThread().getName().contains("1") && i <= 1) {
                        try {
                            mutex.notifyAll();
                            i++;
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            mutex.notifyAll();

        }
    }

}
