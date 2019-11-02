package templates;

import sorts.BinaryInsertionSort;
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

public abstract class MergeSorting extends Sort {
    private BinaryInsertionSort binaryInserter;
    
    protected MergeSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    private void merge(int[] array, int start, int mid, int end, boolean binary) {
        if(start == mid) return;
        
        merge(array, start, (mid+start)/2, mid, binary);
        merge(array, mid, (mid+end)/2, end, binary);

        if(end - start < 32 && binary) {
            return;
        }
        else if(end - start == 32 && binary) {
            binaryInserter.customBinaryInsert(array, start, end, 0.25);
        }
        else {
            int[] tmp = new int[end - start];
            
            int low = start;
            int high = mid;
            
            for(int nxt = 0; nxt < tmp.length; nxt++){
                if(low >= mid && high >= end) break;
                
                if(low < mid && high >= end){
                    Writes.write(tmp, nxt, array[low++], 0, false, true);
                }
                else if(low >= mid && high < end){
                    Writes.write(tmp, nxt, array[high++], 0, false, true);
                }
                else if(Reads.compare(array[low], array[high]) == -1){
                    Writes.write(tmp, nxt, array[low++], 0, false, true);
                }
                else{
                    Writes.write(tmp, nxt, array[high++], 0, false, true);
                }
                
                Highlights.markArray(1, low);
                Highlights.markArray(2, high);
                
                Delays.sleep(1);
            }
            
            Highlights.clearMark(2);
            
            for(int i = 0; i < tmp.length; i++){
                Writes.write(array, start + i, tmp[i], 1, false, false);
                Highlights.markArray(1, start + i);
            }
        }
    }
    
    protected void mergeSort(int[] array, int length, boolean binary) {
        binaryInserter = new BinaryInsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        
        if(length < 32 && binary) {
            binaryInserter.customBinaryInsert(array, 0, length, 0.25);
            return;
        }
        
        int start = 0;
        int end = length;
        int mid = start + ((end - start) / 2);
        
        merge(array, start, mid, end, binary);
    }

    protected void merge2(int[] array, int[] merge, int min1, int max1, int min2, int max2, double writeSleep, double compSleep) {
      if ((min2 - min1) <= (max2 - max1)){
        for (int g = 0; g < (min2 - min1); g++){
        Highlights.markArray(2, max1 - g);
        Writes.write(merge, g, array[max1 - g], writeSleep, false, true);
        }
        int point1 = min2;
        int point2 = (min2 - min1) - 1;
        int point3 = min1;
        while ((point1 <= max2) && (point2 >= 0)){
            Highlights.markArray(2, point1);
            Delays.sleep(compSleep);
            if (Reads.compare(array[point1], merge[point2]) < 0){
                Writes.write(array, point3, array[point1], writeSleep, true, false);
                point3++;
                point1++;
            }
            else{
                Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3++;
            }
        }
        while ((point2 >= 0)){
            Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3++;
        }
        Highlights.clearAllMarks();
      }
      else {
        for (int g = 0; g < (max2 - max1); g++){
        Highlights.markArray(2, min2 + g);
        Writes.write(merge, g, array[min2 + g], writeSleep, false, true);
        }
        int point1 = max1;
        int point2 = (max2 - max1) - 1;
        int point3 = max2;
        while ((point1 >= min1) && (point2 >= 0)){
            Highlights.markArray(2, point1);
            Delays.sleep(compSleep);
            if (Reads.compare(array[point1], merge[point2]) > 0){
                Writes.write(array, point3, array[point1], writeSleep, true, false);
                point3--;
                point1--;
            }
            else{
                Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3--;
            }
        }
        while ((point2 >= 0)){
            Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3--;
        }
        Highlights.clearAllMarks();
      }
    }

    protected void healymerge2(int[] array, int[] merge, int min1, int max1, int min2, int max2, double writeSleep, double compSleep, int parameter4) {
      Highlights.clearAllMarks();
      int q = max1;
        for (int o = 1; o < parameter4; o++){
          if ((max1 - o) >= min1){
            Highlights.markArray(1, max1 - o);
            Highlights.markArray(2, max1);
            Delays.sleep(compSleep);
            if (Reads.compare(array[max1 - o], array[max1]) < 0){
              q = max1 - o;
            }
          } 
        }
      int p = min2;
        for (int o = 1; o < parameter4; o++){
          if ((min2 + o) <= max2){
            Highlights.markArray(1, min2 + o);
            Highlights.markArray(2, min2);
            Delays.sleep(compSleep);
            if (Reads.compare(array[min2 + o], array[min2]) > 0){
              p = min2 + o;
            }
          }
        }
      Highlights.markArray(1, q);
      Highlights.markArray(2, p);
      Delays.sleep(compSleep);
      if (Reads.compare(array[q], array[p]) <= 0){
        return;
      }
      Highlights.clearAllMarks();
      if ((min2 - min1) <= (max2 - max1)){
        for (int g = 0; g < (min2 - min1); g++){
        Highlights.markArray(2, max1 - g);
        Writes.write(merge, g, array[max1 - g], writeSleep, false, true);
        }
        int point1 = min2;
        int point2 = (min2 - min1) - 1;
        int point3 = min1;
        while ((point1 <= max2) && (point2 >= 0)){
            Highlights.markArray(2, point1);
            Delays.sleep(compSleep);
            if (Reads.compare(array[point1], merge[point2]) < 0){
                Writes.write(array, point3, array[point1], writeSleep, true, false);
                point3++;
                point1++;
            }
            else{
                Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3++;
            }
        }
        while ((point2 >= 0)){
            Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3++;
        }
        Highlights.clearAllMarks();
      }
      else {
        for (int g = 0; g < (max2 - max1); g++){
        Highlights.markArray(2, min2 + g);
        Writes.write(merge, g, array[min2 + g], writeSleep, false, true);
        }
        int point1 = max1;
        int point2 = (max2 - max1) - 1;
        int point3 = max2;
        while ((point1 >= min1) && (point2 >= 0)){
            Highlights.markArray(2, point1);
            Delays.sleep(compSleep);
            if (Reads.compare(array[point1], merge[point2]) > 0){
                Writes.write(array, point3, array[point1], writeSleep, true, false);
                point3--;
                point1--;
            }
            else{
                Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3--;
            }
        }
        while ((point2 >= 0)){
            Writes.write(array, point3, merge[point2], writeSleep, true, false);
                point2--;
                point3--;
        }
        Highlights.clearAllMarks();
      }
    }

    protected void merge2Sort(int[] array, int length, int limit, double compSleep, double writeSleep) {
        int sequence = 0;
        int[] stack = new int[(int) Math.floor(Math.log(length+0.0)/Math.log(2.0)+2.0)];
        int stackend = 0;
        int[] merge = new int[length/2];
        int count = 0;
        int i = 0;
        int start = 0;
        int test = start;
        int start2 = start;
        int end = length;
        int flag = 0;
        while (test < (end - 1)) {
        count = 0;
        i = test + 1;
        start2 = test;
        flag = 0;
        while (i < end && flag == 0) {
            int num = array[i];
            int lo = start2, hi = i;
            
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2); // avoid int overflow!
                Highlights.markArray(2, mid);
                
                Delays.sleep(compSleep);
                
                if (Reads.compare(num, array[mid]) < 0) { // do NOT move equal elements to right of inserted element; this maintains stability!
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo
            count += (i - lo);
            if (count > limit){
            flag = 1;
            }
            else {
            if (i > lo){
            int j = i - 1;
            
            while (j >= lo)
            {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            Writes.write(array, lo, num, writeSleep, true, false);
            
            Highlights.clearAllMarks();
            }
            i++;
            }
            test = i;
        }
        sequence++;
        stackend++;
        Writes.write(stack, stackend, test, 0, false, true);
        for (int r = 0; r < Integer.numberOfTrailingZeros(sequence); r++){
            merge2(array, merge, stack[stackend - 2], stack[stackend - 1] - 1, stack[stackend - 1], stack[stackend] - 1, writeSleep, compSleep);
            stackend--;
            Writes.write(stack, stackend, stack[stackend + 1], 0, false, true);
        }
        }
        if (stack[stackend] == (end - 1)){
            stackend++;
            Writes.write(stack, stackend, end, 0, false, true);;
        }
        while (stackend > 1){
            merge2(array, merge, stack[stackend - 2], stack[stackend - 1] - 1, stack[stackend - 1], stack[stackend] - 1, writeSleep, compSleep);
            stackend--;
            Writes.write(stack, stackend, stack[stackend + 1], 0, false, true);
        }
    }

    protected void healySort(int[] array, int length, int limit, double compSleep, double writeSleep, int parameter1, int parameter2, int parameter3, int parameter4) {
        int length2 = 0;
        int sequence = 0;
        int[] stack = new int[(int) Math.floor(Math.log(length+0.0)/Math.log(2.0)+2.0)];
        int stackend = 0;
        int[] merge = new int[length/2];
        int count = 0;
        int m = 0;
        int start = 0;
        int test = start;
        int start2 = start;
        int end = length;
        int flag = 0;
        while (test < (end - 1)) {
        count = 0;
        m = test;
        start2 = test;
        flag = 0;
        int incs[] = {parameter2, parameter3, parameter4};
        length2 = parameter1;
        if ((length - m) < parameter1){
        length2 = length - m;
        }
        for (int k = 0; k < incs.length; k++) {
                if(incs[k] < length2) {
                    for (int h = incs[k], i = (h + m); i < (length2 + m); i++) {
                        //ArrayVisualizer.setCurrentGap(incs[k]);
                        
                        int v = array[i];
                        int j = i;

                        Highlights.markArray(1, j);
                        Highlights.markArray(2, j - h);

                        while (j >= (h + m) && Reads.compare(array[j - h], v) == 1)
                        {
                            Highlights.markArray(1, j);
                            Highlights.markArray(2, j - h);
                            
                            Writes.write(array, j, array[j - h], writeSleep, false, false);
                            j -= h;
                        }
                        Writes.write(array, j, v, writeSleep, true, false);
                    }
                }
        }
        test += length2;
        sequence++;
        stackend++;
        Writes.write(stack, stackend, test, 0, false, true);
        for (int r = 0; r < Integer.numberOfTrailingZeros(sequence); r++){
            healymerge2(array, merge, stack[stackend - 2], stack[stackend - 1] - 1, stack[stackend - 1], stack[stackend] - 1, writeSleep, compSleep, parameter4);
            stackend--;
            Writes.write(stack, stackend, stack[stackend + 1], 0, false, true);;
        }
        }
        if (stack[stackend] == (end - 1)){
        stackend++;
        Writes.write(stack, stackend, end, 0, false, true);
        }
        while (stackend > 1){
            healymerge2(array, merge, stack[stackend - 2], stack[stackend - 1] - 1, stack[stackend - 1], stack[stackend] - 1, writeSleep, compSleep, parameter4);
            stackend--;
            Writes.write(stack, stackend, stack[stackend + 1], 0, false, true);
        }
        int incs2[] = {9, 6, 4};
        for (int k2 = 0; k2 < incs2.length; k2++) {
                if(incs2[k2] < length) {
                    for (int h2 = incs2[k2], i2 = h2; i2 < length; i2++) {
                        //ArrayVisualizer.setCurrentGap(incs2[k2]);
                        
                        int v2 = array[i2];
                        int j2 = i2;

                        Highlights.markArray(1, j2);
                        Highlights.markArray(2, j2 - h2);
			
                        Delays.sleep(compSleep);
                        while (j2 >= h2 && Reads.compare(array[j2 - h2], v2) == 1)
                        {
                            Highlights.markArray(1, j2);
                            Highlights.markArray(2, j2 - h2);
                            
                            Writes.write(array, j2, array[j2 - h2], writeSleep, false, false);
                            j2 -= h2;
                        }
                        Writes.write(array, j2, v2, writeSleep, true, false);
                    }
                }

        }
        int gap = 4;

        double tempDelay = Delays.getSleepRatio();
        while ((gap > 1))
        {
            Highlights.clearMark(2);

            if (gap > 1) {
                gap = (int) ((float) gap - 1);
                //ArrayVisualizer.setCurrentGap(gap);
            }


            for (int i = 0; (gap + i) < length; ++i)
            {
                Delays.sleep(compSleep);
                if (Reads.compare(array[i], array[i + gap]) == 1)
                {
                    Writes.swap(array, i, i+gap, writeSleep, true, false);
                }
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + gap);
                
                Highlights.clearMark(1);
            }
        } 
        
        Delays.setSleepRatio(tempDelay);
    }

    protected void aMergeSort(int[] array, int length, double compSleep, double writeSleep) {
        int sequence = 0;
        int[] stack = new int[(int) Math.floor(Math.log(length+0.0)/Math.log(2.0)+2.0)];
        int stackend = 0;
        int[] merge = new int[length/2];
        int count = 0;
        int i = 0;
        int start = 0;
        int test = start;
        int start2 = start;
        int end = length;
        int flag = 0;
        while (test < (end - 1)) {
        count = 0;
        i = test + 1;
        start2 = test;
        flag = 0;
        int dir = 1;
        while (i < end && flag == 0) {
            if ((i - start2) == 16 && count > 90){
                dir = -1;
                Writes.reversal(array, start2, i-1, writeSleep, true, false);
                count = 120 - count;
            }
            int num = array[i];
            int v = (2*count / (i - start2)) + 1; //I'VE SOLVED IT!!
            int lo = Math.max(i - v, start2), hi = i;
            while ((lo >= start2) && (Reads.compare(array[lo], num) == dir)){
                lo -= v;
                hi -= v;
            }
            lo++;
            if (lo < start2){
                lo = start2;
            }
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2); // avoid int overflow!
                Highlights.markArray(2, mid);
                
                Delays.sleep(compSleep);
                
                if (Reads.compare(array[mid], num) == dir) { // do NOT move equal elements to right of inserted element; this maintains stability!
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo
            int limit = (int)((i - start2)*(Math.log((i - start2)+0.0)/Math.log(2.0))*1.5);
            count += (i - lo);
            if (count > limit && (i - start2) >= 16){
            flag = 1;
            }
            else {
            if (i > lo){
            int j = i - 1;
            
            while (j >= lo)
            {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            Writes.write(array, lo, num, writeSleep, true, false);
            
            Highlights.clearAllMarks();
            }
            i++;
            }
            test = i;
        }
        if (dir == -1){
            Writes.reversal(array, start2, test-1, writeSleep, true, false);
        }
        sequence++;
        stackend++;
        Writes.write(stack, stackend, test, 0, false, true);
        for (int r = 0; r < Integer.numberOfTrailingZeros(sequence); r++){
            merge2(array, merge, stack[stackend - 2], stack[stackend - 1] - 1, stack[stackend - 1], stack[stackend] - 1, writeSleep, compSleep);
            stackend--;
            Writes.write(stack, stackend, stack[stackend + 1], 0, false, true);
        }
        }
        if (stack[stackend] == (end - 1)){
            stackend++;
            Writes.write(stack, stackend, end, 0, false, true);;
        }
        while (stackend > 1){
            merge2(array, merge, stack[stackend - 2], stack[stackend - 1] - 1, stack[stackend - 1], stack[stackend] - 1, writeSleep, compSleep);
            stackend--;
            Writes.write(stack, stackend, stack[stackend + 1], 0, false, true);
        }
    }

}