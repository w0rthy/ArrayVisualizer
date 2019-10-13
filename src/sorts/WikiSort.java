package sorts;

import templates.Sort;
import templates.WikiSorting;

import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>
 *
 */

public class WikiSort extends Sort {
    private WikiSorting wikiSortInstance; // Just like TimSort, WikiSort cannot be simply written off as an abstract class, as it creates an 
                                          // instance of itself in order to track its state. Plus, it contains both instance and static methods, 
                                          // requiring even more refactoring, which would be just doing unnecessary busy work. Instead of what
                                          // we've done for the rest of the algorithms, we'll favor composition over inheritance here and pass
                                          // "util" objects to it.
    
    private InsertionSort insertionSort;
    
    // Cache sizes for WikiSort
    
    // final private static int halfSize = (currentLen + 1) / 2;
    // final private static int squareRoot = (int) (Math.sqrt((currentLen + 1) / 2) + 1);
    // final private static int staticBuffer = 32;
    // final private static int noBuffer = 0;
    
    private int cache;
    
    public WikiSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Wiki");
        this.setRunAllID("Wiki Sort (Block Merge Sort)");
        this.setReportSortID("Wikisort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.cache = 0;
        
        this.insertionSort = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        this.wikiSortInstance = new WikiSorting(this.insertionSort, this.Highlights, this.Reads, this.Writes, this.cache);
        
        WikiSorting.sort(this.wikiSortInstance, array, currentLength);
    }
}