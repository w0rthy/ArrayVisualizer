/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

/**
 *
 * @author S630690
 */
public class BubbleSort {
    public static void bubbleSort() throws Exception{
        for(int i = array.length-1; i > 0; i--){
            marked.set(0,i);
            for(int j = 0; j < i; j++)
                if(array[j]>array[j+1]){
                    comps++;
                    swap(array, j, j+1, Math.max(j%50-48,0));
                }
        }
    }
}
