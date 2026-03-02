package sorts;

import templates.ShellSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps

final public class MarshmallowSort extends ShellSorting {
    public MarshmallowSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Marshmallow");
        this.setRunAllID("Marshmallow Sort");
        this.setReportSortID("Marshmallow Sort");
        this.setCategory("Insertion Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    public void finishQuickShell(int[] array, int currentLen) {
        this.quickShellSort(array, 0, currentLen);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.marshmallowSort(this.ArrayVisualizer, array, currentLength);
    }
}