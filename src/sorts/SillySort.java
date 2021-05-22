package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Written by Tom Duff, and found here: http://home.tiac.net/~cri_d/cri/2001/badsort.html
// from https://stackoverflow.com/questions/2609857/are-there-any-worse-sorting-algorithms-than-bogosort-a-k-a-monkey-sort/

final public class SillySort extends Sort {
    public SillySort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Silly");
        this.setRunAllID("Silly Sort");
        this.setReportSortID("Sillysort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.isBogoSort(false);
    }
    
    private void sillySort(int[] array, int i, int j) {
		int m;
		
		Delays.sleep(1);
		
		if (i < j) {
		    /* find the middle of the array */
		    m = i + ((j - i) / 2);
		    
		    /* 
		     * use this function (recursively) to find put the minimum elements of 
		     * each half into the first elements of each half
		     */
		    this.sillySort(array, i, m);
		    this.sillySort(array, m + 1, j);
		    
		    /* 
		     * Choose the smallest element of the two halves, and put that element in
		     * the first position
		     */
		    if (Reads.compare(array[i], array[m + 1]) >= 0) { 
		        Writes.swap(array, i, m + 1, 1, true, false);
		    }
		    
		    Highlights.markArray(1, i);
		    Highlights.markArray(2, m + 1);
		    
		    this.sillySort(array, i + 1, j);
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.sillySort(array, 0, currentLength - 1);
    }
}