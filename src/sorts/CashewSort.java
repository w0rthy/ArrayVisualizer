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

final public class CashewSort extends Sort {
    public CashewSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Cashew");
        this.setRunAllID("Cashew Sort");
        this.setReportSortID("Cashew Sort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void cashewsort(int[] array, int start, int end, double sleep) {
        int i = start;
        while(i < ((end / 4) + start)) {
            for(int j = i; j < end + start - i - 1; j++) {
                if(Reads.compare(array[j], array[j + 1]) == 1) {
                    Writes.swap(array, j, j + 1, sleep, true, false);
                }
                
                Highlights.markArray(1, j);
                Highlights.markArray(2, j + 1);
                
                Delays.sleep(0.01);
            }
            for(int j = i; j < end + start - i - 1; j++) {
                if(Reads.compare(array[j], array[j + 1]) == 1) {
                    Writes.swap(array, j, j + 1, sleep, true, false);
                }
                
                Highlights.markArray(1, j);
                Highlights.markArray(2, j + 1);
                
                Delays.sleep(0.01);
            }
            for(int j = end + start - i - 1; j > i; j--){
                if(Reads.compare(array[j], array[j - 1]) == -1) {
                    Writes.swap(array, j, j - 1, sleep, true, false);
                }
                
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - 1);
                
                Delays.sleep(0.01);
            }
            for(int j = end + start - i - 1; j > i; j--){
                if(Reads.compare(array[j], array[j - 1]) == -1) {
                    Writes.swap(array, j, j - 1, sleep, true, false);
                }
                
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - 1);
                
                Delays.sleep(0.01);
            }
            
            i++;
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.cashewsort(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.cashewsort(array, 0, length, 0.0875);
    }
}