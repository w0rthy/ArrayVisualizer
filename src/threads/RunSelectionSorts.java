package threads;

import main.ArrayVisualizer;
import sorts.CycleSort;
import sorts.DoubleSelectionSort;
import sorts.FlippedMinHeapSort;
import sorts.MaxHeapSort;
import sorts.MinHeapSort;
import sorts.PoplarHeapSort;
import sorts.SelectionSort;
import sorts.SmoothSort;
import sorts.TernaryHeapSort;
import sorts.TournamentSort;
import sorts.WeakHeapSort;
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

final public class RunSelectionSorts extends MultipleSortThread {
    private Sort SelectionSort;
    private Sort DoubleSelectionSort;
    private Sort CycleSort;
    private Sort MaxHeapSort;
    private Sort MinHeapSort;
    private Sort FlippedMinHeapSort;
    private Sort WeakHeapSort;
    private Sort TernaryHeapSort;
    private Sort SmoothSort;
    private Sort PoplarHeapSort;
    private Sort TournamentSort;
    
    public RunSelectionSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 11;
        this.categoryCount = this.sortCount;
        
        SelectionSort       = new       SelectionSort(Delays, Highlights, Reads, Writes);
        DoubleSelectionSort = new DoubleSelectionSort(Delays, Highlights, Reads, Writes);
        CycleSort           = new           CycleSort(Delays, Highlights, Reads, Writes);
        MaxHeapSort         = new         MaxHeapSort(Delays, Highlights, Reads, Writes);
        MinHeapSort         = new         MinHeapSort(Delays, Highlights, Reads, Writes);
        FlippedMinHeapSort  = new  FlippedMinHeapSort(Delays, Highlights, Reads, Writes);
        WeakHeapSort        = new        WeakHeapSort(Delays, Highlights, Reads, Writes);
        TernaryHeapSort     = new     TernaryHeapSort(Delays, Highlights, Reads, Writes);
        SmoothSort          = new          SmoothSort(Delays, Highlights, Reads, Writes);
        PoplarHeapSort      = new      PoplarHeapSort(Delays, Highlights, Reads, Writes);
        TournamentSort      = new      TournamentSort(Delays, Highlights, Reads, Writes);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunSelectionSorts.this.runIndividualSort(SelectionSort,       0, array,  128, 0.01);
        RunSelectionSorts.this.runIndividualSort(DoubleSelectionSort, 0, array,  128, 0.01);
        RunSelectionSorts.this.runIndividualSort(CycleSort,           0, array,  128, 0.01);
        RunSelectionSorts.this.runIndividualSort(MaxHeapSort,         0, array, 2048, 1.5);
        RunSelectionSorts.this.runIndividualSort(MinHeapSort,         0, array, 2048, 1.5);
        RunSelectionSorts.this.runIndividualSort(FlippedMinHeapSort,  0, array, 2048, 1.5);
        RunSelectionSorts.this.runIndividualSort(WeakHeapSort,        0, array, 2048, 1);
        RunSelectionSorts.this.runIndividualSort(TernaryHeapSort,     0, array, 2048, 1);
        RunSelectionSorts.this.runIndividualSort(SmoothSort,          0, array, 2048, 1.5);
        RunSelectionSorts.this.runIndividualSort(PoplarHeapSort,      0, array, 2048, 1);
        RunSelectionSorts.this.runIndividualSort(TournamentSort,      0, array, 2048, 1.5);
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
                        RunSelectionSorts.this.sortNumber = current;
                        RunSelectionSorts.this.sortCount = total;
                    }
                    else {
                        RunSelectionSorts.this.sortNumber = 1;
                    }
                    
                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Selection Sorts");

                    RunSelectionSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        ArrayVisualizer.setCategory("Run Selection Sorts");
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
    
    @Override
    public synchronized void reportCategorySorts(int[] array) throws Exception {
        this.runThread(array, 0, 0, false);
    }
    
    @Override
    public synchronized void reportAllSorts(int[] array, int current, int total) throws Exception {
        this.runThread(array, current, total, true);
    }
}