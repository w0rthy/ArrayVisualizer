package threads;

import main.ArrayVisualizer;
import sorts.BinaryGnomeSort;
import sorts.BubbleSort;
import sorts.CircleSort;
import sorts.CocktailShakerSort;
import sorts.CombSort;
import sorts.DualPivotQuickSort;
import sorts.GnomeSort;
import sorts.LLQuickSort;
import sorts.LRQuickSort;
import sorts.OddEvenSort;
import sorts.SmartBubbleSort;
import sorts.SmartGnomeSort;
import sorts.StableQuickSort;
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

final public class RunExchangeSorts extends MultipleSortThread {
    public RunExchangeSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 12;
                       //13;
        this.categoryCount = this.sortCount;
    }

    public synchronized void ReportExchangeSorts(int[] array) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort BubbleSort         = new         BubbleSort(Delays, Highlights, Reads, Writes);
                    Sort SmartBubbleSort    = new    SmartBubbleSort(Delays, Highlights, Reads, Writes);
                    Sort CocktailShakerSort = new CocktailShakerSort(Delays, Highlights, Reads, Writes);
                    Sort OddEvenSort        = new        OddEvenSort(Delays, Highlights, Reads, Writes);
                    Sort GnomeSort          = new          GnomeSort(Delays, Highlights, Reads, Writes);
                    Sort SmartGnomeSort     = new     SmartGnomeSort(Delays, Highlights, Reads, Writes);
                    //Sort BinaryGnomeSort    = new    BinaryGnomeSort(Delays, Highlights, Reads, Writes);
                    Sort CombSort           = new           CombSort(Delays, Highlights, Reads, Writes);
                    Sort CircleSort         = new         CircleSort(Delays, Highlights, Reads, Writes);
                    Sort LLQuickSort        = new        LLQuickSort(Delays, Highlights, Reads, Writes);
                    Sort LRQuickSort        = new        LRQuickSort(Delays, Highlights, Reads, Writes);
                    Sort DualPivotQuickSort = new DualPivotQuickSort(Delays, Highlights, Reads, Writes);
                    Sort StableQuickSort    = new    StableQuickSort(Delays, Highlights, Reads, Writes);

                    RunExchangeSorts.this.sortNumber = 1;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Exchange Sorts");

                    RunExchangeSorts.this.RunIndividualSort(BubbleSort,         0, array,  512, 1.5);
                    RunExchangeSorts.this.RunIndividualSort(SmartBubbleSort,    0, array,  512, 1.5);
                    RunExchangeSorts.this.RunIndividualSort(CocktailShakerSort, 0, array,  512, 1.25);
                    RunExchangeSorts.this.RunIndividualSort(OddEvenSort,        0, array,  512, 1);
                    RunExchangeSorts.this.RunIndividualSort(GnomeSort,          0, array,  128, 0.025);
                    RunExchangeSorts.this.RunIndividualSort(SmartGnomeSort,     0, array,  128, 0.025);
                    //RunExchangeSorts.this.RunIndividualSort(BinaryGnomeSort,    0, array,  128, 0.002);
                    RunExchangeSorts.this.RunIndividualSort(CombSort,           0, array, 1024, 1);
                    RunExchangeSorts.this.RunIndividualSort(CircleSort,         0, array, 1024, 1);
                    RunExchangeSorts.this.RunIndividualSort(LLQuickSort,        0, array, 2048, 1);
                    RunExchangeSorts.this.RunIndividualSort(LRQuickSort,        0, array, 2048, 1);
                    RunExchangeSorts.this.RunIndividualSort(DualPivotQuickSort, 0, array, 2048, 1);
                    RunExchangeSorts.this.RunIndividualSort(StableQuickSort,    0, array, 2048, 1);
                    
                    ArrayVisualizer.setCategory("Run Exchange Sorts");
                    ArrayVisualizer.setHeading("Done");
                    
                    ArrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Sounds.toggleSound(false);
                ArrayVisualizer.setSortingThread(null);
            }
        });
        ArrayVisualizer.runSortingThread();
    }
    
    @Override
    public synchronized void ReportAllSorts(int[] array, int current, int total) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort BubbleSort         = new         BubbleSort(Delays, Highlights, Reads, Writes);
                    Sort SmartBubbleSort    = new    SmartBubbleSort(Delays, Highlights, Reads, Writes);
                    Sort CocktailShakerSort = new CocktailShakerSort(Delays, Highlights, Reads, Writes);
                    Sort OddEvenSort        = new        OddEvenSort(Delays, Highlights, Reads, Writes);
                    Sort GnomeSort          = new          GnomeSort(Delays, Highlights, Reads, Writes);
                    Sort SmartGnomeSort     = new     SmartGnomeSort(Delays, Highlights, Reads, Writes);
                    //Sort BinaryGnomeSort    = new    BinaryGnomeSort(Delays, Highlights, Reads, Writes);
                    Sort CombSort           = new           CombSort(Delays, Highlights, Reads, Writes);
                    Sort CircleSort         = new         CircleSort(Delays, Highlights, Reads, Writes);
                    Sort LLQuickSort        = new        LLQuickSort(Delays, Highlights, Reads, Writes);
                    Sort LRQuickSort        = new        LRQuickSort(Delays, Highlights, Reads, Writes);
                    Sort DualPivotQuickSort = new DualPivotQuickSort(Delays, Highlights, Reads, Writes);
                    Sort StableQuickSort    = new    StableQuickSort(Delays, Highlights, Reads, Writes);

                    RunExchangeSorts.this.sortNumber = current;
                    RunExchangeSorts.this.sortCount = total;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Exchange Sorts");

                    RunExchangeSorts.this.RunIndividualSort(BubbleSort,         0, array,  512, 1.5);
                    RunExchangeSorts.this.RunIndividualSort(SmartBubbleSort,    0, array,  512, 1.5);
                    RunExchangeSorts.this.RunIndividualSort(CocktailShakerSort, 0, array,  512, 1.25);
                    RunExchangeSorts.this.RunIndividualSort(OddEvenSort,        0, array,  512, 1);
                    RunExchangeSorts.this.RunIndividualSort(GnomeSort,          0, array,  128, 0.025);
                    RunExchangeSorts.this.RunIndividualSort(SmartGnomeSort,     0, array,  128, 0.025);
                    //RunExchangeSorts.this.RunIndividualSort(BinaryGnomeSort,    0, array,  128, 0.002);
                    RunExchangeSorts.this.RunIndividualSort(CombSort,           0, array, 1024, 1);
                    RunExchangeSorts.this.RunIndividualSort(CircleSort,         0, array, 1024, 1);
                    RunExchangeSorts.this.RunIndividualSort(LLQuickSort,        0, array, 2048, 1.5);
                    RunExchangeSorts.this.RunIndividualSort(LRQuickSort,        0, array, 2048, 1);
                    RunExchangeSorts.this.RunIndividualSort(DualPivotQuickSort, 0, array, 2048, 1);
                    RunExchangeSorts.this.RunIndividualSort(StableQuickSort,    0, array, 2048, 1);
                    
                    ArrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Sounds.toggleSound(false);
                ArrayVisualizer.setSortingThread(null);
            }
        });
        ArrayVisualizer.runSortingThread();
    }
}