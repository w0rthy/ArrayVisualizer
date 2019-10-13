package sorts;

import templates.HolyGrailSorting;
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

/********* Holy Grail Sorting ****************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* (c) 2019 modified by John Reynolds (MusicTheorist)    */
/*                                                       */
/* Holy Grail Sort is a variant of Grail Sort, an        */
/* implementation of Block Merge Sort.                   */
/*                                                       */
/* Grail Sort is a stable sort that works in O(n log n)  */
/* worst time and uses O(1) extra memory. It splits      */
/* sorted ranges of an array into "blocks" of length     */
/* sqrt(n), combines pairs of ranges via swapping        */
/* blocks, and locally merges blocks together using      */
/* an internal buffer. It maintains stability by         */
/* collecting unique values throughout the array called  */
/* "keys", and tagging blocks with these keys to track   */
/* their movements (think of "key-value pairs").         */
/*                                                       */
/* Holy Grail Sort improves on these ideas by using      */
/* Binary Insertion Sort instead of Insertion Sort,      */
/* reducing the number of write operations in select     */
/* cases of Grail Sort's "Rotate" method, and            */
/* implementing a bidirectional internal buffer. These   */
/* optimizations result in an algorithm that is at least */
/* 30% faster than Grail Sort.                           */
/*                                                       */
/* Define int / SortComparator                     	     */
/* and then call HolyGrailSort() function                */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use HolyGrailSortWithBuffer()                         */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use HolyGrailSortWithDynBuffer()                      */
/*                                                       */
/*********************************************************/

SORT DISABLED

final public class HolyGrailSort extends HolyGrailSorting {
    private int bufferType = 0;
    
    public HolyGrailSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Holy Grail");
        this.setRunAllID("Holy Grail Sort (Block Merge Sort)");
        this.setReportSortID("Holygrailsort");
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
                throw new Exception("Invalid Holy Grail buffer!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}