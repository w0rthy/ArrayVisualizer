package threads;

import main.ArrayVisualizer;
import sorts.BinaryMergeSort;
import sorts.BottomUpMergeSort;
import sorts.BranchedPDQSort;
import sorts.BranchlessPDQSort;
import sorts.CocktailMergeSort;
import sorts.GrailSort;
import sorts.HybridCombSort;
import sorts.IntroCircleSort;
import sorts.IntroSort;
import sorts.OptimizedDualPivotQuickSort;
import sorts.SqrtSort;
import sorts.TimSort;
import sorts.WeaveMergeSort;
import sorts.WikiSort;
import templates.JErrorPane;
import templates.MultipleSortThread;
import templates.Sort;
import utils.Shuffles;

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

final public class RunHybridSorts extends MultipleSortThread {
    private Sort HybridCombSort;
    private Sort IntroCircleSort;
    private Sort BinaryMergeSort;
    private Sort WeaveMergeSort;
    private Sort TimSort;
    private Sort CocktailMergeSort;
    private Sort WikiSort;
    private Sort GrailSort;
    private Sort SqrtSort;
    private Sort IntroSort;
    private Sort BottomUpMergeSort;
    private Sort OptimizedDualPivotQuickSort;
    private Sort BranchedPDQSort;
    private Sort BranchlessPDQSort;
    
    public RunHybridSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 14;
        this.categoryCount = this.sortCount;
        
        HybridCombSort              = new              HybridCombSort(Delays, Highlights, Reads, Writes);
        IntroCircleSort             = new             IntroCircleSort(Delays, Highlights, Reads, Writes);
        BinaryMergeSort             = new             BinaryMergeSort(Delays, Highlights, Reads, Writes);
        WeaveMergeSort              = new              WeaveMergeSort(Delays, Highlights, Reads, Writes);
        TimSort                     = new                     TimSort(Delays, Highlights, Reads, Writes);
        CocktailMergeSort           = new           CocktailMergeSort(Delays, Highlights, Reads, Writes);
        WikiSort                    = new                    WikiSort(Delays, Highlights, Reads, Writes);
        GrailSort                   = new                   GrailSort(Delays, Highlights, Reads, Writes);
        SqrtSort                    = new                    SqrtSort(Delays, Highlights, Reads, Writes);
        IntroSort                   = new                   IntroSort(Delays, Highlights, Reads, Writes);
        BottomUpMergeSort           = new           BottomUpMergeSort(Delays, Highlights, Reads, Writes);
        OptimizedDualPivotQuickSort = new OptimizedDualPivotQuickSort(Delays, Highlights, Reads, Writes);
        BranchedPDQSort             = new             BranchedPDQSort(Delays, Highlights, Reads, Writes);
        BranchlessPDQSort           = new           BranchlessPDQSort(Delays, Highlights, Reads, Writes);
    }
    
    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunHybridSorts.this.runIndividualSort(HybridCombSort,              0, array, 1024, 1);
        RunHybridSorts.this.runIndividualSort(IntroCircleSort,             0, array, 1024, 1);
        RunHybridSorts.this.runIndividualSort(BinaryMergeSort,             0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(WeaveMergeSort,              0, array, 2048, ArrayManager.getShuffle() == Shuffles.RANDOM ? 1.25 : 6);
        RunHybridSorts.this.runIndividualSort(TimSort,                     0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(CocktailMergeSort,           0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(WikiSort,                    0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(GrailSort,                   0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(SqrtSort,                    0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(IntroSort,                   0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(BottomUpMergeSort,           0, array, 2048, 1);
        RunHybridSorts.this.runIndividualSort(OptimizedDualPivotQuickSort, 0, array, 2048, 0.75);
        RunHybridSorts.this.runIndividualSort(BranchedPDQSort,             0, array, 2048, 0.75);
        RunHybridSorts.this.runIndividualSort(BranchlessPDQSort,           0, array, 2048, 0.75);
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
                        RunHybridSorts.this.sortNumber = current;
                        RunHybridSorts.this.sortCount = total;
                    }
                    else {
                        RunHybridSorts.this.sortNumber = 1;
                    }
                    
                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Hybrid Sorts");

                    RunHybridSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        ArrayVisualizer.setCategory("Run Hybrid Sorts");
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