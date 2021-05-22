package threads;

import java.util.ArrayList;

import main.ArrayVisualizer;
import templates.JErrorPane;
import templates.MultipleSortThread;

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

final public class RunAllSorts {
    private ArrayVisualizer ArrayVisualizer;
    private ArrayList<MultipleSortThread> allSortThreads;
    
    public RunAllSorts(ArrayVisualizer ArrayVisualizer) {
        this.ArrayVisualizer = ArrayVisualizer;
        this.allSortThreads = new ArrayList<>();
        this.allSortThreads.add(new RunExchangeSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunSelectionSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunInsertionSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunMergeSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunDistributionSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunConcurrentSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunHybridSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunMiscellaneousSorts(ArrayVisualizer));
        this.allSortThreads.add(new RunImpracticalSorts(ArrayVisualizer));
    }

    public void reportAllSorts(int[] array) throws Exception {
        int totalSortCount = 0;
        for(MultipleSortThread category : this.allSortThreads) {
            totalSortCount += category.getSortCount();
        }
        
        try {
            int currentSort = 1;
            for(MultipleSortThread thread : this.allSortThreads) {
                thread.reportAllSorts(array, currentSort, totalSortCount);
                this.ArrayVisualizer.getSortingThread().join();
                currentSort += thread.getCategoryCount();
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
        
        this.ArrayVisualizer.setCategory("Run All Sorts");
        this.ArrayVisualizer.setHeading("Finished!!");
    }
}