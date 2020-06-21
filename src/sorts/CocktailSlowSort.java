package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Code refactored from Python: http://wiki.c2.com/?SlowSort

final public class CocktailSlowSort extends Sort {
    public CocktailSlowSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Cocktail Slow");
        this.setRunAllID("Cocktail Slow Sort");
        this.setReportSortID("Cocktail Slow Sort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.isBogoSort(false);
    }
    
	private void cocktailslowSort(int[] A, int i, int j) {	
		if (i >= j) {
			return;
		}
	
	    int m = i + ((j - i) / 2);
	    
	    Highlights.markArray(3, m);
	
	    this.cocktailslowSort(A, i, m);
	    this.cocktailslowSort(A, m + 1, j);
	
	    if (Reads.compare(A[m], A[j]) == 1) {
	        Writes.swap(A, m, j, 1, true, false);
	    }
	    if (((j-i)>1)&&(Reads.compare(A[i], A[m+1]) == 1)) {
	        Writes.swap(A, i, m+1, 1, true, false);
	    }
	    
	    Highlights.markArray(1, j);
	    Highlights.markArray(2, m);
	    
	    this.cocktailslowSort(A, i+1, j - 1);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.cocktailslowSort(array, 0, currentLength - 1);
    }
}