package GameEngine;

/**
 * Created by jowens on 3/20/17.
 */
public class ThreadDemo {
    static public void main(String... args){
        Thread t1 = new Thread(new Runnable(){
            private boolean left = true;
            public void run() {
                for(int i = 0; i<10; ++i){
                    step();
                    Thread.sleep(1000);
                }
            }
            private void step(){
                left = !left;
                System.out.println(left ? "left" : "right");
            }
        });
        Thread t2 = new Thread(new Runnable(){
            private boolean left = true;
            public void run() {
                for(int i = 0; i<10; ++i){
                    System.out.println("chomp");
                    try{Thread.sleep(1000);}
                    catch(InterruptedException ie){

                    }
                }
            }
            private void step(){
                left = !left;
                System.out.println(left ? "left" : "right");
            }
        });

        t1.start();
        t2.start();
    }
}
