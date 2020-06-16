package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * This version of Odd-Even Merge Sort was taken from here, written by wkpark on StackOverflow:
 * https://stackoverflow.com/questions/34426337/how-to-fix-this-non-recursive-odd-even-merge-sort-algorithm
 */

final public class IterativeOddEvenMergeSort extends Sort {
    public IterativeOddEvenMergeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Iter. Odd-Even Merge");
        this.setRunAllID("Iterative Odd-Even Merge Sort");
        this.setReportSortID("Iterative Odd-Even Mergesort");
        this.setCategory("Concurrent Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int p = 1; p < currentLength; p += p)
          for (int k = p; k > 0; k /= 2)
            for (int j = k % p; j + k < currentLength; j += k + k)
              for (int i = 0; i < k; i++)
                if ((i + j)/(p + p) == (i + j + k)/(p + p)) {
			if(i+j+k < currentLength){
                   Highlights.markArray(1, i + j);
                   Highlights.markArray(2, i + j + k);
                   Delays.sleep(1);
                   if(Reads.compare(array[i + j], array[i + j + k]) > 0)
                      Writes.swap(array, i + j, i + j + k, 1, true, false);
                        }
                }
    }
}