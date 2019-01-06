/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

/**
 *
 * @author S630690
 */
public class InsertionSort {
    public static void insertionSort(final ArrayController ac) {
        int pos;
        for(int i = 1; i < ac.length; i++){
            pos = i;
            ac.marked.set(1, i);
            ac.marked.set(2, -5);
            while(pos>0&&ac.array[pos]<=ac.array[pos-1]){
                ac.comps+=2;
                swap(ac, pos, pos-1, 0.02);
                pos--;
            }
        }
    }
    
    public static void insertionSort(final ArrayController ac, int start, int end, double slpamt) {
        int pos;
        for(int i = start; i < end; i++){
            pos = i;
            ac.marked.set(1, i);
            ac.marked.set(2, -5);
            while(pos>start&&ac.array[pos]<=ac.array[pos-1]){
                ac.comps+=2;
                    swap(ac, pos, pos-1);
                    sleep(slpamt);
                    pos--;
            }
        }
    }
}
