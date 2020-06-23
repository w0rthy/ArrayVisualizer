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

final public class progressivesort2 extends Sort {
    public progressivesort2(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("progressive2");
        this.setRunAllID("progressive sort 2");
        this.setReportSortID("progressive sort 2");
        this.setCategory("Insertion Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private int compare(int[] array, int start, int end, double sleep, int dir) {
int flag = 0;
                if((dir != 0) ? (Reads.compare(array[start], array[end]) == 1) : (Reads.compare(array[start], array[end]) == -1)) {
                    Writes.swap(array, start, end, sleep, true, false);
flag = 1;
                }
                
                Highlights.markArray(1, start);
                Highlights.markArray(2, end);
                
                Delays.sleep(sleep);
return flag;
    }

    private void progressivesort2(int[] array, int start, int end, double sleep) {
	int q = 1;
        int flag = 1;
        while(flag!=0){
            flag = 0;
            int i = q+start;
            if(q != 1) flag = 1;
            while(i<end){
                int j = i;
                while(j-q >= start && compare(array, j-q, j, sleep, 1)!=0){
                    flag=1;
                    j -= q;
                    q++;
                }
                q=1;
                i++;
            }
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        this.progressivesort2(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.progressivesort2(array, 0, length, 1);
    }
}