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

final public class MOMquick extends Sort {
    public MOMquick(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("M.of.M. quicksort");
        this.setRunAllID("Median Of Medians Quick Sort");
        this.setReportSortID("Median Of Medians Quick Sort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void MOMquick(int[] array, int start, int end, double sleep) {
        int q = 1;
        while (q <= (end - start)){
            q *= 2;
        }
        while (q > 2){
            q /= 2;
            int i = start;
            while ((i+q-1) < end){
                select(array, i, Math.min(end, i+(2*q)-1), (i+q-1), sleep);
                i += (q*2);
            }
        }
    }

    private void select(int[] array, int start, int end, int split, double sleep) {
        Highlights.clearAllMarks();
        while ((end - start) > 1){
            int w = ((end - start)+4)/5;
            int j = w + start;
            while (j < end){
                int k = array[j];
                int l = j - w;
                int flag = 0;
                while ((flag == 0) && (l >= start)){
                    Highlights.markArray(1, l);
                    Highlights.markArray(2, l + w);
                    Delays.sleep(sleep);
                    if (Reads.compare(array[l], k) == 1){
                        Writes.write(array, l+w, array[l], sleep, false, false);
                        l -= w;
                    }
                    else{
                        flag = 1;
                    }
                }
                Writes.write(array, l+w, k, sleep, false, false);
                j++;
            }
            if (end - start <= 5){
                Highlights.clearAllMarks();
                return;
            }
            select(array, start+(2*w), start+(3*w), start+(2*w+(w/2)), sleep);
            int x = array[start+(2*w+(w/2))];
            Writes.swap(array, start+(2*w+(w/2)), end-1, sleep, true, false);
            int p = start;
            int r = end - 2;
            int i = p;
            int e = r;
        
            while (i <= e) {
                while (Reads.compare(array[i], x) == -1){
                    i++;
                    Highlights.markArray(1, i);
                    Delays.sleep(sleep);
                }
                while (Reads.compare(array[e], x) == 1){
                    e--;
                    Highlights.markArray(2, e);
                    Delays.sleep(sleep);
                }

                if (i <= e) {
                    Writes.swap(array, i, e, sleep, true, false);
                
                    i++;
                    e--;
                }
            }
            Writes.swap(array, i, end-1, sleep, true, false);
            if (i == split){
                end = start;
            }
            else{
                if (i < split){
                    start = i+1;
                }
                else{
                    if (i > split){
                        end = i;
                    }
                }
            }
        }
        Highlights.clearAllMarks();
    }
    
    public void customSort(int[] array, int start, int end) {
        this.MOMquick(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.MOMquick(array, 0, length, 1);
    }
}