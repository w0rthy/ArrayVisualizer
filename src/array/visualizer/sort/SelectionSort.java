/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

/**
 *
 * @author S630690
 */
public class SelectionSort {
       
    public static void selectionSort() throws Exception {
        for (int i = 0; i < array.length - 1; i++) {
            int lowestindex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[lowestindex]){
                    lowestindex = j;
                }
                sleep(0.01);
                comps++;
            }
            swap(array, i, lowestindex);
        }
    }
}
