package sorts;

import templates.ShellSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps

final public class ShellSort extends ShellSorting {
    public ShellSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Shell");
        this.setRunAllID("Shell Sort");
        this.setReportSortID("Shellsort");
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
        this.shellSort(this.ArrayVisualizer, array, currentLength);
    }
}