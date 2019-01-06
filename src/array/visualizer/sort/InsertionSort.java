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
public class InsertionSort {
    public static void insertionSort() {
        int pos;
        for(int i = 1; i < array.length; i++){
            pos = i;
            marked.set(1, i);
            marked.set(2, -5);
            while(pos>0&&array[pos]<=array[pos-1]){
                comps+=2;
                swap(array, pos, pos-1, 0.02);
                pos--;
            }
        }
    }
    
    public static void insertionSort(int start, int end, double slpamt) {
        int pos;
        for(int i = start; i < end; i++){
            pos = i;
            marked.set(1, i);
            marked.set(2, -5);
            while(pos>start&&array[pos]<=array[pos-1]){
                    comps+=2;
                    swap(array, pos, pos-1);
                    sleep(slpamt);
                    pos--;
            }
        }
    }
}
