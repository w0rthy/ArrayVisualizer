package sorts;

import templates.CircleSorting;
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

final public class IntroCircleSort extends CircleSorting {
    public IntroCircleSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Intro Circle");
        this.setRunAllID("Introspective Circle Sort");
        this.setReportSortID("Introspective Circlesort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int iterations = 0;
        int threshold = (int) (Math.log(length) / Math.log(2)) / 2;
        
        do {
            iterations++;
            
            if(iterations >= threshold) {
                BinaryInsertionSort binaryInserter = new BinaryInsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
                binaryInserter.customBinaryInsert(array, 0, length, 0.1);
                break;
            }
        } while (this.circleSortRoutine(array, 0, length - 1, 0) != 0);
    }
}