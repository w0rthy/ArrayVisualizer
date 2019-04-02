/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Analysis.*;
import static array.visualizer.utils.Swaps.*;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Binary MSD Radix Sort / Binary Quicksort.
 *
 * Implemented as recursive decent, and via task queue, see:
 * * binaryQuickSortRecursive, and
 * * binaryQuickSort respectively.
 *
 * Both of which are in-place sorting algorithms, with the recursive utilizing
 * the stack for divide-and-conquer, while the non-recursive utilizes a queue.
 *
 * Can be extended to support unsigned integers, by sorting the first bit rin
 * reverse. Can be made stable at the cost of O(n) memory. Can be parallalized
 * to O(log2(n)) subtasks / threads.
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

    public static class Task {
        public int p;
        public int r;
        public int bit;

        public Task(int p, int r, int bit)
        {
            this.p = p;
            this.r = r;
            this.bit = bit;
        }
    }

    public static void binaryQuickSortRecursive(final ArrayController ac, int p, int r, int bit)
    {
        if (p < r && bit >= 0)
        {
            int q=partition(ac, p, r, bit);
            sleep(1);
            binaryQuickSortRecursive(ac, p, q, bit-1);
            binaryQuickSortRecursive(ac, q+1, r, bit-1);
        }
    }

    public static void binaryQuickSort(final ArrayController ac, int p, int r, int bit)
    {
        Queue<Task> tasks = new LinkedList<Task>();
        tasks.add(new Task(p, r, bit));

        while (tasks.isEmpty() == false) 
        {
            Task task = tasks.remove();
            if (task.p < task.r && task.bit >= 0)
            {
                int q=partition(ac, task.p, task.r, task.bit);
                sleep(1);
                tasks.add(new Task(task.p, q, task.bit-1));
                tasks.add(new Task(q+1, task.r, task.bit-1));
            }
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
        // binaryQuickSortRecursive(ac, 0, ac.length-1, msb);
    }
}
