package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class LRQuickSort extends Sort {
    public LRQuickSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("LR Quick");
        this.setRunAllID("Quick Sort, Left/Right Pointers");
        this.setReportSortID("Left/Right Quicksort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    // Thanks to Timo Bingmann for providing a good reference for Quick Sort w/ LR pointers.
    private void quickSort(int[] a, int p, int r) {       
        int pivot = p + (r - p) / 2;
        int x = a[pivot];
        
        int i = p;
        int j = r;

        Highlights.markArray(3, pivot);
        
        while (i <= j) {
            while (Reads.compare(a[i], x) == -1){
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while (Reads.compare(a[j], x) == 1){
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }

            if (i <= j) {
                // Follow the pivot and highlight it.
                if(i == pivot) {
                    Highlights.markArray(3, j);
                }
                if(j == pivot) {
                    Highlights.markArray(3, i);
                }
                
                Writes.swap(a, i, j, 1, true, false);
                
                i++;
                j--;
            }
        }
        
        if(p < j) {
            this.quickSort(a, p, j);
        }
        if(i < r) {
            this.quickSort(a, i, r);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}