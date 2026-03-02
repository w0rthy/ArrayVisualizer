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

final public class InMerge2 extends Sort {
    public InMerge2(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("In Place Merge 2");
        this.setRunAllID("In Place Merge Sort 2");
        this.setReportSortID("In Place Merge Sort 2");
        this.setCategory("Merge Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void inmerge2(int[] array, int start, int end, double sleep) {
        int q = 1;
        while (q < (end - start)){
            int w = start;
            while (w < end){
                this.inplacemerge(array, w, Math.min(end, w+(2*q)), Math.min(end, w+q), sleep);
                w += (2*q);
            }
            q *= 2;
        }
    }

    private void inplacemerge(int[] array, int start, int end, int split, double sleep) {
        if ((split == end) || (split == start)){
            return;
        }
        int a = 0;
        int b = Math.min((end - split), (split-start));
        while (b > a){
            Delays.sleep(sleep);
            Highlights.markArray(1, split-((a+b)/2)-1);
            Highlights.markArray(2, split+((a+b)/2));
            if(Reads.compare(array[split-((a+b)/2)-1], array[split+((a+b)/2)]) == 1) {
                a = ((a+b)/2)+1;
            }
            else {
                b = ((a+b)/2);
            }
        }
        b = split - a;
        while (b < split){
            Writes.swap(array, b, b+a, sleep, true, false);
            b++;
        }
        if ((end - split)<(split-start)){
            this.inplacemerge(array, split, end, split+a, sleep);
            this.inplacemerge(array, start, split, split-a, sleep);
        }
        else{
            this.inplacemerge(array, start, split, split-a, sleep);
            this.inplacemerge(array, split, end, split+a, sleep);
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.inmerge2(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.inmerge2(array, 0, length, 1);
    }
}