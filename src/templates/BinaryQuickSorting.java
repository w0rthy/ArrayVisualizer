package templates;

import java.util.LinkedList;
import java.util.Queue;

import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

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

public abstract class BinaryQuickSorting extends Sort {
    protected BinaryQuickSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
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

    public void binaryQuickSortRecursive(int[] array, int p, int r, int bit)
    {
        if (p < r && bit >= 0)
        {
            int q = partition(array, p, r, bit);
            Delays.sleep(1);
            binaryQuickSortRecursive(array, p, q, bit-1);
            binaryQuickSortRecursive(array, q+1, r, bit-1);
        }
    }

    public void binaryQuickSort(int[] array, int p, int r, int bit)
    {
        Queue<Task> tasks = new LinkedList<>();
        tasks.add(new Task(p, r, bit));

        while (tasks.isEmpty() == false) 
        {
            Task task = tasks.remove();
            if (task.p < task.r && task.bit >= 0)
            {
                int q = partition(array, task.p, task.r, task.bit);
                Delays.sleep(1);
                tasks.add(new Task(task.p, q, task.bit-1));
                tasks.add(new Task(q+1, task.r, task.bit-1));
            }
        }
    }

    public int partition(int[] array, int p, int r, int bit)
    {
        int i = p - 1;
        int j = r + 1;
    
        while (true) {
            // Left is not set
            i++;
            while(i < r && !Reads.getBit(array[i], bit)) {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.45);
            }
            // Right is set
            j--;
            while(j > p && Reads.getBit(array[j], bit)) {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.45);
            }
            // If i is less than j, we swap, otherwise we are done
            if (i < j)
                Writes.swap(array, i, j, 1, true, false);
            else
                return j;
        }
    }
}