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

final public class MSDRadixSort extends Sort {
    public MSDRadixSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("MSD Radix");
        this.setRunAllID("Most Significant Digit Radix Sort");
        this.setReportSortID("Most Significant Digit Radixsort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false);
        this.isBucketSort(true);
        this.isRadixSort(true);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private void radixMSD(int[] array, int length, int min, int max, int radix, int pow) {
        if(min >= max || pow < 0)
            return;
        
        Highlights.markArray(2, max - 1);
        Highlights.markArray(3, min);
        
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] registers = new ArrayList[radix];
        
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<>();
        
        for(int i = min; i < max; i++) {
            Highlights.markArray(1, i);
            
            int digit = Reads.getDigit(array[i], pow, radix);
            registers[digit].add(array[i]);
            
            Writes.mockWrite(length, digit, array[i], 0.5);
        }
        
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        
        double tempSleep = Delays.getSleepRatio();

        if(!Delays.skipped()) {    
            Delays.setSleepRatio((Math.log(length) / Math.log(2)) / 6);
        }

        Writes.transcribeMSD(array, registers, 0, min, true, false);

        if(!Delays.skipped()) {
            Delays.setSleepRatio(tempSleep);
        }
        
        int sum = 0;
        for(int i = 0; i < registers.length; i++) {
            this.radixMSD(array, length, sum + min, sum + min + registers[i].size(), radix, pow-1);
            
            sum += registers[i].size();
            registers[i].clear();
            Writes.changeTempWrites(registers[i].size());
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int highestpower = Reads.analyzeMaxLog(array, length, bucketCount, 0.25, true);
        
        radixMSD(array, length, 0, length, bucketCount, highestpower);
    }
}