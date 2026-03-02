package sorts;

import templates.BinaryQuickSorting;
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

final public class RecursiveBinaryQuickSort extends BinaryQuickSorting {
    public RecursiveBinaryQuickSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Recurs. Binary Quick");
        this.setRunAllID("Recursive Binary Quick Sort");
        this.setReportSortID("Recursive Binary Quicksort");
        this.setCategory("Distribution Sorts");
        this.isComparisonBased(false);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int mostSignificantBit = Reads.analyzeBit(array, length);
        this.binaryQuickSortRecursive(array, 0, length - 1, mostSignificantBit);
    }
}