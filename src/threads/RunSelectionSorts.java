package threads;

import main.ArrayVisualizer;
import sorts.CycleSort;
import sorts.DoubleSelectionSort;
import sorts.MaxHeapSort;
import sorts.MinHeapSort;
import sorts.FlippedMinHeapSort;
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
    public RunSelectionSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 11;
        this.categoryCount = this.sortCount;
    }

    public synchronized void ReportSelectionSorts(int[] array) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort SelectionSort       = new       SelectionSort(Delays, Highlights, Reads, Writes);
                    Sort DoubleSelectionSort = new DoubleSelectionSort(Delays, Highlights, Reads, Writes);
                    Sort CycleSort           = new           CycleSort(Delays, Highlights, Reads, Writes);
                    Sort MaxHeapSort         = new         MaxHeapSort(Delays, Highlights, Reads, Writes);
                    Sort MinHeapSort         = new         MinHeapSort(Delays, Highlights, Reads, Writes);
                    Sort FlippedMinHeapSort  = new  FlippedMinHeapSort(Delays, Highlights, Reads, Writes);
                    Sort WeakHeapSort        = new        WeakHeapSort(Delays, Highlights, Reads, Writes);
                    Sort TernaryHeapSort     = new     TernaryHeapSort(Delays, Highlights, Reads, Writes);
                    Sort SmoothSort          = new          SmoothSort(Delays, Highlights, Reads, Writes);
                    Sort PoplarHeapSort      = new      PoplarHeapSort(Delays, Highlights, Reads, Writes);
                    Sort TournamentSort      = new      TournamentSort(Delays, Highlights, Reads, Writes);

                    RunSelectionSorts.this.sortNumber = 1;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Selection Sorts");

                    RunSelectionSorts.this.RunIndividualSort(SelectionSort,       0, array,  128, 0.01);
                    RunSelectionSorts.this.RunIndividualSort(DoubleSelectionSort, 0, array,  128, 0.01);
                    RunSelectionSorts.this.RunIndividualSort(CycleSort,           0, array,  128, 0.01);
                    RunSelectionSorts.this.RunIndividualSort(MaxHeapSort,         0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(MinHeapSort,         0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(FlippedMinHeapSort,  0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(WeakHeapSort,        0, array, 2048, 1);
                    RunSelectionSorts.this.RunIndividualSort(TernaryHeapSort,     0, array, 2048, 1);
                    RunSelectionSorts.this.RunIndividualSort(SmoothSort,          0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(PoplarHeapSort,      0, array, 2048, 1);
                    RunSelectionSorts.this.RunIndividualSort(TournamentSort,      0, array, 2048, 1.5);
                    
                    ArrayVisualizer.setCategory("Run Selection Sorts");
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
                    Sort SelectionSort       = new       SelectionSort(Delays, Highlights, Reads, Writes);
                    Sort DoubleSelectionSort = new DoubleSelectionSort(Delays, Highlights, Reads, Writes);
                    Sort CycleSort           = new           CycleSort(Delays, Highlights, Reads, Writes);
                    Sort MaxHeapSort         = new         MaxHeapSort(Delays, Highlights, Reads, Writes);
                    Sort MinHeapSort         = new         MinHeapSort(Delays, Highlights, Reads, Writes);
                    Sort FlippedMinHeapSort  = new  FlippedMinHeapSort(Delays, Highlights, Reads, Writes);
                    Sort WeakHeapSort        = new        WeakHeapSort(Delays, Highlights, Reads, Writes);
                    Sort TernaryHeapSort     = new     TernaryHeapSort(Delays, Highlights, Reads, Writes);
                    Sort SmoothSort          = new          SmoothSort(Delays, Highlights, Reads, Writes);
                    Sort PoplarHeapSort      = new      PoplarHeapSort(Delays, Highlights, Reads, Writes);
                    Sort TournamentSort      = new      TournamentSort(Delays, Highlights, Reads, Writes);

                    RunSelectionSorts.this.sortNumber = current;
                    RunSelectionSorts.this.sortCount = total;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Selection Sorts");

                    RunSelectionSorts.this.RunIndividualSort(SelectionSort,       0, array,  128, 0.01);
                    RunSelectionSorts.this.RunIndividualSort(DoubleSelectionSort, 0, array,  128, 0.01);
                    RunSelectionSorts.this.RunIndividualSort(CycleSort,           0, array,  128, 0.01);
                    RunSelectionSorts.this.RunIndividualSort(MaxHeapSort,         0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(MinHeapSort,         0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(FlippedMinHeapSort,  0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(WeakHeapSort,        0, array, 2048, 1);
                    RunSelectionSorts.this.RunIndividualSort(TernaryHeapSort,     0, array, 2048, 1);
                    RunSelectionSorts.this.RunIndividualSort(SmoothSort,          0, array, 2048, 1.5);
                    RunSelectionSorts.this.RunIndividualSort(PoplarHeapSort,      0, array, 2048, 1);
                    RunSelectionSorts.this.RunIndividualSort(TournamentSort,      0, array, 2048, 1.5);
                    
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