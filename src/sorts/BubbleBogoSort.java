package sorts;

import templates.BogoSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

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

final public class BubbleBogoSort extends BogoSorting {
    public BubbleBogoSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Bubble Bogo");
        this.setRunAllID("Bubble Bogo Sort");
        this.setReportSortID("Bubble Bogosort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true); //Comparisons ARE used to swap elements
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.isBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        while(!this.bogoIsSorted(array, currentLen)){
            int index1 = (int) (Math.random() * (currentLen - 1));
            
            Highlights.markArray(1, index1);
            
            if(Reads.compare(array[index1], array[index1 + 1]) == 1){
                Writes.swap(array, index1, index1 + 1, 1, true, false);
            }
        }
    }
}