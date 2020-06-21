package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class LLQuickSort extends Sort {
    public LLQuickSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("LL Quick");
        this.setRunAllID("Quick Sort, Left/Left Pointers");
        this.setReportSortID("Left/Left Quicksort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    private int partition(int[] array, int lo, int hi) {
        int pivot = array[hi];
        int i = lo;
        
        for(int j = lo; j < hi; j++) {
            if(Reads.compare(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 1, true, false);
                i++;
            }
        }
        Writes.swap(array, i, hi, 1, true, false);
        return i;
    }
    
    private void quickSort(int[] array, int lo, int hi) {
        if(lo < hi) {
            int p = this.partition(array, lo, hi);
            this.quickSort(array, lo, p - 1);
            this.quickSort(array, p + 1, hi);
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}