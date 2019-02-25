/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;
import static array.visualizer.Analysis.*;

/**
 *
 * @author Skeen
 */
public class BinaryQuickSort {

    public static boolean getBit(int n, int k) {
        return ((n >> k) & 1) == 1;
    }

    public static void bquickSort() throws Exception
    {
        int msb = analyze(2);
        bQuickSort(array, 0, array.length-1, msb);
    }

    public static void bQuickSort(int[] a, int min, int max, int bit) throws InterruptedException
    {
        if (min >= max || bit < 0)
            return;
        marked.set(3, max);
        marked.set(4, min);

        int left = min;
        int right = max;
    
        while (left <= right) {
            sleep(1);
            boolean left_set = getBit(a[left], bit);
            boolean right_set = getBit(a[right], bit);
            aa++;
            aa++;

            // Both are set, we need to find space for left
            if (left_set && right_set)
            {
                right--;
                marked.set(2,right);
            } 
            // Left is set, right is not, swap them, both are in their places now
            else if (left_set && !right_set)
            {
                swap(a, left, right);
                left++;
                marked.set(1,left);
                right--;
                marked.set(2,right);
            } 
            // Both are correct out of the gate
            else if (!left_set && right_set)
            {
                left++;
                marked.set(1,left);
                right--;
                marked.set(2,right);
            }
            // Neither are set, we need to find space for right
            else
            {
                left++;
                marked.set(1,left);
            }
        }

        int partition = right;
        while (getBit(a[partition], bit) && partition < max) {
            partition++;
        }
        while (getBit(a[partition], bit) && partition > min) {
            partition--;
        }

        bQuickSort(a, min, partition, bit-1);
        bQuickSort(a, partition+1, max, bit-1);
    }
}
