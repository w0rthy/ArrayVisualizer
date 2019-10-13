package sorts;

import templates.Sort;
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

final public class WeaveMergeSort extends Sort {
    public WeaveMergeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Weave Merge");
        this.setRunAllID("Weave Merge Sort");
        this.setReportSortID("Weave Mergesort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void weaveInsert(int[] arr, int start, int end) {
        int pos;
        
        for(int j = start; j < end; j++){
            pos = j;
            
            Highlights.markArray(1, j);
            Highlights.clearMark(2);
            
            while(pos > start && Reads.compare(arr[pos], arr[pos - 1]) < 1) {
                Writes.swap(arr, pos, pos - 1, 0.2, true, false);
                pos--;
            }
        }
    }
	
	private void weaveMerge(int[] arr, int min, int max, int mid) {
	    int i = 1;
	    int target = (mid - min);
	    
	    while(i <= target) {
	        Writes.multiSwap(arr, mid + i, min + (i * 2) - 1, 0.05, true, false);
	        i++;
	    }
	    
	    this.weaveInsert(arr, min, max + 1);
	}

	private void weaveMergeSort(int[] array, int min, int max) {
		if(max - min == 0) {      //only one element.
			Delays.sleep(1);      //no swap
		}
		else if(max - min == 1) { //only two elements and swaps them
			if(Reads.compare(array[min], array[max]) == 1) {
				Writes.swap(array, min, max, 0.01, true, false);
			}
		}
		else {
			int mid = (int) Math.floor((min + max) / 2); //The midpoint

			this.weaveMergeSort(array, min, mid);     //sort the left side
			this.weaveMergeSort(array, mid + 1, max); //sort the right side
			this.weaveMerge(array, min, max, mid);    //combines them
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.weaveMergeSort(array, 0, currentLength - 1);
    }
}