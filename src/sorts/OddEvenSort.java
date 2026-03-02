package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * This version of Odd-Even Sort was taken from here, written by Rachit Belwariar:
 * https://www.geeksforgeeks.org/odd-even-sort-brick-sort/
 */

final public class OddEvenSort extends Sort {
    public OddEvenSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Odd-Even");
        this.setRunAllID("Odd-Even Sort");
        this.setReportSortID("Odd-Even Sort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean sorted = false;
        
        while (!sorted) {
            sorted = true;
    
            for (int i = 1; i < length - 1; i += 2) {
                if(Reads.compare(array[i], array[i + 1]) == 1) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    sorted = false;
                }
                
                Highlights.markArray(1, i);
                Delays.sleep(0.025);
            }
    
            for (int i = 0; i < length - 1; i += 2) {
                if(Reads.compare(array[i], array[i + 1]) == 1) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    sorted = false;
                }
                
                Highlights.markArray(2, i);
                Delays.sleep(0.025);
            }
        }
    }
}