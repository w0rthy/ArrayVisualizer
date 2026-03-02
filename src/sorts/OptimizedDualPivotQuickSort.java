package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// code retrieved from https://codeblab.com/wp-content/uploads/2009/09/DualPivotQuicksort.pdf
// written by Vladimir Yaroslavskiy

final public class OptimizedDualPivotQuickSort extends Sort {
    private InsertionSort insertSorter;
    
    public OptimizedDualPivotQuickSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Opti. Dual-Pivot Quick");
        this.setRunAllID("Optimized Dual-Pivot Quick Sort");
        this.setReportSortID("Optimized Dual-Pivot Quicksort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    private void dualPivot(int[] array, int left, int right, int divisor) {
        int length = right - left;
        
        // insertion sort for tiny array
        if(length < 27) {
            Highlights.clearMark(2);
            insertSorter.customInsertSort(array, left, right + 1, 0.75, false);
            return;
        }
        
        int third = length / divisor;
        
        // "medians"
        int med1 = left  + third;
        int med2 = right - third;
        
        if(med1 <= left) {
            med1 = left + 1;
        }
        if(med2 >= right) {
            med2 = right - 1;
        }
        if(Reads.compare(array[med1], array[med2]) == -1) {
            Writes.swap(array, med1, left,  1, true, false);
            Writes.swap(array, med2, right, 1, true, false);
        }
        else {
            Writes.swap(array, med1, right, 1, true, false);
            Writes.swap(array, med2, left,  1, true, false);
        }
        
        // pivots
        int pivot1 = array[left];
        int pivot2 = array[right];
        
        // pointers
        int less  = left  + 1;
        int great = right - 1;
        
        // sorting
        for(int k = less; k <= great; k++) {
            if(Reads.compare(array[k], pivot1) == -1) {
                Writes.swap(array, k, less++, 1, true, false);
            }
            else if(Reads.compare(array[k], pivot2) == 1) {
                while(k < great && Reads.compare(array[great], pivot2) == 1) {
                    great--;
                }
                Writes.swap(array, k, great--, 1, true, false);
                
                if(Reads.compare(array[k], pivot1) == -1) {
                    Writes.swap(array, k, less++, 1, true, false);
                }
            }
        }
        
        // swaps
        int dist = great - less;
        
        if(dist < 13) {
            divisor++;
        }
        Writes.swap(array, less  - 1, left,  1, true, false);
        Writes.swap(array, great + 1, right, 1, true, false);
        
        // subarrays
        this.dualPivot(array, left,   less - 2, divisor);
        this.dualPivot(array, great + 2, right, divisor);
        
        // equal elements
        if(dist > length - 13 && pivot1 != pivot2) {
            for(int k = less; k <= great; k++) {
                if(Reads.compare(array[k], pivot1) == 0) {
                    Writes.swap(array, k, less++, 1, true, false);
                }
                else if(Reads.compare(array[k], pivot2) == 0) {
                    Writes.swap(array, k, great--, 1, true, false);
                    
                    if(Reads.compare(array[k], pivot1) == 0) {
                        Writes.swap(array, k, less++, 1, true, false);
                    }
                }
            }
        }
        
        // subarray
        if(pivot1 < pivot2) {
            this.dualPivot(array, less, great, divisor);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.insertSorter = new InsertionSort(Delays, Highlights, Reads, Writes);
        this.dualPivot(array, 0, currentLength - 1, 3);
    }
}