package templates;

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

public abstract class BogoSorting extends Sort {
    protected BogoSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    private static int randomPosition(int length, int offset) {
        return (int) ((Math.random() * (length - offset)) + offset);
    }
    
    protected void bogoSwap(int[] array, int length, int offset){
        for(int i = offset; i < length; i++) {
            Writes.swap(array, i, BogoSorting.randomPosition(length, offset), 0, true, false);
        }
    }
    
    protected boolean bogoIsSorted(int[] array, int length){
        for(int i = 0; i < length - 1; i++) {
            if(Reads.compare(array[i], array[i + 1]) == 1) {
                Highlights.markArray(1, i);
                return false;
            }
        }
        Highlights.clearMark(1);
        return true;
    }
    
    protected boolean bogoIsSortedWithDelay(int[] array, int length, double sleep){
        for(int i = 0; i < length - 1; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(sleep);
            if(Reads.compare(array[i], array[i + 1]) == 1) return false;
        }
        Highlights.clearMark(1);
        return true;
    }
    
    protected boolean isMinSorted(int[] array, int length, int offset) {
        Highlights.clearAllMarks();
        
        //Highlights.markArray(2, offset);
        //Highlights.markArray(3, length);
        
        for(int i = offset + 1; i < length; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.075);
            
            if(Reads.compare(array[offset], array[i]) == 1) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean isMaxSorted(int[] array, int minIterator, int maxIterator) {
        Highlights.clearAllMarks();
        
        //Highlights.markArray(2, minIterator);
        //Highlights.markArray(3, maxIterator);
        
        for(int i = maxIterator; i >= minIterator; i--) {
            Highlights.markArray(1, i);
            Delays.sleep(0.075);
            
            if(Reads.compare(array[maxIterator], array[i]) == -1) {
                return false;
            }
        }
        return true;
    }
}