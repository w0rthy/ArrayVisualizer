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

final public class GravitySort extends Sort {
    public GravitySort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Gravity");
        this.setRunAllID("Gravity Sort");
        //this.setRunAllID("Gravity (Bead) Sort");
        this.setReportSortID("Beadsort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int max = Reads.analyzeMax(array, length, 1, true);
        int[][] abacus = new int[length][max];
        
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < array[i]; j++) {
                Writes.multiDimWrite(abacus, i, abacus[0].length - j - 1, 1, 0, true, true);
            }
        }
        
        //apply gravity
        for(int i = 0; i < abacus[0].length; i++) { 
            for(int j = 0; j < abacus.length; j++) {
                Highlights.markArray(1, j);
                if(abacus[j][i] == 1) {                 
                    //Drop it
                    int dropPos = j;
                    
                    Writes.startLap();
                    while(dropPos + 1 < abacus.length && abacus[dropPos][i] == 1) {   
                        dropPos++;
                    }
                    Writes.stopLap();
                    
                    if(abacus[dropPos][i] == 0) {
                        Writes.multiDimWrite(abacus, j, i, 0, 0, true, true);
                        Writes.multiDimWrite(abacus, dropPos, i, 1, 0, true, true);
                    }
                }
            }
            
            int count = 0;
            for(int x = 0; x < abacus.length; x++){
                count = 0;
                
                Writes.startLap();
                for(int y = 0; y < abacus[0].length; y++) {
                    count += abacus[x][y];
                }
                Writes.stopLap();
                
                Writes.write(array, x, count, 0.001, true, false);
            }
            Highlights.markArray(2, length - i - 1);
        }
    }
}