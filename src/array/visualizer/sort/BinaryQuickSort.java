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
        return ((n >> k) & 1) == 1;
    }

    public static void binaryQuickSort(final ArrayController ac, int p, int r, int pow)
    {
        if (p < r && pow >= 0)
        {
            int q=partition(ac, p, r, pow);
            sleep(1);
            binaryQuickSort(ac, p, q, pow-1);
            binaryQuickSort(ac, q+1, r, pow-1);
        }
    }

    public static int partition(final ArrayController ac, int p, int r, int pow)
    {
        int i = p - 1;
        int j = r + 1;
    
        while (true) {
            // Left is not set
            i++;
            while(i < r && !getBit(ac.array[i], pow)) {
                i++;
                ac.marked.set(1, i);
                sleep(0.45);
                ac.comps+=2;
            }
            // Right is set
            j--;
            while(j > p && getBit(ac.array[j], pow)) {
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
        int highestpower = analyze(ac, 2);
        binaryQuickSort(ac, 0, ac.length-1, highestpower);
    }
}
