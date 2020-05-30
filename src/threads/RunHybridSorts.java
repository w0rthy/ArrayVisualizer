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
    public RunHybridSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 14;
        this.categoryCount = this.sortCount;
    }

    public synchronized void ReportHybridSorts(int[] array) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort HybridCombSort              = new              HybridCombSort(Delays, Highlights, Reads, Writes);
                    Sort IntroCircleSort             = new             IntroCircleSort(Delays, Highlights, Reads, Writes);
                    Sort BinaryMergeSort             = new             BinaryMergeSort(Delays, Highlights, Reads, Writes);
                    Sort WeaveMergeSort              = new              WeaveMergeSort(Delays, Highlights, Reads, Writes);
                    Sort TimSort                     = new                     TimSort(Delays, Highlights, Reads, Writes);
                    Sort CocktailMergeSort           = new           CocktailMergeSort(Delays, Highlights, Reads, Writes);
                    Sort WikiSort                    = new                    WikiSort(Delays, Highlights, Reads, Writes);
                    Sort GrailSort                   = new                   GrailSort(Delays, Highlights, Reads, Writes);
                    Sort SqrtSort                    = new                    SqrtSort(Delays, Highlights, Reads, Writes);
                    Sort IntroSort                   = new                   IntroSort(Delays, Highlights, Reads, Writes);
                    Sort BottomUpMergeSort           = new           BottomUpMergeSort(Delays, Highlights, Reads, Writes);
                    Sort OptimizedDualPivotQuickSort = new OptimizedDualPivotQuickSort(Delays, Highlights, Reads, Writes);
                    Sort BranchedPDQSort             = new             BranchedPDQSort(Delays, Highlights, Reads, Writes);
                    Sort BranchlessPDQSort           = new           BranchlessPDQSort(Delays, Highlights, Reads, Writes);

                    RunHybridSorts.this.sortNumber = 1;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Hybrid Sorts");

                    RunHybridSorts.this.RunIndividualSort(HybridCombSort,              0, array, 1024, 1);
                    RunHybridSorts.this.RunIndividualSort(IntroCircleSort,             0, array, 1024, 1);
                    RunHybridSorts.this.RunIndividualSort(BinaryMergeSort,             0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(WeaveMergeSort,              0, array, 2048, 1.25);
                    RunHybridSorts.this.RunIndividualSort(TimSort,                     0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(CocktailMergeSort,           0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(WikiSort,                    0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(GrailSort,                   0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(SqrtSort,                    0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(IntroSort,                   0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(BottomUpMergeSort,           0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(OptimizedDualPivotQuickSort, 0, array, 2048, 0.75);
                    RunHybridSorts.this.RunIndividualSort(BranchedPDQSort,             0, array, 2048, 0.75);
                    RunHybridSorts.this.RunIndividualSort(BranchlessPDQSort,           0, array, 2048, 0.75);
                    
                    ArrayVisualizer.setCategory("Run Hybrid Sorts");
                    ArrayVisualizer.setHeading("Done");
                    
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

    @Override
    public void ReportAllSorts(int[] array, int current, int total) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort HybridCombSort              = new              HybridCombSort(Delays, Highlights, Reads, Writes);
                    Sort IntroCircleSort             = new             IntroCircleSort(Delays, Highlights, Reads, Writes);
                    Sort BinaryMergeSort             = new             BinaryMergeSort(Delays, Highlights, Reads, Writes);
                    Sort WeaveMergeSort              = new              WeaveMergeSort(Delays, Highlights, Reads, Writes);
                    Sort TimSort                     = new                     TimSort(Delays, Highlights, Reads, Writes);
                    Sort CocktailMergeSort           = new           CocktailMergeSort(Delays, Highlights, Reads, Writes);
                    Sort WikiSort                    = new                    WikiSort(Delays, Highlights, Reads, Writes);
                    Sort GrailSort                   = new                   GrailSort(Delays, Highlights, Reads, Writes);
                    Sort SqrtSort                    = new                    SqrtSort(Delays, Highlights, Reads, Writes);
                    Sort IntroSort                   = new                   IntroSort(Delays, Highlights, Reads, Writes);
                    Sort BottomUpMergeSort           = new           BottomUpMergeSort(Delays, Highlights, Reads, Writes);
                    Sort OptimizedDualPivotQuickSort = new OptimizedDualPivotQuickSort(Delays, Highlights, Reads, Writes);
                    Sort BranchedPDQSort             = new             BranchedPDQSort(Delays, Highlights, Reads, Writes);
                    Sort BranchlessPDQSort           = new           BranchlessPDQSort(Delays, Highlights, Reads, Writes);

                    RunHybridSorts.this.sortNumber = current;
                    RunHybridSorts.this.sortCount = total;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Hybrid Sorts");

                    RunHybridSorts.this.RunIndividualSort(HybridCombSort,              0, array, 1024, 1);
                    RunHybridSorts.this.RunIndividualSort(IntroCircleSort,             0, array, 1024, 1);
                    RunHybridSorts.this.RunIndividualSort(BinaryMergeSort,             0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(WeaveMergeSort,              0, array, 2048, 1.25);
                    RunHybridSorts.this.RunIndividualSort(TimSort,                     0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(CocktailMergeSort,           0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(WikiSort,                    0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(GrailSort,                   0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(SqrtSort,                    0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(IntroSort,                   0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(BottomUpMergeSort,           0, array, 2048, 1);
                    RunHybridSorts.this.RunIndividualSort(OptimizedDualPivotQuickSort, 0, array, 2048, 0.75);
                    RunHybridSorts.this.RunIndividualSort(BranchedPDQSort,             0, array, 2048, 0.75);
                    RunHybridSorts.this.RunIndividualSort(BranchlessPDQSort,           0, array, 2048, 0.75);
                             
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