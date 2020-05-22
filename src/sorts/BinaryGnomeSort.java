package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class BinaryGnomeSort extends Sort {
    public BinaryGnomeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Binary Gnome");
        this.setRunAllID("Optimized Gnome Sort + Binary Search");
        this.setReportSortID("Optimized Gnomesort + Binary Search");
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
        for (int i = 1; i < length; i++) {
            int num = array[i];

            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2);
                
                Highlights.markArray(1, lo);
                Highlights.markArray(3, mid);
                Highlights.markArray(2, hi);
                
                Delays.sleep(0.05);
                
                if (Reads.compare(num, array[mid]) < 0) { // do NOT shift equal elements past each other; this maintains stability!
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo

            Highlights.clearMark(1);
            Highlights.clearMark(2);
            
            int j = i;
            while (j > lo) {   
                Writes.swap(array, j, j - 1, 0.01, true, false);
                j--;
            }
        }         
    }
}