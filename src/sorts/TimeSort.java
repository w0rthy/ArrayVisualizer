package sorts;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.ArrayVisualizer;
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

final public class TimeSort extends Sort {
    private InsertionSort insertSorter;
    
    private volatile int next = 0;
    
    public TimeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Time");
        this.setRunAllID("Time Sort");
        this.setReportSortID("Timesort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false);
        this.isBucketSort(false); // *Does not* use buckets! "magnitude" is only a multiplier.
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(2); //See threads.RunDistributionSort for details
        this.isBogoSort(false);
    }
    
    private synchronized void report(int[] array, int a){
        Writes.write(array, next, a, 0, true, false);
        next++;
    }

    @Override
    public void runSort(int[] array, int length, int magnitude) {
        insertSorter = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        
        final int A = magnitude;
        next = 0;
        
        ArrayList<Thread> threads = new ArrayList<>();
        
        final int[] tmp = new int[length];
        
        for(int i = 0; i < length; i++) {
            Writes.write(tmp, i, array[i], 0.25, true, true);
        }
        
        double temp = Delays.getCurrentDelay();
        Delays.setCurrentDelay(magnitude);
        
        for(int i = 0; i < length; i++){
            final int c = i;
            
            threads.add(new Thread(){
                @Override
                public void run() {
                    int a = tmp[c];
                   
                    try {
                        sleep(a*A);
                        Writes.addTime(A);
                    } 
                    catch (InterruptedException ex) {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    TimeSort.this.report(array, a);
                }
            });
        }
        
        for(Thread t : threads)
            t.start();
        
        try {
            Thread.sleep(length * A);
        }
        catch (InterruptedException e) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
        }
        
        Delays.setCurrentDelay(temp);
        Writes.setTime(length * A);
        
        insertSorter.customInsertSort(array, 0, length, 0.2, false);
    }
}