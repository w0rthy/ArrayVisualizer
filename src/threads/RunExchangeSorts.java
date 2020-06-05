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
import sorts.SmartCocktailSort;
import sorts.SmartGnomeSort;
import sorts.StableQuickSort;
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

final public class RunExchangeSorts extends MultipleSortThread {
    private Sort BubbleSort;
    private Sort SmartBubbleSort;
    private Sort CocktailShakerSort;
    private Sort SmartCocktailSort;
    private Sort OddEvenSort;
    private Sort GnomeSort;
    private Sort SmartGnomeSort;
    private Sort BinaryGnomeSort;
    private Sort CombSort;
    private Sort CircleSort;
    private Sort LLQuickSort;
    private Sort LRQuickSort;
    private Sort DualPivotQuickSort;
    private Sort StableQuickSort;
    
    public RunExchangeSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 14;
        this.categoryCount = this.sortCount;
        
        BubbleSort         = new         BubbleSort(Delays, Highlights, Reads, Writes);
        SmartBubbleSort    = new    SmartBubbleSort(Delays, Highlights, Reads, Writes);
        CocktailShakerSort = new CocktailShakerSort(Delays, Highlights, Reads, Writes);
        SmartCocktailSort  = new  SmartCocktailSort(Delays, Highlights, Reads, Writes);
        OddEvenSort        = new        OddEvenSort(Delays, Highlights, Reads, Writes);
        GnomeSort          = new          GnomeSort(Delays, Highlights, Reads, Writes);
        SmartGnomeSort     = new     SmartGnomeSort(Delays, Highlights, Reads, Writes);
        BinaryGnomeSort    = new    BinaryGnomeSort(Delays, Highlights, Reads, Writes);
        CombSort           = new           CombSort(Delays, Highlights, Reads, Writes);
        CircleSort         = new         CircleSort(Delays, Highlights, Reads, Writes);
        LLQuickSort        = new        LLQuickSort(Delays, Highlights, Reads, Writes);
        LRQuickSort        = new        LRQuickSort(Delays, Highlights, Reads, Writes);
        DualPivotQuickSort = new DualPivotQuickSort(Delays, Highlights, Reads, Writes);
        StableQuickSort    = new    StableQuickSort(Delays, Highlights, Reads, Writes);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunExchangeSorts.this.runIndividualSort(BubbleSort,         0, array,  512, 1.5);
        RunExchangeSorts.this.runIndividualSort(SmartBubbleSort,    0, array,  512, 1.5);
        RunExchangeSorts.this.runIndividualSort(CocktailShakerSort, 0, array,  512, 1.25);
        RunExchangeSorts.this.runIndividualSort(SmartCocktailSort,  0, array,  512, 1.25);
        RunExchangeSorts.this.runIndividualSort(OddEvenSort,        0, array,  512, 1);
        RunExchangeSorts.this.runIndividualSort(GnomeSort,          0, array,  128, 0.025);
        RunExchangeSorts.this.runIndividualSort(SmartGnomeSort,     0, array,  128, 0.025);
        RunExchangeSorts.this.runIndividualSort(BinaryGnomeSort,    0, array,  128, 0.025);
        RunExchangeSorts.this.runIndividualSort(CombSort,           0, array, 1024, 1);
        RunExchangeSorts.this.runIndividualSort(CircleSort,         0, array, 1024, 1);
        RunExchangeSorts.this.runIndividualSort(LLQuickSort,        0, array, 2048, ArrayManager.getShuffle() == Shuffles.RANDOM ? 1.5 : 65);
        RunExchangeSorts.this.runIndividualSort(LRQuickSort,        0, array, 2048, 1);
        RunExchangeSorts.this.runIndividualSort(DualPivotQuickSort, 0, array, 2048, ArrayManager.getShuffle() == Shuffles.RANDOM ? 1 : 50);
        RunExchangeSorts.this.runIndividualSort(StableQuickSort,    0, array, 2048, ArrayManager.getShuffle() == Shuffles.RANDOM ? 1 : 50);
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
                        RunExchangeSorts.this.sortNumber = current;
                        RunExchangeSorts.this.sortCount = total;
                    }
                    else {
                        RunExchangeSorts.this.sortNumber = 1;
                    }
                    
                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Exchange Sorts");

                    RunExchangeSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        ArrayVisualizer.setCategory("Run Exchange Sorts");
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