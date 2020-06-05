package threads;

import main.ArrayVisualizer;
import sorts.BinaryInsertionSort;
import sorts.InsertionSort;
import sorts.PatienceSort;
import sorts.ShellSort;
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

final public class RunInsertionSorts extends MultipleSortThread {
    private Sort InsertionSort;
    private Sort BinaryInsertionSort;
    private Sort ShellSort;
    private Sort PatienceSort;
    //private Sort TreeSort;
    
    public RunInsertionSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 4;
                       //5;
                       //TODO: Add Treesort back in when fixed
        this.categoryCount = this.sortCount;
    
        InsertionSort       = new       InsertionSort(Delays, Highlights, Reads, Writes);
        BinaryInsertionSort = new BinaryInsertionSort(Delays, Highlights, Reads, Writes);
        ShellSort           = new           ShellSort(Delays, Highlights, Reads, Writes); 
        PatienceSort        = new        PatienceSort(Delays, Highlights, Reads, Writes);
        //Sort TreeSort            = new            TreeSort(Delays, Highlights, Reads, Writes);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunInsertionSorts.this.runIndividualSort(InsertionSort,       0, array,  128, 0.005);
        RunInsertionSorts.this.runIndividualSort(BinaryInsertionSort, 0, array,  128, 0.025);
        RunInsertionSorts.this.runIndividualSort(ShellSort,           0, array,  256, 0.1);
        RunInsertionSorts.this.runIndividualSort(PatienceSort,        0, array, 2048, 1);
        //RunInsertionSorts.this.RunIndividualSort(TreeSort,            0, array, 2048, 1);
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
                        RunInsertionSorts.this.sortNumber = current;
                        RunInsertionSorts.this.sortCount = total;
                    }
                    else {
                        RunInsertionSorts.this.sortNumber = 1;
                    }
                    
                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Insertion Sorts");

                    RunInsertionSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        ArrayVisualizer.setCategory("Run Insertion Sorts");
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