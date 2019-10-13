package threads;

import main.ArrayVisualizer;
import sorts.InPlaceMergeSort;
import sorts.LazyStableSort;
import sorts.MergeSort;
import sorts.RotateMergeSort;
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

final public class RunMergeSorts extends MultipleSortThread {
    public RunMergeSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    public synchronized void ReportMergeSorts(int[] array) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort MergeSort        = new        MergeSort(Delays, Highlights, Reads, Writes);
                    Sort InPlaceMergeSort = new InPlaceMergeSort(Delays, Highlights, Reads, Writes);
                    Sort LazyStableSort   = new   LazyStableSort(Delays, Highlights, Reads, Writes);
                    Sort RotateMergeSort  = new  RotateMergeSort(Delays, Highlights, Reads, Writes);
                    
                    RunMergeSorts.this.sortNumber = 1;
                    RunMergeSorts.this.sortCount  = 4;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Merge Sorts");

                    RunMergeSorts.this.RunIndividualSort(MergeSort,        0, array,  1.75);
                    RunMergeSorts.this.RunIndividualSort(InPlaceMergeSort, 0, array,  2);
                    RunMergeSorts.this.RunIndividualSort(LazyStableSort,   0, array, 20);
                    RunMergeSorts.this.RunIndividualSort(RotateMergeSort,  0, array,  1.5);
                    
                    ArrayVisualizer.setCategory("Merge Sorts");
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
}