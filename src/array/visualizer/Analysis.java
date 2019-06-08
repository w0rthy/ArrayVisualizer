/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.ANALYZE;
import static array.visualizer.ArrayVisualizer.calcReal;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;

/**
 *
 * @author S630690
 */
public class Analysis {

    private static int a;
    private static int log;

    public static int analyze(int[] array, int length, int base, double sleep, boolean max, boolean mark) {
        ANALYZE = true;
        @SuppressWarnings("unused")
        long time = 0, startTime = 0, stopTime;
        a = 0;

        if(max) {
            for(int i = 0; i < length; i++) {
                if(mark) {
                    marked.set(1, i);
                    sleep(sleep);
                }
                if(array[i] > a) {
                    if(calcReal) {
                        startTime = System.nanoTime();
                    }
                    a = array[i];
                    if(calcReal) {
                        stopTime = System.nanoTime();
                        time += (stopTime - startTime) / 1e+6;
                    }
                }
            }
            ANALYZE = false;
            return a;
        }
        else {
            for(int i = 0; i < length; i++) {
                log = (int) (Math.log(array[i]) / Math.log(base));

                if(mark) {
                    marked.set(1, i);
                    sleep(sleep);
                }
                if(log > a){
                    if(calcReal) {
                        startTime = System.nanoTime();
                    }
                    a = log;
                    if(calcReal) {
                        stopTime = System.nanoTime();
                        time += (stopTime - startTime) / 1e+6;
                    }
                }
            }
            ANALYZE = false;
            return a;
        }
    }
}