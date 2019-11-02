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

final public class SnailSort extends Sort {
    public SnailSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Snail");
        this.setRunAllID("Snail Sort");
        this.setReportSortID("Snailsort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.isBogoSort(false);
    }

    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        while (i < currentLength - 2) {
            int m = i;
            if (Reads.compare(array[i], array[i + 2]) == 1) {
                Writes.swap(array, i, i + 2, 1, true, false);
                i = 0;
            }
            Highlights.markArray(1, m);
            Highlights.markArray(2, m + 2);
            if (i == 0) m = 0;
            if (Reads.compare(array[i], array[i + 1]) == 1) {
                Writes.swap(array, i, i + 1, 1, true, false);
                i = 0;
            }
            Highlights.markArray(1, m);
            Highlights.markArray(2, m + 1);
            i++;
        }
    }
}
