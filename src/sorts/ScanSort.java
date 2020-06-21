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

final public class ScanSort extends Sort {
    public ScanSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Scan");
        this.setRunAllID("Scan Sort");
        this.setReportSortID("Scan Sort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void scansort(int[] array, int start, int end, double sleep) {
        int i = start;
        for (int a = 1; a <= ((end - start) * 6); a++){
            for (int b = 0; b < (i + ((end - start) * 5)); b++){
                int j = (int)(Math.random()*(end - start - 1))+(start + 1);
                if (i < j){
                    Highlights.markArray(1, i);
                    Highlights.markArray(2, j);
                    Delays.sleep(sleep);
                    if(Reads.compare(array[i], array[j]) == 1) {
                        Writes.swap(array, i, j, sleep, true, false);
                    }
                }
                if (j < i){
                    Highlights.markArray(1, j);
                    Highlights.markArray(2, i);
                    Delays.sleep(sleep);
                    if(Reads.compare(array[j], array[i]) == 1) {
                        Writes.swap(array, j, i, sleep, true, false);
                    }
                }
                j = (int)(Math.random()*(end - i - 1))+(i + 1);
            }
            i++;
            if (i == (end - 1)){
                i = start;
            }
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.scansort(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.scansort(array, 0, length, 0.0875);
    }
}