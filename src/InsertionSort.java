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
public class InsertionSort {
    public static void insertionSort() {
        int pos;
        for(int i = 1; i < array.length; i++){
            pos = i;
            marked.set(0, i);
            while(pos>0&&array[pos]<=array[pos-1]){
                    comps+=2;
                    swap(array, pos, pos-1,Math.max(pos%50-48,0));
                    pos--;
            }
        }
    }
    
    public static void insertionSort(int start, int end, double slpamt) {
        int pos;
        for(int i = start; i < end; i++){
            pos = i;
            marked.set(0, i);
            while(pos>start&&array[pos]<=array[pos-1]){
                    comps+=2;
                    swap(array, pos, pos-1);
                    sleep(slpamt);
                    pos--;
            }
        }
    }
}
