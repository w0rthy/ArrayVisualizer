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

final public class PopSort2 extends Sort {
    public PopSort2(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Pop2");
        this.setRunAllID("Pop Sort 2");
        this.setReportSortID("Pop Sort 2");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private int compare(int[] array, int start, int end, double sleep, int dir) {
int flag = 0;
                if((dir != 0) ? (Reads.compare(array[start], array[end]) == 1) : (Reads.compare(array[start], array[end]) == -1)) {
                    Writes.swap(array, start, end, sleep, true, false);
flag = 1;
                }
                
                Highlights.markArray(1, start);
                Highlights.markArray(2, end);
                
                Delays.sleep(sleep);
return flag;
    }

    private int bubblepass(int[] array, int start, int end, double sleep, int dir) {
    int lastswap = start; for(int i=start+1; i<end; i++) if((compare(array, 
i-1, i, sleep, dir)!=0) ? true : false) lastswap = i;
return lastswap;
    }

    private void bubblesort(int[] array, int start, int end, double sleep, int dir) {
	int lastswap = end;
	while(lastswap > start+1){
            lastswap = bubblepass(array, start, lastswap, sleep, dir);
        }
    }

    private void fourbubblesort(int[] array, int start1, int end1, int start2, int end2, int start3, int end3, int start4, int end4, double sleep, int dir1, int dir2, int dir3, int dir4) {
	int lastswap1 = end1;
	int lastswap2 = end2;
	int lastswap3 = end3;
	int lastswap4 = end4;
	while(lastswap1 > start1+1 || lastswap2 > start2+1 || lastswap3 > start3+1 || lastswap4 > start4+1){
            lastswap1 = bubblepass(array, start1, lastswap1, sleep, dir1);
            lastswap2 = bubblepass(array, start2, lastswap2, sleep, dir2);
            lastswap3 = bubblepass(array, start3, lastswap3, sleep, dir3);
            lastswap4 = bubblepass(array, start4, lastswap4, sleep, dir4);
        }
    }

    private void twobubblesort(int[] array, int start1, int end1, int start2, int end2, double sleep, int dir1, int dir2) {
	int lastswap1 = end1;
	int lastswap2 = end2;
	while(lastswap1 > start1+1 || lastswap2 > start2+1){
            lastswap1 = bubblepass(array, start1, lastswap1, sleep, dir1);
            lastswap2 = bubblepass(array, start2, lastswap2, sleep, dir2);
        }
    }

    private void popsort2(int[] array, int start, int end, double sleep) {
	fourbubblesort(array, start, start+((end-start)/4), start+((end-start)/4), start+((end-start)/2), start+((end-start)/2), start+(3*(end-start)/4), start+(3*(end-start)/4), end, sleep, 0, 1, 0, 1);
	twobubblesort(array, start, start+((end-start)/2), start+((end-start)/2), end, sleep, 0, 1);
	bubblesort(array, start, end, sleep, 1);
    }
    
    public void customSort(int[] array, int start, int end) {
        this.popsort2(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.popsort2(array, 0, length, 0.0875);
    }
}