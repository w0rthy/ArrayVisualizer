package sorts;

import templates.Sort;
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

/*
modified by Lucy Phipps from ../templates/HeapSorting.java and MinHeapSort.java
the only real changes are subtracting every array access from (length - 1)
and removing the Writes.reverse() at the end
the rest is just compacting tbe code a bit
*/

final public class FlippedMinHeapSort extends Sort {
    public FlippedMinHeapSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        this.setSortPromptID("Flipped Min Heap");
        this.setRunAllID("Flipped Min Heap Sort");
        this.setReportSortID("Flipped Reverse Heapsort");
        this.setCategory("Selection Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    private void siftDown(int[] array, int length, int root, int dist) {
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compare(array[length - leaf], array[length - leaf - 1]) == 1) {
                leaf++;
            }
            if (Reads.compare(array[length - root], array[length - leaf]) == 1) {
                Writes.swap(array, length - root, length - leaf, 1, true, false);
                root = leaf;
            } else break;
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length / 2; i >= 1; i--) {
            siftDown(array, length, i, length);
        }
        for (int i = length; i > 1; i--) {
            Writes.swap(array, length - 1, length - i, 1, true, false);
            siftDown(array, length, 1, i - 1);
        }
    }
}