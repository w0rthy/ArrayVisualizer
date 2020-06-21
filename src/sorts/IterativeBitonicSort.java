package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * This version of Bitonic Sort was taken from here, written by Nikos Pitsianis:
 * https://www2.cs.duke.edu/courses/fall08/cps196.1/Pthreads/bitonic.c
 */

final public class IterativeBitonicSort extends Sort {
    public IterativeBitonicSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Iterative Bitonic");
        this.setRunAllID("Iterative Bitonic Sort");
        this.setReportSortID("Iterative Bitonic Sort");
        this.setCategory("Concurrent Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    	
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i, j, k;
        
        for(k = 2; k < currentLength*2; k = 2 * k) {
		int m = ((currentLength+(k-1))/k)%2;
            for(j = k >> 1; j > 0; j = j >> 1) {
                for(i = 0; i < currentLength; i++) {
                    int ij = i ^ j;
                    
                    if((ij) > i && ij < currentLength) {
                   Highlights.markArray(1, i);
                   Highlights.markArray(2, ij);
                   Delays.sleep(1);
                        if((((i & k) == 0)^(m==0)) && Reads.compare(array[i], array[ij]) == 1) 
                            Writes.swap(array, i, ij, 1, true, false);
                        if((((i & k) != 0)^(m==0)) && Reads.compare(array[i], array[ij]) == -1)
                            Writes.swap(array, i, ij, 1, true, false);
                    }
                }
            }
        }
    }
}