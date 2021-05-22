package threads;

import main.ArrayVisualizer;
import sorts.IterativeBitonicSort;
import sorts.IterativeOddEvenMergeSort;
import sorts.IterativePairwiseSort;
import sorts.RecursiveBitonicSort;
import sorts.RecursiveOddEvenMergeSort;
import sorts.RecursivePairwiseSort;
import templates.JErrorPane;
import templates.MultipleSortThread;
import templates.Sort;

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

final public class RunConcurrentSorts extends MultipleSortThread {
    private Sort RecursiveBitonicSort;
    private Sort RecursiveOddEvenMergeSort;
    private Sort RecursivePairwiseSort;
    private Sort IterativeBitonicSort;
    private Sort IterativeOddEvenMergeSort;
    private Sort IterativePairwiseSort;
    
    public RunConcurrentSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 6;
        this.categoryCount = this.sortCount;
        
        RecursiveBitonicSort      = new      RecursiveBitonicSort(Delays, Highlights, Reads, Writes);
        RecursiveOddEvenMergeSort = new RecursiveOddEvenMergeSort(Delays, Highlights, Reads, Writes);
        RecursivePairwiseSort     = new     RecursivePairwiseSort(Delays, Highlights, Reads, Writes);
        IterativeBitonicSort      = new      IterativeBitonicSort(Delays, Highlights, Reads, Writes);
        IterativeOddEvenMergeSort = new IterativeOddEvenMergeSort(Delays, Highlights, Reads, Writes);
        IterativePairwiseSort     = new     IterativePairwiseSort(Delays, Highlights, Reads, Writes);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunConcurrentSorts.this.runIndividualSort(RecursiveBitonicSort,      0, array, 1024, 0.6667);
        RunConcurrentSorts.this.runIndividualSort(RecursiveOddEvenMergeSort, 0, array, 1024, 1);
        RunConcurrentSorts.this.runIndividualSort(RecursivePairwiseSort,     0, array, 1024, 1);
        RunConcurrentSorts.this.runIndividualSort(IterativeBitonicSort,      0, array, 1024, 0.3333);
        RunConcurrentSorts.this.runIndividualSort(IterativeOddEvenMergeSort, 0, array, 1024, 1);
        RunConcurrentSorts.this.runIndividualSort(IterativePairwiseSort,     0, array, 1024, 0.8);
    }
    
    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    if(runAllActive) {
                        RunConcurrentSorts.this.sortNumber = current;
                        RunConcurrentSorts.this.sortCount = total;
                    }
                    else {
                        RunConcurrentSorts.this.sortNumber = 1;
                    }

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Concurrent Sorts");

                    RunConcurrentSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        ArrayVisualizer.setCategory("Run Concurrent Sorts");
                        ArrayVisualizer.setHeading("Done");
                    }
                    
                    ArrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                ArrayVisualizer.setSortingThread(null);
            }
        });

        ArrayVisualizer.runSortingThread();
    }
}