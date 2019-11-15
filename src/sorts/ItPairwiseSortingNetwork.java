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

final public class ItPairwiseSortingNetwork extends Sort {
    public ItPairwiseSortingNetwork(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Iter. Pairwise");
        this.setRunAllID("Iterative Pairwise Sorting Network");
        this.setReportSortID("Iterative Pairwise Sorting Network");
        this.setCategory("Concurrent Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

//                if(Reads.compare(array[j], array[j + 1]) == 1) {
//                    Writes.swap(array, j, j + 1, sleep, true, false);

    private void iterativepairwise(int[] array, int start, int end, double sleep) {
        int a = 1;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        while (a < end){
            b = start+a;
            c = 0;
            while (b < end){
                Delays.sleep(sleep);
                Highlights.markArray(1, b - a);
                Highlights.markArray(2, b);
                if(Reads.compare(array[b - a], array[b]) == 1) {
                    Writes.swap(array, b - a, b, sleep, true, false);
                }
                c = (c + 1) % a;
                b++;
                if (c == 0){
                    b += a;
                }
            }
            a *= 2;
        }
        a /= 4;
        e = 1;
        while (a > 0){
            d = e;
            while (d > 0){
                b = start+((d + 1) * a);
                c = 0;
                while (b < end){
                    Delays.sleep(sleep);
                    Highlights.markArray(1, b - (d * a));
                    Highlights.markArray(2, b);
                    if(Reads.compare(array[b - (d * a)], array[b]) == 1) {
                        Writes.swap(array, b - (d * a), b, sleep, true, false);
                    }
                    c = (c + 1) % a;
                    b++;
                    if (c == 0){
                        b += a;
                    }
                }
                d /= 2;
            }
            a /= 2;
            e = (e * 2) + 1;
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.iterativepairwise(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.iterativepairwise(array, 0, length, 1);
    }
}