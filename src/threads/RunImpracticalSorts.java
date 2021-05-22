package threads;

import main.ArrayVisualizer;
import sorts.BadSort;
import sorts.BogoSort;
import sorts.BubbleBogoSort;
import sorts.CocktailBogoSort;
import sorts.ExchangeBogoSort;
import sorts.LessBogoSort;
import sorts.SillySort;
import sorts.SlowSort;
import sorts.StoogeSort;
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

final public class RunImpracticalSorts extends MultipleSortThread {
    private Sort BadSort;
    private Sort StoogeSort;
    private Sort SillySort;
    private Sort SlowSort;
    private Sort ExchangeBogoSort;
    private Sort BubbleBogoSort;
    private Sort LessBogoSort;
    private Sort CocktailBogoSort;
    private Sort BogoSort;
    
    public RunImpracticalSorts(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.sortCount = 9;
        this.categoryCount = this.sortCount;
        
        BadSort          = new          BadSort(Delays, Highlights, Reads, Writes);
        StoogeSort       = new       StoogeSort(Delays, Highlights, Reads, Writes);
        SillySort        = new        SillySort(Delays, Highlights, Reads, Writes);
        SlowSort         = new         SlowSort(Delays, Highlights, Reads, Writes);
        ExchangeBogoSort = new ExchangeBogoSort(Delays, Highlights, Reads, Writes);
        BubbleBogoSort   = new   BubbleBogoSort(Delays, Highlights, Reads, Writes);
        LessBogoSort     = new     LessBogoSort(Delays, Highlights, Reads, Writes);
        CocktailBogoSort = new CocktailBogoSort(Delays, Highlights, Reads, Writes);
        BogoSort         = new         BogoSort(Delays, Highlights, Reads, Writes);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunImpracticalSorts.this.runIndividualSort(BadSort,          0, array, 64,  0.0075);
        RunImpracticalSorts.this.runIndividualSort(StoogeSort,       0, array, 64,  0.005);
        RunImpracticalSorts.this.runIndividualSort(SillySort,        0, array, 64, 10);
        RunImpracticalSorts.this.runIndividualSort(SlowSort,         0, array, 64, 10);
        Sounds.toggleSofterSounds(true);
        RunImpracticalSorts.this.runIndividualSort(ExchangeBogoSort, 0, array, 32,  0.01);
        RunImpracticalSorts.this.runIndividualSort(BubbleBogoSort,   0, array, 32,  0.01);
        RunImpracticalSorts.this.runIndividualSort(LessBogoSort,     0, array, 16,  0.0025);
        RunImpracticalSorts.this.runIndividualSort(CocktailBogoSort, 0, array, 16,  0.0025);
        RunImpracticalSorts.this.runIndividualSort(BogoSort,         0, array,  8,  1);
        Sounds.toggleSofterSounds(false);
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
                        RunImpracticalSorts.this.sortNumber = current;
                        RunImpracticalSorts.this.sortCount = total;
                    }
                    else {
                        RunImpracticalSorts.this.sortNumber = 1;
                    }
                    
                    ArrayManager.toggleMutableLength(false);

                    ArrayVisualizer.setCategory("Impractical Sorts");

                    RunImpracticalSorts.this.executeSortList(array);
                    
                    if(runAllActive) {
                        Thread.sleep(3000);
                    }
                    else {
                        ArrayVisualizer.setCategory("Run Impractical Sorts");
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
}