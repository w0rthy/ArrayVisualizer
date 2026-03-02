package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

// Code refactored from: https://en.wikipedia.org/wiki/Stooge_sort
final public class StoogeSort extends Sort {
    public StoogeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Stooge");
        this.setRunAllID("Stooge Sort");
        this.setReportSortID("Stoogesort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.isBogoSort(false);
    }
    
	private void stoogeSort(int[] A, int i, int j) {
	    if (Reads.compare(A[i], A[j]) == 1) {
	        Writes.swap(A, i, j, 0.005, true, false);
	    }
	    
	    Delays.sleep(0.0025);
	    
	    Highlights.markArray(1, i);
        Highlights.markArray(2, j);
	    
        if (j - i + 1 >= 3) {
	        int t = (j - i + 1) / 3;
	        
	        Highlights.markArray(3, j - t);
	        Highlights.markArray(4, i + t);
	
	        this.stoogeSort(A, i, j-t);
	        this.stoogeSort(A, i+t, j);
	        this.stoogeSort(A, i, j-t);
	    }
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stoogeSort(array, 0, currentLength - 1);
    }
}