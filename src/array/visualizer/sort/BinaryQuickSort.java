/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Analysis.*;
import static array.visualizer.utils.Swaps.*;

/**
 *
 * @author Skeen
 */
public class BinaryQuickSort implements Sort {

    public static boolean getBit(int n, int k) {
        // Find boolean value of bit k in n
        return ((n >> k) & 1) == 1;
    }

    public static int analyzeBit(final ArrayController ac) {
        // Find highest bit of highest value
        int max = 0;
        for(int i = 0; i < ac.length; i++){
            ac.marked.set(1, i);
            ac.aa++;
            sleep(1);
            max = Math.max(max, ac.array[i]);
        }
        return 31 - Integer.numberOfLeadingZeros(max);
    }

    public static void binaryQuickSort(final ArrayController ac, int p, int r, int bit)
    {
        if (p < r && bit >= 0)
        {
            int q=partition(ac, p, r, bit);
            sleep(1);
            binaryQuickSort(ac, p, q, bit-1);
            binaryQuickSort(ac, q+1, r, bit-1);
        }
    }

    public static int partition(final ArrayController ac, int p, int r, int bit)
    {
        int i = p - 1;
        int j = r + 1;
    
        while (true) {
            // Left is not set
            i++;
            while(i < r && !getBit(ac.array[i], bit)) {
                i++;
                ac.marked.set(1, i);
                sleep(0.45);
                ac.comps+=2;
            }
            // Right is set
            j--;
            while(j > p && getBit(ac.array[j], bit)) {
                j--;
                ac.marked.set(2, j);
                sleep(0.45);
                ac.comps+=2;
            }
            // If i is less than j, we swap, otherwise we are done
            if (i < j)
                swap(ac, i, j);
            else
                return j;
        }
    }
    
    @Override
    public String name()
    {
        return "Binary Quicksort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        int msb = analyzeBit(ac);
        binaryQuickSort(ac, 0, ac.length-1, msb);
    }
}
