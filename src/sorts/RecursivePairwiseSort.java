package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
MIT License
Copyright (c) 2019 PiotrGrochowski
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

final public class RecursivePairwiseSort extends Sort {
    public RecursivePairwiseSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);

        this.setSortPromptID("Recursive Pairwise");
        this.setRunAllID("Recursive Pairwise Sorting Network");
        this.setReportSortID("Recursive Pairwise Sort");
        this.setCategory("Concurrent Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void pairwiserecursive(int[] array, int start, int end, int gap, double sleep) {
        if (start == end - gap){
            return;
        }
        int b = start + gap;
        while (b < end){
            Delays.sleep(sleep);
            Highlights.markArray(1, b - gap);
            Highlights.markArray(2, b);
            if(Reads.compare(array[b - gap], array[b]) == 1) {
                Writes.swap(array, b - gap, b, sleep, true, false);
            }
            b += (2 * gap);
        }
        if (((end - start) / gap)%2 == 0){
            this.pairwiserecursive(array, start, end, gap * 2, 1);
            this.pairwiserecursive(array, start + gap, end + gap, gap * 2, 1);
        }
        else{
            this.pairwiserecursive(array, start, end + gap, gap * 2, 1);
            this.pairwiserecursive(array, start + gap, end, gap * 2, 1);
        }
        int a = 1;
        while (a < ((end - start) / gap)){
            a = (a * 2) + 1;
        }
        b = start + gap;
        while (b + gap < end){
            int c = a;
            while (c > 1){
                c /= 2;
                if (b + (c * gap) < end){
                    Delays.sleep(sleep);
                    Highlights.markArray(1, b);
                    Highlights.markArray(2, b + (c * gap));
                    if(Reads.compare(array[b], array[b + (c * gap)]) == 1) {
                        Writes.swap(array, b, b + (c * gap), sleep, true, false);
                    }
                }
            }
            b += (2 * gap);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.pairwiserecursive(array, 0, length, 1, 1);
    }
}