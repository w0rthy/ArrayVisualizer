package sorts;

import java.util.ArrayList;

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

final public class LSDRadixSort extends Sort {
    public LSDRadixSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("LSD Radix");
        this.setRunAllID("Least Significant Digit Radix Sort, Base 4");
        this.setReportSortID("Least Significant Digit Radixsort");
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
        this.setRunAllID("Least Significant Digit Radix Sort, Base " + bucketCount);
        
        int highestpower = Reads.analyzeMaxLog(array, length, bucketCount, 0.5, true);
        
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] registers = new ArrayList[bucketCount];
        
        for(int i = 0; i < bucketCount; i++)
            registers[i] = new ArrayList<>();
        
        for(int p = 0; p <= highestpower; p++){
            for(int i = 0; i < length; i++){
                Highlights.markArray(1, i);
                
                int digit = Reads.getDigit(array[i], p, bucketCount);
                registers[digit].add(array[i]);
                
                Writes.mockWrite(length, digit, array[i], 1);
            }

            Writes.fancyTranscribe(array, length, registers, bucketCount * 0.8);
        }
    }
}