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

final public class InPlaceLSDRadixSort extends Sort {
    private int buckets = 2; // default choice
    
    public InPlaceLSDRadixSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("In-Place LSD Radix");
        this.setRunAllID("In-Place LSD Radix Sort, Base " + buckets);
        this.setReportSortID("In-Place LSD Radix Sort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false);
        this.isBucketSort(true);
        this.isRadixSort(true);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.buckets = bucketCount;
        
        int pos = 0;
        int[] vregs = new int[bucketCount-1];
        
        int maxpower = Reads.analyzeMaxLog(array, length, bucketCount, 0.25, true);
        
        for(int p = 0; p <= maxpower; p++){
            for(int i = 0; i < vregs.length; i++) {
                Writes.write(vregs, i, length - 1, 0, false, true);
            }
            
            pos = 0;
            
            for(int i = 0; i < length; i++){
                int digit = Reads.getDigit(array[pos], p, bucketCount);
                
                if(digit == 0) {
                    pos++;
                    Highlights.markArray(0, pos);
                } 
                else {
                    for(int j = 0; j < vregs.length;j++)
                        Highlights.markArray(j + 1, vregs[j]);
                    
                    Writes.multiSwap(array, pos, vregs[digit - 1], 0.00075, false, false);
                    
                    for(int j = digit - 1; j > 0; j--) {
                        Writes.write(vregs, j - 1, vregs[j - 1] - 1, 0, false, true);
                    }
                }
            }
        }
    }
}