package OS.prac4;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * Created by Rebecca D'souza on 24/02/2018
 * */
public class Main {
    /*
    * Initializing the circular queue ie the buffer
    * We create pointers to the rear and front ends
    * of the queue
    * */
    static int buffer [] = new int[5];
    static int front = -1;
    static int rear = -1;
    /*
    * Initialising the empty full and binary mutex semaphores
    * */
    static Semaphore mutex = new Semaphore(1);
    static Semaphore empty = new Semaphore(5);
    static Semaphore full = new Semaphore(0);
    public static void main(String args[]){
        /*
        * The time to put the main thread to sleep
        * as well as the number of producer and
        * consumer threads to be created are taken
        * in through as command line arguments
        * */
        int time = Integer.parseInt(args[0]);
        int noProducer = Integer.parseInt(args[1]);
        int noConsumer = Integer.parseInt(args[2]);
        try {
            /*
             *The producer and consumer threads are created
             */
            for(int i = 0;i<noProducer;i++){
                producer p = new producer(i+1);
                p.start();
            }
            for(int i = 0;i<noConsumer;i++){
                consumer c = new consumer(i+1);
                c.start();
            }
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class producer extends Thread{
    private int id;
    producer(int id){ this.id = id; }
    @Override
    public void run(){
        if (!(Main.front==0 && Main.rear==Main.buffer.length-1) && !(Main.front-1== Main.rear))
            try {
                Random r = new Random();
                int item = r.nextInt(100);
                /*
                * Before producer can produce an item,
                * it must acquire a permit from empty
                * and mutex
                */
                Main.empty.acquire();
                Main.mutex.acquire();
                if(Main.front==-1)
                    Main.front = Main.rear = 0;
                else Main.rear = (Main.rear+1)%Main.buffer.length;
                Main.buffer[Main.rear] = item;
                System.out.println("Producer "+this.id+" added "+item+" to buffer.");
                /*
                * After producer produces the item,
                * it releases mutex and full
                */
                Main.mutex.release();
                Main.full.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        else
            /*
             * Buffer is full producer thread can't add anything more to the buffer
             * */
            System.out.println("Producer "+this.id+" can't add item since buffer is full.");
    }
}
class consumer extends Thread{
    private int id;
    consumer(int id){ this.id = id; }
    @Override
    public void run(){
        if (Main.front!=-1)
            try {
                /*
                 * Before consumer can consume an item,
                 * it must acquire a permit from full
                 * and mutex
                 */
                Main.full.acquire();
                Main.mutex.acquire();
                int item = Main.buffer[Main.front];
                if(Main.front == Main.rear)
                    Main.front = Main.rear = -1;
                else Main.front = (Main.front+1)%Main.buffer.length;
                System.out.println("Consumer "+this.id+" consumed "+item+" from buffer.");
                /*
                 * After producer produces the item,
                 * it releases mutex and empty
                 */
                Main.mutex.release();
                Main.empty.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        else
            /*
            * Buffer is empty consumer thread can't consume anything
            * */
            System.out.println("Consumer "+this.id+" can't consume since buffer is empty.");
    }
}