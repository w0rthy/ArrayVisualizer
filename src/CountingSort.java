/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Analysis.*;

/**
 *
 * @author S630690
 */
public class CountingSort {
    public static void countingSort() throws Exception {
        int max = analyzemax();
        int[] counts = new int[max+1];
        for(int i = 0; i < array.length; i++){
            marked.set(1,i);
            sleep(2);
            counts[array[i]]++;
            aa++;
        }
        int x = 0;
        for(int i = 0; i < array.length; i++){
            if(counts[x]==0)
                x++;
            array[i]=x;
            aa++;
            counts[x]--;
            marked.set(1, i);
            sleep(2);
        }
    }
}
