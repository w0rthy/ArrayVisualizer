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

public abstract class HeapSorting extends Sort {
    protected HeapSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    private void siftDown(int[] array, int root, int dist, int start, double sleep, boolean isMax) {
        int compareVal = 0;
        
        if(isMax) compareVal = -1;
        else compareVal = 1;
        
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compare(array[start + leaf - 1], array[start + leaf]) == compareVal) {
                leaf++;
            }
            Highlights.markArray(1, start + root - 1);
            Highlights.markArray(2, start + leaf - 1);
            Delays.sleep(sleep);
            if (Reads.compare(array[start + root - 1], array[start + leaf - 1]) == compareVal) {
                Writes.swap(array, start + root - 1, start + leaf - 1, 0, true, false);
                root = leaf;
            }
            else break;
        }
    }

    private void heapify(int[] arr, int low, int high, double sleep, boolean isMax) {
        int length = high - low;
        for (int i = length / 2; i >= 1; i--) {
            siftDown(arr, i, length, low, sleep, isMax);
        }
    }
    
    // This version of heap sort works for max and min variants, alongside sorting 
    // partial ranges of an array.
    protected void heapSort(int[] arr, int start, int length, double sleep, boolean isMax) {
        heapify(arr, start, length, sleep, isMax);
     
        for (int i = length - start; i > 1; i--) {
            Writes.swap(arr, start, start + i - 1, sleep, true, false);
            siftDown(arr, 1, i - 1, start, sleep, isMax);
        }
        
        if(!isMax) {
            Writes.reversal(arr, start, start + length - 1, 1, true, false);
        }
    }
}