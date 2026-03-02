package sorts;

import templates.GrailSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
The MIT License (MIT)

Copyright (c) 2013 Andrey Astrelin

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

/********* Grail sorting *********************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* Refactored by MusicTheorist                           */
/*                                                       */
/* Stable sorting that works in O(N*log(N)) worst time   */
/* and uses O(1) extra memory                            */
/*                                                       */
/* Define int / SortComparator                     	     */
/* and then call GrailSort() function                    */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use GrailSortWithBuffer()                             */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use GrailSortWithDynBuffer()                          */
/*                                                       */
/*********************************************************/

final public class GrailSort extends GrailSorting {
    private int bufferType = 0;
    
    public GrailSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Grail");
        this.setRunAllID("Grail Sort (Block Merge Sort)");
        this.setReportSortID("Grailsort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    // 0 for no buffer, 1 for static buffer, 2 for dynamic buffer
    public void chooseBuffer(int choice) {
        bufferType = choice;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if(bufferType == 0) this.grailCommonSort(array, 0, length, null, 0, 0);
        else if(bufferType == 1) {
            int[] ExtBuf = new int[this.getStaticBuffer()];
            this.grailCommonSort(array, 0, length, ExtBuf, 0, this.getStaticBuffer());
        }
        else if(bufferType == 2) {
            int tempLen = 1;
            while(tempLen * tempLen < length) tempLen *= 2;
            int[] DynExtBuf = new int[tempLen];
            this.grailCommonSort(array, 0, length, DynExtBuf, 0, tempLen);
        }
        else {
            try {
                throw new Exception("Invalid Grail buffer!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}