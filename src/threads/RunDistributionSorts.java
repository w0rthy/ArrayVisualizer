package threads;

import main.ArrayVisualizer;
import sorts.AmericanFlagSort;
import sorts.BinaryQuickSort;
import sorts.CountingSort;
import sorts.FlashSort;
import sorts.GravitySort;
import sorts.InPlaceLSDRadixSort;
import sorts.LSDRadixSort;
import sorts.MSDRadixSort;
import sorts.PigeonholeSort;
import sorts.RecursiveBinaryQuickSort;
import sorts.ShatterSort;
import sorts.SimpleShatterSort;
import sorts.TimeSort;
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

final public class RunDistributionSorts extends MultipleSortThread {
    private Sort CountingSort;
    private Sort PigeonholeSort;
    private Sort GravitySort;
    private Sort AmericanFlagSort;
    private Sort LSDRadixSort;
    private Sort InPlaceLSDRadixSort;
    private Sort MSDRadixSort;
    private Sort FlashSort;
    private Sort BinaryQuickSort;
    private Sort RecursiveBinaryQuickSort;
    private Sort ShatterSort;
    private Sort SimpleShatterSort;
    private Sort TimeSort;
    
    public RunDistributionSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 13;
        this.categoryCount = this.sortCount;
        
        CountingSort             = new             CountingSort(Delays, Highlights, Reads, Writes);
        PigeonholeSort           = new           PigeonholeSort(Delays, Highlights, Reads, Writes);
        GravitySort              = new              GravitySort(Delays, Highlights, Reads, Writes);
        AmericanFlagSort         = new         AmericanFlagSort(Delays, Highlights, Reads, Writes);
        LSDRadixSort             = new             LSDRadixSort(Delays, Highlights, Reads, Writes);
        InPlaceLSDRadixSort      = new      InPlaceLSDRadixSort(Delays, Highlights, Reads, Writes);
        MSDRadixSort             = new             MSDRadixSort(Delays, Highlights, Reads, Writes);
        FlashSort                = new                FlashSort(Delays, Highlights, Reads, Writes);
        BinaryQuickSort          = new          BinaryQuickSort(Delays, Highlights, Reads, Writes);
        RecursiveBinaryQuickSort = new RecursiveBinaryQuickSort(Delays, Highlights, Reads, Writes);
        ShatterSort              = new              ShatterSort(Delays, Highlights, Reads, Writes);
        SimpleShatterSort        = new        SimpleShatterSort(Delays, Highlights, Reads, Writes);
        TimeSort                 = new                 TimeSort(Delays, Highlights, Reads, Writes);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunDistributionSorts.this.runIndividualSort(CountingSort,             0, array, 2048, 1.5);
        RunDistributionSorts.this.runIndividualSort(PigeonholeSort,           0, array, 2048, 1.5);
        RunDistributionSorts.this.runIndividualSort(GravitySort,              0, array, 1024, 0.5);
        RunDistributionSorts.this.runIndividualSort(AmericanFlagSort,       128, array, 2048, 0.75);
        RunDistributionSorts.this.runIndividualSort(LSDRadixSort,             4, array, 2048, 1.5);
        Sounds.toggleSofterSounds(true);
        RunDistributionSorts.this.runIndividualSort(InPlaceLSDRadixSort,     10, array, 2048, 1);
        Sounds.toggleSofterSounds(false);
        RunDistributionSorts.this.runIndividualSort(MSDRadixSort,             4, array, 2048, 1.25);
        RunDistributionSorts.this.runIndividualSort(FlashSort,                0, array, 2048, 1);
        RunDistributionSorts.this.runIndividualSort(BinaryQuickSort,          0, array, 2048, 1);
        RunDistributionSorts.this.runIndividualSort(RecursiveBinaryQuickSort, 0, array, 2048, 1);
        RunDistributionSorts.this.runIndividualSort(ShatterSort,            128, array, 2048, 1);
        RunDistributionSorts.this.runIndividualSort(SimpleShatterSort,      128, array, 2048, 1);
        RunDistributionSorts.this.runIndividualSort(TimeSort,                10, array, 2048, 1);
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
                        RunDistributionSorts.this.sortNumber = current;
                        RunDistributionSorts.this.sortCount = total;
                    }
                    else {
                        RunDistributionSorts.this.sortNumber = 1;
                    }
                    
                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Distribution Sorts");

                    RunDistributionSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        ArrayVisualizer.setCategory("Run Distribution Sorts");
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