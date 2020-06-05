package threads;

import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;

import main.ArrayManager;
import main.ArrayVisualizer;
import templates.JErrorPane;
import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Sounds;
import utils.Timer;
import utils.Writes;

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

final public class RunComparisonSort {
    private ArrayManager ArrayManager;
    private ArrayVisualizer ArrayVisualizer;
    private Delays delayOps;
    private Highlights markOps;
    private Reads readOps;
    private Writes writeOps;
    private Sounds Sounds;
    private Timer realTimer;
    
    public RunComparisonSort(ArrayVisualizer ArrayVisualizer) {
        this.ArrayVisualizer = ArrayVisualizer;
        this.ArrayManager = ArrayVisualizer.getArrayManager();
        this.delayOps = ArrayVisualizer.getDelays();
        this.markOps = ArrayVisualizer.getHighlights();
        this.readOps = ArrayVisualizer.getReads();
        this.writeOps = ArrayVisualizer.getWrites();
        this.Sounds = ArrayVisualizer.getSounds();
        this.realTimer = ArrayVisualizer.getTimer();
    }
    
    public void ReportComparativeSort(int[] array, int selection) {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        //TODO: This code is bugged! It causes the program to forget the sleep ratio specified by the user!
        if(delayOps.skipped()) {
            delayOps.setSleepRatio(1);
            delayOps.changeSkipped(false);
        }

        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try {
                    Class<?> sortClass = Class.forName(ArrayVisualizer.getComparisonSorts()[0][selection]);
                    Constructor<?> newSort = sortClass.getConstructor(new Class[] {Delays.class, Highlights.class, Reads.class, Writes.class});
                    Sort sort = (Sort) newSort.newInstance(delayOps, markOps, readOps, writeOps);
                
                    boolean goAhead;
                    
                    ArrayManager.toggleMutableLength(false);
                    ArrayManager.refreshArray(array, ArrayVisualizer.getCurrentLength(), ArrayVisualizer);
                    
                    if(sort.getUnreasonablySlow() && ArrayVisualizer.getCurrentLength() >= sort.getUnreasonableLimit()) {
                        goAhead = false;
                        Object[] options = { "Let's see how bad " + sort.getReportSortID() + " is!", "Cancel" };
                        
                        if(sort.bogoSort()) {
                            int warning = JOptionPane.showOptionDialog(ArrayVisualizer.getMainWindow(), "Even at a high speed, "
                                                                     + sort.getReportSortID() + "ing " + ArrayVisualizer.getCurrentLength()
                                                                     + " numbers will almost certainly not finish in a reasonable amount of time. "
                                                                     + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                     null, options, options[1]);
                            if(warning == 0) goAhead = true;
                            else goAhead = false;
                        }
                        else {
                            int warning = JOptionPane.showOptionDialog(ArrayVisualizer.getMainWindow(), "Even at a high speed, " 
                                                                     + sort.getReportSortID() + "ing " + ArrayVisualizer.getCurrentLength()
                                                                     + " numbers will not finish in a reasonable amount of time. "
                                                                     + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                     null, options, options[1]);

                            if(warning == 0) goAhead = true;
                            else goAhead = false;
                        }
                    }
                    else {
                        goAhead = true;
                    }
                    
                    if(goAhead) {
                        ArrayVisualizer.setHeading(sort.getReportSortID());
                        ArrayVisualizer.setCategory(sort.getCategory());
                        
                        realTimer.enableRealTimer();
                        sort.runSort(array, ArrayVisualizer.getCurrentLength(), 0);
                    }
                    else {
                        ArrayManager.initializeArray(array);
                    }
                }
                catch(Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                ArrayVisualizer.endSort();
                ArrayManager.toggleMutableLength(true);
                Sounds.toggleSound(false);
            }
        });
       
        ArrayVisualizer.runSortingThread();
    }
}