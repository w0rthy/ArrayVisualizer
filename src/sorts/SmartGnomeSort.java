package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class SmartGnomeSort extends Sort {
    public SmartGnomeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Smart Gnome");
        this.setRunAllID("Optimized Gnome Sort");
        this.setReportSortID("Optimized Gnomesort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    // Taken from https://en.wikipedia.org/wiki/Gnome_sort
    private void smartGnomeSort(int[] array, int upperBound, double sleep) {
        int pos = upperBound;
        
        while(pos > 0 && Reads.compare(array[pos - 1], array[pos]) == 1) {
            Writes.swap(array, pos - 1, pos, sleep, true, false);
            pos--;
        }
    }

    public void customSort(int[] array, int low, int high, double sleep) {
        for(int i = low + 1; i < high; i++) {
            smartGnomeSort(array, i, sleep);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int i = 1; i < length; i++) {
            smartGnomeSort(array, i, 0.05);
        }    
    }
}