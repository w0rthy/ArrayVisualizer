package threads;

import main.ArrayVisualizer;
import sorts.PancakeSort;
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

final public class RunMiscellaneousSorts extends MultipleSortThread {
    public RunMiscellaneousSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 1;
        this.categoryCount = this.sortCount;
    }

    public synchronized void ReportMiscellaneousSorts(int[] array) throws Exception {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    Sort PancakeSort = new PancakeSort(Delays, Highlights, Reads, Writes);
                    
                    RunMiscellaneousSorts.this.sortNumber = 1;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Miscellaneous Sorts");

                    RunMiscellaneousSorts.this.RunIndividualSort(PancakeSort, 0, array, 128, 0.015);
                    
                    ArrayVisualizer.setCategory("Run Miscellaneous Sorts");
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
                    Sort PancakeSort = new PancakeSort(Delays, Highlights, Reads, Writes);
                    
                    RunMiscellaneousSorts.this.sortNumber = current;
                    RunMiscellaneousSorts.this.sortCount = total;

                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Miscellaneous Sorts");

                    RunMiscellaneousSorts.this.RunIndividualSort(PancakeSort, 0, array, 128, 0.015);
                    
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