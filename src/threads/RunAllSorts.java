package threads;

import main.ArrayManager;
import main.ArrayVisualizer;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Sounds;
import utils.Timer;
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

final public class RunAllSorts {

                    category = "Merge Sorts";

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Merge Sort";
                    SLEEPRATIO = 1.75;
                    startRealTimer();
                    mergeSortOOP(array, currentLen, false);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "In-Place Merge Sort";
                    SLEEPRATIO = 7;
                    startRealTimer();
                    mergeSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    category = "Distribution Sorts";

                    SLEEPRATIO = 2.5; 

                    int radix = currentLen < 256 ? currentLen / 2 : 256;
                    if(radix == 1) radix = 2;
                    
                    refresharray();
                    heading = "American Flag Sort, " + radix + " Buckets";
                    startRealTimer();
                    flagSort(array, currentLen, radix);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Bead (Gravity) Sort";
                    SLEEPRATIO = Integer.MAX_VALUE;
                    startRealTimer();
                    gravitySort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Counting Sort";
                    SLEEPRATIO = 5;
                    startRealTimer();
                    countingSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Pigeonhole Sort";
                    SLEEPRATIO = 5;
                    startRealTimer();
                    pigeonSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;
                    
                    radix = currentLen == 2 ? 2 : 4;
                    
                    refresharray();
                    heading = "Radix LSD Sort, Base " + radix;
                    SLEEPRATIO = 2;
                    startRealTimer();
                    radixLSDsort(array, currentLen, radix);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;
                    SOUNDMUL = 0.01;
                    
                    radix = currentLen < 16 ? currentLen / 2 : 16;
                    if(radix == 1) radix = 2;
                    
                    refresharray();
                    heading = "In-Place Radix LSD Sort, Base " + radix;
                    SLEEPRATIO = 2;
                    startRealTimer();
                    inPlaceRadixLSDSort(array, currentLen, radix);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;
                    SOUNDMUL = storeVol;

                    radix = currentLen < 8 ? currentLen / 2 : 8;
                    if(radix == 1) radix = 2;
                    
                    refresharray();
                    heading = "Radix MSD Sort, Base " + radix;
                    SLEEPRATIO = 1.75;
                    startRealTimer();
                    radixMSDSort(array, currentLen, radix);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Flash Sort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    flashSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Shatter Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    shatterSort(array, currentLen, 64);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Simple Shatter Sort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    simpleShatterSort(array, currentLen, 256, 4);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Time Sort (Mul 4)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    timeSort(array, currentLen, 4);

                    endSort();
                    Thread.sleep(1000);

                    category = "Concurrent Sorts";

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Batcher's Bitonic Sort";
                    SLEEPRATIO = 4.5;
                    startRealTimer();
                    bitonicSort(array, 0, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Batcher's Odd-Even Merge Sort";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    oddEvenMergeSort(array, 0, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Hybrid Sorts";

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Hybrid Comb Sort (Comb/Insertion)";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    combSort(array, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Binary Merge Sort (Merge/Binary Insertion)";
                    SLEEPRATIO = 1.25;
                    startRealTimer();
                    mergeSortOOP(array, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Weave Merge Sort (Merge/Insertion)";
                    SLEEPRATIO = 2.5;
                    startRealTimer();
                    weaveMergeSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "TimSort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    timSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "WikiSort (Block Merge Sort)";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    startWikiSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "GrailSort (Block Merge Sort)";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    grailSortWithoutBuffer(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "std::sort (Introsort)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    introSort(array, currentLen, 32, 1, 0);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Quick Shell Sort (Introsort with Shellsort)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    introSort(array, currentLen, 48, 2, 12);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "std::stable_sort (Insert/Bottom-up Merge)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    stableSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Miscellaneous Sorts";
                    
                    int shortLen = (int) Math.ceil((256 * (currentLen / 2048d)));
                    if(shortLen == 1) shortLen = 2;
                    if(VISUALS == CIRCULAR && shortLen == 2) shortLen = 4;
                    currentLen = currentLen >= 2048 ? 256 : shortLen; 
                    
                    SLEEPRATIO = 1;

                    refresharray();
                    heading = "Pancake Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    pancakeSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Impractical Sorts";

                    refresharray();
                    heading = "Stooge Sort";
                    SLEEPRATIO = 2.5;
                    startRealTimer();
                    stoogeSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 1;

                    refresharray();
                    heading = "Bad Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    badSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);
                    
                    refresharray();
                    heading = "Silly Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    sillySort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    refresharray();
                    heading = "Slow Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    slowSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);
                    
                    refresharray();
                    heading = "Less Bogo Sort";
                    SLEEPRATIO = 300;
                    startRealTimer();
                    lessBogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 1;

                    refresharray();
                    heading = "Cocktail Bogo Sort";
                    startRealTimer();
                    SLEEPRATIO = 300;
                    doubleBogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);
                    
                    SLEEPRATIO = 1;

                    refresharray();
                    heading = "Bubble Bogo Sort";
                    startRealTimer();
                    SLEEPRATIO = 1;
                    bubbleBogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 1;
                    
                    refresharray();
                    heading = "Exchange Bogo Sort";
                    startRealTimer();
                    SLEEPRATIO = 0.35;
                    exchangeBogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);
                                        
                    currentLen = currentLen < 8 ? currentLen : 8;
                    if(VISUALS == CIRCULAR && currentLen == 2) currentLen = 4;
                    
                    SLEEPRATIO = 0.01;

                    refresharray();
                    heading = "Bogo Sort";
                    startRealTimer();
                    SLEEPRATIO = 20;
                    bogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Run All";
                    heading = "Done";

                    MUTABLE = true;
                }catch (Exception e){}
                SetSound(false);
            }
        };
        sortingThread.start(); 
    }
}