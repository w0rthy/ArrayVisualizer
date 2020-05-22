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

final public class RecursiveCombSort extends Sort {
    public RecursiveCombSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Recursive Comb");
        this.setRunAllID("Recursive Comb Sort");
        this.setReportSortID("Recursive Combsort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void recursivecomb(int[] array, int start, int end, int gap, double sleep) {
        if (start == (end - gap)){
            return;
        }
        if (((end - start) / gap) % 2 == 0){
            this.recursivecomb(array, start, end, gap * 2, sleep);
            this.recursivecomb(array, start + gap, end + gap, gap * 2, sleep);
        }
        else{
            this.recursivecomb(array, start, end + gap, gap * 2, sleep);
            this.recursivecomb(array, start + gap, end, gap * 2, sleep);
        }
        this.finishrecucomb(array, start, end, gap, sleep);
    }

    private void finishrecucomb(int[] array, int start, int end, int gap, double sleep) {
        if (start >= (end - gap)){
            return;
        }
        if (((end - start) / gap) % 3 == 2){
            this.finishrecucomb(array, start, end + gap, gap * 3, sleep);
            this.finishrecucomb(array, start + gap, end + gap + gap, gap * 3, sleep);
            this.finishrecucomb(array, start + gap + gap, end, gap * 3, sleep);
        }
        else{
        if (((end - start) / gap) % 3 == 1){
            this.finishrecucomb(array, start, end + gap + gap, gap * 3, sleep);
            this.finishrecucomb(array, start + gap, end, gap * 3, sleep);
            this.finishrecucomb(array, start + gap + gap, end + gap, gap * 3, sleep);
        }
        else{
            this.finishrecucomb(array, start, end, gap * 3, sleep);
            this.finishrecucomb(array, start + gap, end + gap, gap * 3, sleep);
            this.finishrecucomb(array, start + gap + gap, end + gap + gap, gap * 3, sleep);
        }
        }
        for (int i = start; i < (end - gap); i += gap){
            Delays.sleep(sleep);
            Highlights.markArray(1, i);
            Highlights.markArray(2, i + gap);
            if(Reads.compare(array[i], array[i + gap]) == 1) {
                Writes.swap(array, i, i + gap, sleep, true, false);
            }
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.recursivecomb(array, 0, length, 1, 1);
    }
}