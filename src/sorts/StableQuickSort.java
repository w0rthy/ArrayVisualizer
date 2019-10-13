package sorts;

import java.util.ArrayList;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
MIT License

Copyright (c) 2017 Rodney Shaghoulian

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class StableQuickSort extends Sort {
    public StableQuickSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Stable Quick");
        this.setRunAllID("Stable Quick Sort");
        this.setReportSortID("Stable Quicksort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    // Author: Rodney Shaghoulian
    // Github: github.com/RodneyShag

    private void copy(ArrayList<Integer> list, int [] array, int startIndex) {
        for (int num : list) {
            Writes.write(array, startIndex++, num, 0.25, false, false);
            Highlights.markArray(1, startIndex);
        }
    }
    
    /* Partition/Quicksort "Stable Sort" version using O(n) space */
    private int stablePartition(int[] array, int start, int end) {
        int pivotValue = array[start]; //poor pivot choice
        Highlights.markArray(3, start);
        
        ArrayList<Integer> leftList  = new ArrayList<>();
        ArrayList<Integer> rightList = new ArrayList<>();

        for (int i = start + 1 ; i <= end; i++) {
            Highlights.markArray(1, i);
            
            if (Reads.compare(array[i], pivotValue) == -1) {
                Writes.mockWrite(end - start, leftList.size(), array[i], 0.25);
                leftList.add(array[i]);
            } 
            else {
                Writes.mockWrite(end - start, rightList.size(), array[i], 0.25);
                rightList.add(array[i]);
            }
        }

        /* Recreate array */
        this.copy(leftList, array, start);
        
        int newPivotIndex = start + leftList.size();
        
        Writes.write(array, newPivotIndex, pivotValue, 0.25, false, false);
        Highlights.markArray(1, newPivotIndex);
        
        this.copy(rightList, array, newPivotIndex + 1);

        return newPivotIndex;
    }

    private void stableQuickSort(int [] array, int start, int end) {
        if (start < end) {
            int pivotIndex = this.stablePartition(array, start, end);
            this.stableQuickSort(array, start, pivotIndex - 1);
            this.stableQuickSort(array, pivotIndex + 1, end);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.stableQuickSort(array, 0, length - 1);
    }
}