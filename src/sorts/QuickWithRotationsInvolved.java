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

final public class QuickWithRotationsInvolved extends Sort {
    public QuickWithRotationsInvolved(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Rotate Quick");
        this.setRunAllID("Stable Quick Sort With Rotations");
        this.setReportSortID("Stable Quick Sort With Rotations");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void rotatequick(int[] array, int start, int end, int sign, double sleep) {
        if ((end - start) <= 1){
            return;
        }
        int testing = 0;
        int i = start;
        while ((i < (end-1)) && (testing == 0)){
            Delays.sleep(sleep);
            Highlights.markArray(1, i);
            Highlights.markArray(2, i+1);
            if (Reads.compare(array[i], array[i+1]) != 0){
                testing = 1;
            }
            i++;
        }
        if (testing == 0){
            return;
        }
        int[] STACK2 = new int[(int)(Math.ceil(Math.log(end-start)/Math.log(2)))+2];
        int[] STACK3 = new int[(int)(Math.ceil(Math.log(end-start)/Math.log(2)))+2];
        int pivot;
        Delays.sleep(sleep);
        Highlights.markArray(1, start);
        Highlights.markArray(2, start+((end-start)/2));
        if ((Reads.compare(array[start], array[start+((end-start)/2)])) == 1){
            Delays.sleep(sleep);
            Highlights.markArray(1, start+((end-start)/2));
            Highlights.markArray(2, end-1);
            if ((Reads.compare(array[start+((end-start)/2)], array[end-1])) == 1){
                pivot = array[start+((end-start)/2)];
            }
            else{
                Delays.sleep(sleep);
                Highlights.markArray(1, start);
                Highlights.markArray(2, end-1);
                if (Reads.compare(array[start], array[end-1]) == 1){
                    pivot = array[end-1];
                }
                else{
                    pivot = array[start];
                }
            }
        }
        else{
            Delays.sleep(sleep);
            Highlights.markArray(1, start+((end-start)/2));
            Highlights.markArray(2, end-1);
            if (Reads.compare(array[start+((end-start)/2)], array[end-1]) == 1){
                Delays.sleep(sleep);
                Highlights.markArray(1, start);
                Highlights.markArray(2, end-1);
                if (Reads.compare(array[start], array[end-1]) == 1){
                    pivot = array[start];
                }
                else{
                    pivot = array[end-1];
                }
            }
            else{
                pivot = array[start+((end-start)/2)];
            }
        }
        Highlights.clearAllMarks();
        int q = sign;
        int b = start;
        int r = 0;
        int e = 0;
        STACK2[0]=start;
        while (b < end){
            b++;
            r++;
            e++;
            STACK2[r-1]=b-1;
            Delays.sleep(sleep);
            Highlights.markArray(1, b-1);
            if ((q+(Reads.compare(array[b-1], pivot)))>=1){
                STACK3[r-1]=b-1;
            }
            else{
                STACK3[r-1]=b;
            }
            STACK2[r]=b;
            for (int u = 0; u < Integer.numberOfTrailingZeros(e); u++){
                rotate(array, STACK3[r-2], STACK3[r-1], STACK2[r-1], sleep);
                STACK3[r-2]+=(STACK3[r-1]-STACK2[r-1]);
                STACK2[r-1]=STACK2[r];
                r--;
            }
        }
        while (r > 1){
            rotate(array, STACK3[r-2], STACK3[r-1], STACK2[r-1], sleep);
            STACK3[r-2]+=(STACK3[r-1]-STACK2[r-1]);
            STACK2[r-1]=STACK2[r];
            r--;
        }
        int a = STACK3[0];
        rotatequick(array, start, a, 1-sign, sleep);
        rotatequick(array, a, end, 1-sign, sleep);
    }

    private void rotate(int[] array, int start, int end, int split, double sleep) {
        int temp;
        while ((split < end) && (split > start)){
            if ((end-split)<(split-start)){
                for (int i = split; i < end; i++){
                    Writes.swap(array, i-(end-split), i, sleep, true, false);
                }
                temp = end;
                end = split;
                split = (split-(temp-split));
            }
            else{
                for (int i = start; i < split; i++){
                    Writes.swap(array, i, i+(split-start), sleep, true, false);
                }
                temp = start;
                start = split;
                split = (split+(split-temp));
            }
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.rotatequick(array, start, end, 0, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.rotatequick(array, 0, length, 0, 1);
    }
}