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
    public RunDistributionSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 13;
        this.categoryCount = this.sortCount;
    }

    public synchronized void ReportDistributionSorts(int[] array) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort CountingSort             = new             CountingSort(Delays, Highlights, Reads, Writes);
                    Sort PigeonholeSort           = new           PigeonholeSort(Delays, Highlights, Reads, Writes);
                    Sort GravitySort              = new              GravitySort(Delays, Highlights, Reads, Writes);
                    Sort AmericanFlagSort         = new         AmericanFlagSort(Delays, Highlights, Reads, Writes);
                    Sort LSDRadixSort             = new             LSDRadixSort(Delays, Highlights, Reads, Writes);
                    Sort InPlaceLSDRadixSort      = new      InPlaceLSDRadixSort(Delays, Highlights, Reads, Writes);
                    Sort MSDRadixSort             = new             MSDRadixSort(Delays, Highlights, Reads, Writes);
                    Sort FlashSort                = new                FlashSort(Delays, Highlights, Reads, Writes);
                    Sort BinaryQuickSort          = new          BinaryQuickSort(Delays, Highlights, Reads, Writes);
                    Sort RecursiveBinaryQuickSort = new RecursiveBinaryQuickSort(Delays, Highlights, Reads, Writes);
                    Sort ShatterSort              = new              ShatterSort(Delays, Highlights, Reads, Writes);
                    Sort SimpleShatterSort        = new        SimpleShatterSort(Delays, Highlights, Reads, Writes);
                    Sort TimeSort                 = new                 TimeSort(Delays, Highlights, Reads, Writes);

                    RunDistributionSorts.this.sortNumber = 1;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Distribution Sorts");

                    RunDistributionSorts.this.RunIndividualSort(CountingSort,             0, array, 2048, 1.5);
                    RunDistributionSorts.this.RunIndividualSort(PigeonholeSort,           0, array, 2048, 1.5);
                    RunDistributionSorts.this.RunIndividualSort(GravitySort,              0, array, 1024, 0.5);
                    RunDistributionSorts.this.RunIndividualSort(AmericanFlagSort,       128, array, 2048, 0.75);
                    RunDistributionSorts.this.RunIndividualSort(LSDRadixSort,             4, array, 2048, 1.5);
                    Sounds.toggleSofterSounds(true);
                    RunDistributionSorts.this.RunIndividualSort(InPlaceLSDRadixSort,     10, array, 2048, 1);
                    Sounds.toggleSofterSounds(false);
                    RunDistributionSorts.this.RunIndividualSort(MSDRadixSort,             4, array, 2048, 1.25);
                    RunDistributionSorts.this.RunIndividualSort(FlashSort,                0, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(BinaryQuickSort,          0, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(RecursiveBinaryQuickSort, 0, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(ShatterSort,            128, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(SimpleShatterSort,      128, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(TimeSort,                10, array, 2048, 1);
                    
                    ArrayVisualizer.setCategory("Run Distribution Sorts");
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
    public void ReportAllSorts(int[] array, int current, int total) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort CountingSort             = new             CountingSort(Delays, Highlights, Reads, Writes);
                    Sort PigeonholeSort           = new           PigeonholeSort(Delays, Highlights, Reads, Writes);
                    Sort GravitySort              = new              GravitySort(Delays, Highlights, Reads, Writes);
                    Sort AmericanFlagSort         = new         AmericanFlagSort(Delays, Highlights, Reads, Writes);
                    Sort LSDRadixSort             = new             LSDRadixSort(Delays, Highlights, Reads, Writes);
                    Sort InPlaceLSDRadixSort      = new      InPlaceLSDRadixSort(Delays, Highlights, Reads, Writes);
                    Sort MSDRadixSort             = new             MSDRadixSort(Delays, Highlights, Reads, Writes);
                    Sort FlashSort                = new                FlashSort(Delays, Highlights, Reads, Writes);
                    Sort BinaryQuickSort          = new          BinaryQuickSort(Delays, Highlights, Reads, Writes);
                    Sort RecursiveBinaryQuickSort = new RecursiveBinaryQuickSort(Delays, Highlights, Reads, Writes);
                    Sort ShatterSort              = new              ShatterSort(Delays, Highlights, Reads, Writes);
                    Sort SimpleShatterSort        = new        SimpleShatterSort(Delays, Highlights, Reads, Writes);
                    Sort TimeSort                 = new                 TimeSort(Delays, Highlights, Reads, Writes);

                    RunDistributionSorts.this.sortNumber = current;
                    RunDistributionSorts.this.sortCount = total;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Distribution Sorts");

                    RunDistributionSorts.this.RunIndividualSort(CountingSort,             0, array, 2048, 1.5);
                    RunDistributionSorts.this.RunIndividualSort(PigeonholeSort,           0, array, 2048, 1.5);
                    RunDistributionSorts.this.RunIndividualSort(GravitySort,              0, array, 1024, 0.5);
                    RunDistributionSorts.this.RunIndividualSort(AmericanFlagSort,       128, array, 2048, 0.75);
                    RunDistributionSorts.this.RunIndividualSort(LSDRadixSort,             4, array, 2048, 1.5);
                    Sounds.toggleSofterSounds(true);
                    RunDistributionSorts.this.RunIndividualSort(InPlaceLSDRadixSort,     10, array, 2048, 1);
                    Sounds.toggleSofterSounds(false);
                    RunDistributionSorts.this.RunIndividualSort(MSDRadixSort,             4, array, 2048, 1.25);
                    RunDistributionSorts.this.RunIndividualSort(FlashSort,                0, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(BinaryQuickSort,          0, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(RecursiveBinaryQuickSort, 0, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(ShatterSort,            128, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(SimpleShatterSort,      128, array, 2048, 1);
                    RunDistributionSorts.this.RunIndividualSort(TimeSort,                10, array, 2048, 1);
                    
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