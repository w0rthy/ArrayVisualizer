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
            if (Reads.compare(array[start + root - 1], array[start + leaf - 1]) == compareVal) {
                Writes.swap(array, start + root - 1, start + leaf - 1, sleep, true, false);
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
    protected void heapSort2(int[] array, int start, int length, double sleep, boolean isMax) {
        if ((length - start) >= 5){
            heapify(array, start, length, sleep, true);
         
            for (int i = length - start; i > 4; i--) {
                Writes.swap(array, start, start + i - 1, sleep, true, false);
                siftDown(array, 1, i - 1, start, sleep, true);
            }
            for (int i = start; i < (start + 3); i++) {
                int lowestindex = i;
                
                for (int j = i + 1; j < (start+4); j++) {
                    Highlights.markArray(2, j);
                    Delays.sleep(sleep);
                    
                    if (Reads.compare(array[j], array[lowestindex]) == -1){
                        lowestindex = j;
                        Highlights.markArray(1, lowestindex);
                        Delays.sleep(sleep);
                    }
                }
                Writes.swap(array, i, lowestindex, sleep, true, false);
            }
        }
        else{
            for (int i = start; i < length - 1; i++) {
                int lowestindex = i;
                
                for (int j = i + 1; j < length; j++) {
                    Highlights.markArray(2, j);
                    Delays.sleep(sleep);
                    
                    if (Reads.compare(array[j], array[lowestindex]) == -1){
                        lowestindex = j;
                        Highlights.markArray(1, lowestindex);
                        Delays.sleep(sleep);
                    }
                }
                Writes.swap(array, i, lowestindex, sleep, true, false);
            }
        }
    }
}