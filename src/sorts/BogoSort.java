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

final public class BogoSort extends Sort {
    public BogoSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Bogo");
        this.setRunAllID("Bogo Sort");
        this.setReportSortID("Bogo Sort");
        this.setCategory("Impractical Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.isBogoSort(true);
    }

    private void shuffle(int[] array, int start, int end, double sleep, int dir){ 
        for(int i=end; i>1; i--){
            Writes.swap(array, (int)(Math.random()*i), i-1, sleep, true, false);
        }
    }

    private int checkifsorted(int[] array, int start, int end, double sleep, int dir) {
        int flag = 0;
        for(int i=start+1; i<end; i++)
if((dir != 0) ? (Reads.compare(array[i-1], array[i]) == 1) : (Reads.compare(array[i-1], array[i]) == -1)) flag=1;
        return flag;
    }

    private void bogosort(int[] array, int start, int end, double sleep) {
        while(checkifsorted(array, start, end, sleep, 1) != 0) shuffle(array, start, end, sleep, 1);
    }
    
    public void customSort(int[] array, int start, int end) {
        this.bogosort(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.bogosort(array, 0, length, 0.0875);
    }
}