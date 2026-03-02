package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class DualPivotQuickSort extends Sort {
    public DualPivotQuickSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Dual-Pivot Quick");
        this.setRunAllID("Dual-Pivot Quick Sort");
        this.setReportSortID("Dual-Pivot Quicksort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    /*
     * This example of a basic Dual-Pivot Quicksort may be found here, written by Sebastian Wild (Sebastian on StackOverflow):
     * https://cs.stackexchange.com/questions/24092/dual-pivot-quicksort-reference-implementation
     */
    private void dualPivot(int[] a, int left, int right) {
        if (right > left) {       
            if (Reads.compare(a[left], a[right]) == 1) {
                Writes.swap(a, left, right, 1, true, false);
            }
            
            int p = a[left];
            int q = a[right];
            
            int l = left + 1;
            int g = right - 1;
            int k = l;

            while (k <= g)
            {
                Delays.sleep(0.1);
                
                if (Reads.compare(a[k], p) == -1) {
                    Writes.swap(a, k, l, 1, true, false);
                    Highlights.clearMark(2);
                    
                    l++;
                    Highlights.markArray(4, l);
                }
                else if (Reads.compare(a[k], q) >= 0) {
                    while (Reads.compare(a[g], q) == 1 && k < g) {
                        g--;
                        Highlights.markArray(3, g);
                        Delays.sleep(0.2);
                    }
                    
                    Writes.swap(a, k, g, 1, true, false);
                    Highlights.clearMark(2);
                    
                    g--;
                    Highlights.markArray(3, g);
                    
                    if (Reads.compare(a[k], p) == -1) {
                        Writes.swap(a, k, l, 0.2, true, false);
                        Highlights.clearMark(2);
                        
                        ++l;
                        Highlights.markArray(4, l);
                    }
                }
                ++k;
                Highlights.markArray(1, k);
                Delays.sleep(0.2);
            }
            
            --l;
            ++g;
            
            Writes.swap(a, left, l, 1, true, false);
            
            Highlights.clearMark(2);
            
            Writes.swap(a, right, g, 1, true, false);
            
            Highlights.clearMark(2);
            Highlights.clearMark(3);
            Highlights.clearMark(4);

            this.dualPivot(a, left, l - 1);
            this.dualPivot(a, l + 1, g - 1);
            this.dualPivot(a, g + 1, right);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.dualPivot(array, 0, currentLength - 1);
    }
}