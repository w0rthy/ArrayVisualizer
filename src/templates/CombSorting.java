package templates;

import main.ArrayVisualizer;
import sorts.InsertionSort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
The MIT License (MIT)

Copyright (c) 2012 Daniel Imms, http://www.growingwiththeweb.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

public abstract class CombSorting extends Sort {
    protected ArrayVisualizer ArrayVisualizer; // Instance of ArrayVisualizer used to print
                                               // Combsort's current gap to the statistics
    
    private InsertionSort insertSorter;
    
    final private double[] shrinkFactors = {1.1, 1.15, 1.2, 1.25, 1.3, 1.35, 1.4, 1.45, 1.5};
    private int shrinkChoice;
    
    protected CombSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    protected double[] getShrinkFactors() {
        return this.shrinkFactors;
    }
    
    protected void setShrinkFactor(int choice) {
        this.shrinkChoice = choice;
    }
    
    public void setArrayVisualizer(ArrayVisualizer av) {
        this.ArrayVisualizer = av;
    }
    
    protected void combSort(ArrayVisualizer ArrayVisualizer, int[] array, int length, boolean hybrid) {
        insertSorter = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        double shrink = shrinkFactors[this.shrinkChoice];

        boolean swapped = false;
        int gap = length;

        double tempDelay = Delays.getSleepRatio();
        
        while ((gap > 1) || swapped)
        {
            Highlights.clearMark(2);

            if (gap > 1) {
                gap = (int) ((float) gap / shrink);
                //ArrayVisualizer.setCurrentGap(gap);

                if(gap == 1)
                    Delays.setSleepRatio(10);
            }

            swapped = false;

            for (int i = 0; (gap + i) < length; ++i)
            {
                if(hybrid && (gap <= Math.min(8, length * 0.03125))) {
                    gap = 0;
                    
                    insertSorter.customInsertSort(array, 0, length, 0.5, false);
                    break;
                }
                if (Reads.compare(array[i], array[i + gap]) == 1)
                {
                    Writes.swap(array, i, i+gap, 0.75, true, false);
                    swapped = true;
                }
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + gap);
                
                Delays.sleep(0.25);
                Highlights.clearMark(1);
            }
        } 
        
        Delays.setSleepRatio(tempDelay);
    }
}