package sorts;

import templates.ZipSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/* translated from Richard Cookman's original MIT licensed C++ implementation
 * https://github.com/ceorron/stable-inplace-sorting-algorithms#zip_sort
 * by Lucy Phipps
 */

final public class ZipSort extends ZipSorting {
    public ZipSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        this.setSortPromptID("Zip");
        this.setRunAllID("Zip Sort");
        this.setReportSortID("Zipsort");
        this.setCategory("Merge Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.zipSort(array, 0, length, false, 1, true, false);
    }
}