package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class GnomeSort extends Sort {
    public GnomeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Gnome");
        this.setRunAllID("Gnome Sort");
        this.setReportSortID("Gnomesort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    // Code retrieved from http://www.algostructure.com/sorting/gnomesort.php
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 1; i < length;)
        {
            if (Reads.compare(array[i], array[i-1]) >= 0)
            {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.02);
            }
            else
            {
                Writes.swap(array, i, i - 1, 0.02, true, false);
                
                Highlights.clearMark(2);
                
                if (i > 1) {
                    i--;
                    Highlights.markArray(1, i);
                    Delays.sleep(0.01);
                }
            }
        }
    }
}