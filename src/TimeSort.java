/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;


import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.InsertionSort.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author S630690
 */
public class TimeSort {
    public static void timeSort(int magnitude) throws Exception {
        final int A = magnitude;
        next = 0;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        final int[] tmp = array.clone();
        for(int i = 0; i < array.length; i++){
            final int c = i;
            threads.add(new Thread(){
                public void run() {
                    int a = tmp[c];
                    try {
                        sleep(a*A);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    report(a);
                }
            });
        }
        for(Thread t : threads)
            t.start();
        Thread.sleep(array.length * A);
        insertionSort(0,array.length,0.2d);
        
    }
    static volatile int next = 0;
    public static synchronized void report(int a){
        marked.set(0,next);
        array[next] = a;
        aa++;
        next++;
    }
}
