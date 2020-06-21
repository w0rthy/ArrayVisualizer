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

final public class AdBinaryInsert extends Sort {
    public AdBinaryInsert(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("A.Binary Insertion");
        this.setRunAllID("Adaptive Binary Insertion Sort");
        this.setReportSortID("Adaptive Binary Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    protected void abinaryinsert(int[] array, int start, int end, double sleep) {
        int count = 0;
        for (int i = start + 1; i < end; i++) {
            int num = array[i];
            int v = (2*count / (i - start)) + 1; //I'VE SOLVED IT!!
            int lo = Math.max(i - v, start), hi = i;
            while ((lo >= start) && (Reads.compare(array[lo], num) == 1)){
                lo -= v;
                hi -= v;
            }
            lo++;
            if (lo < start){
                lo = start;
            }
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2); // avoid int overflow!
                Highlights.markArray(2, mid);
                
                Delays.sleep(sleep);
                
                if (Reads.compare(num, array[mid]) < 0) { // do NOT move equal elements to right of inserted element; this maintains stability!
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo
            count += (i - lo);

            int j = i - 1;
            
            if (j >= lo){
                while (j >= lo)
                {
                    Writes.write(array, j + 1, array[j], sleep, true, false);
                    j--;
                }
                Writes.write(array, lo, num, sleep, true, false);
            }
            
            Highlights.clearAllMarks();
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.abinaryinsert(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.abinaryinsert(array, 0, length, 0.0875);
    }
}