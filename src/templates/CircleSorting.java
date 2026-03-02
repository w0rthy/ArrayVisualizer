package templates;

import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
Copyright (c) rosettacode.org.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

public abstract class CircleSorting extends Sort {
    protected CircleSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    protected int circleSortRoutine(int[] array, int lo, int hi, int swapCount) {        
        if (lo == hi)
            return swapCount;
 
        int high = hi;
        int low = lo;
        int mid = (hi - lo) / 2;
 
        while (lo < hi) {
            if (Reads.compare(array[lo], array[hi]) > 0) {
                Writes.swap(array, lo, hi, 1, true, false);
                swapCount++;
            }
            lo++;
            hi--;
            
            Highlights.markArray(1, lo);
            Highlights.markArray(2, hi);
            Delays.sleep(0.5);
        }
 
        if (lo == hi && Reads.compare(array[lo], array[hi + 1]) > 0) {
            Writes.swap(array, lo, hi + 1, 1, true, false);
            swapCount++;
        }
 
        swapCount = this.circleSortRoutine(array, low, low + mid, swapCount);
        swapCount = this.circleSortRoutine(array, low + mid + 1, high, swapCount);
        
        return swapCount;
    }
}