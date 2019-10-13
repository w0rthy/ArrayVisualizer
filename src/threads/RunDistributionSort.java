package threads;

import java.awt.HeadlessException;
import java.lang.reflect.Constructor;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import main.ArrayManager;
import main.ArrayVisualizer;
import sorts.FlashSort;
import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Shuffles;
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

// Many thanks to Freek de Bruijn on StackOverflow for providing a custom JOptionPane.
// https://stackoverflow.com/questions/14407804/how-to-change-the-default-text-of-buttons-in-joptionpane-showinputdialog?noredirect=1&lq=1
final class JEnhancedOptionPane extends JOptionPane {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String showInputDialog(final Object message, final Object[] options) throws HeadlessException {
        final JOptionPane pane = new JOptionPane(message, QUESTION_MESSAGE,
                                                 OK_CANCEL_OPTION, null,
                                                 options, null);
        pane.setWantsInput(true);
        pane.setComponentOrientation((getRootFrame()).getComponentOrientation());
        pane.setMessageType(QUESTION_MESSAGE);
        pane.selectInitialValue();
        final String title = UIManager.getString("OptionPane.inputDialogTitle", null);
        final JDialog dialog = pane.createDialog(null, title);
        dialog.setVisible(true);
        dialog.dispose();
        final Object value = pane.getInputValue();
        return (value == UNINITIALIZED_VALUE) ? null : (String) value;
    }
}

final public class RunDistributionSort {
    private ArrayManager ArrayManager;
    private ArrayVisualizer ArrayVisualizer;
    private Delays delayOps;
    private Highlights markOps;
    private Reads readOps;
    private Writes writeOps;
    private Sounds Sounds;
    private Timer realTimer;
    
    public RunDistributionSort(ArrayVisualizer ArrayVisualizer) {
        this.ArrayVisualizer = ArrayVisualizer;
        this.ArrayManager = ArrayVisualizer.getArrayManager();
        this.delayOps = ArrayVisualizer.getDelays();
        this.markOps = ArrayVisualizer.getHighlights();
        this.readOps = ArrayVisualizer.getReads();
        this.writeOps = ArrayVisualizer.getWrites();
        this.Sounds = ArrayVisualizer.getSounds();
        this.realTimer = ArrayVisualizer.getTimer();
    }
    
    private String getTimeSortEstimate(int bucketCount) {
        String timeString = "";
        String timeUnit;
        
        int seconds = Math.max(((ArrayVisualizer.getCurrentLength() * bucketCount) / 1000), 1);
        int minutes;
        int hours;
        long days;
        
        if(seconds >= 60) {
            minutes = Math.round(seconds / 60);
            
            if(minutes >= 60) {
                hours = Math.round(minutes / 60);
                
                if(hours >= 24) {
                    days = Math.round(hours / 24);
                    
                    if(days < 2)  timeUnit = "day";
                    else          timeUnit = "days";
                    
                    timeString = "" + ArrayVisualizer.getNumberFormat().format(days) + " " + timeUnit + " ";
                }
                else {
                    if(hours < 2) timeUnit = "hour";
                    else          timeUnit = "hours";
                    
                    timeString = "" + hours + " " + timeUnit + " ";
                }
            }
            else {
                if(minutes < 2) timeUnit = "minute";
                else            timeUnit = "minutes";
                
                timeString = "" + minutes + " " + timeUnit + " ";
            }
        }
        else {
            if(seconds < 2) timeUnit = "second";
            else            timeUnit = "seconds";
            
            timeString = "" + seconds + " " + timeUnit + " ";
        }
        
        return timeString;
    }
    
    public void ReportDistributiveSort(int[] array, int selection) {
        if(ArrayVisualizer.getSortingThread() != null && ArrayVisualizer.getSortingThread().isAlive())
            return;

        if(delayOps.skipped()) {
            delayOps.setSleepRatio(1);
            delayOps.changeSkipped(false);
        }

        double storeVol = Sounds.getVolume();
        
        ArrayVisualizer.setCategory("Distributive Sorts");
        
        Sounds.toggleSound(true);
        ArrayVisualizer.setSortingThread(new Thread() {
            @SuppressWarnings("unused")
            @Override
            public void run(){
                try {
                    Class<?> sortClass = Class.forName(ArrayVisualizer.getDistributionSorts()[0][selection]);
                    Constructor<?> newSort = sortClass.getConstructor(new Class[] {Delays.class, Highlights.class, Reads.class, Writes.class});
                    Sort sort = (Sort) newSort.newInstance(delayOps, markOps, readOps, writeOps);

                    int bucketCount;
                    
                    if(sort.getReportSortID().equals("Timesort")) {
                        try {
                            bucketCount = (int) Math.ceil(Double.parseDouble(JEnhancedOptionPane.showInputDialog("Enter delay per number in milliseconds:", new Object[]{"Enter", "Use default"})));
                        }
                        catch(Exception e) {
                            bucketCount = 10;
                        }
                    }
                    else {
                        if(sort.usesBuckets()) {
                            if(sort.radixSort()) {
                                try {
                                    bucketCount = Integer.parseInt(JEnhancedOptionPane.showInputDialog("Enter the base for this sort:", new Object[]{"Enter", "Use default"}));
                                }
                                catch(Exception e) {
                                    bucketCount = 4;
                                }   
                            }
                            else if(sort.getReportSortID().equals("Shatter Sort") || sort.getReportSortID().equals("Simple Shatter Sort")) {
                                // Ugh. Not happy with this patch at all. For some reason, Shatter Sort does not work
                                // for all distributions of data unless you only have two partitions/"shatters" **presumably**.
                                
                                // Specifically, an array that doesn't hold many unique values breaks Shatter Sort.
                                if(ArrayManager.getShuffle().equals(Shuffles.SIMILAR)) {
                                    bucketCount = 2;
                                }
                                else {
                                    try {
                                        bucketCount = Integer.parseInt(JEnhancedOptionPane.showInputDialog("Enter the size for each partition:", new Object[]{"Enter", "Use default"}));
                                    }
                                    catch(Exception e) {
                                        bucketCount = ArrayVisualizer.getCurrentLength() / 16;
                                    }
                                }
                            }
                            else {
                                try {
                                    bucketCount = Integer.parseInt(JEnhancedOptionPane.showInputDialog("How many buckets will this sort use?", new Object[]{"Enter", "Use default"}));
                                }
                                catch(Exception e) {
                                    bucketCount = 16;
                                }
                            }
                            
                            if(bucketCount < 2) bucketCount = 2;
                        }
                        else {
                            bucketCount = 0;
                        }
                    }
                    
                    ArrayManager.toggleMutableLength(false);
                    ArrayManager.refreshArray(array, ArrayVisualizer.getCurrentLength(), ArrayVisualizer);
                
                    boolean goAhead;
                    
                    if(sort.getUnreasonablySlow() && ArrayVisualizer.getCurrentLength() >= sort.getUnreasonableLimit()) {
                        goAhead = false;
                       
                        if(sort.getReportSortID().equals("Timesort")) {
                            Object[] options = { "Continue", "Cancel" };

                            int warning = JOptionPane.showOptionDialog(ArrayVisualizer.getMainWindow(), "Time Sort will take at least " + getTimeSortEstimate(bucketCount)
                                                                     + "to complete. Once it starts, you cannot skip this sort.", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                     null, options, options[1]);

                            if(warning == 0) goAhead = true;
                            else goAhead = false;

                        }
                        else {
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
                                //Currently, no distribution sort calls this message. It's here if you want to include a sort that might use it in the future.
                                int warning = JOptionPane.showOptionDialog(ArrayVisualizer.getMainWindow(), "Even at a high speed, " 
                                                                         + sort.getReportSortID() + "ing " + ArrayVisualizer.getCurrentLength()
                                                                         + " numbers will not finish in a reasonable amount of time. "
                                                                         + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                         null, options, options[1]);

                                if(warning == 0) goAhead = true;
                                else goAhead = false;
                            }
                        }
                    }
                    else {
                        goAhead = true;
                    }
                    
                    // Patch for Flashsort, fixes distribution for few unique numbers
                    if(sort.getReportSortID().equals("Flashsort")) {
                        ((FlashSort) sort).setCurrentShuffle(ArrayManager.getShuffle());
                    }
                    
                    if(sort.getReportSortID().equals("In-Place LSD Radix")) {
                        Sounds.changeVolume(0.01); // Here to protect your ears :)
                    }
                    
                    if(goAhead) {
                        ArrayVisualizer.setHeading(sort.getReportSortID());
                        
                        realTimer.enableRealTimer();
                        sort.runSort(array, ArrayVisualizer.getCurrentLength(), bucketCount);
                    }
                    else {
                        ArrayManager.initializeArray(array);
                    }
                    
                    ArrayVisualizer.endSort();
                    ArrayManager.toggleMutableLength(true);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                Sounds.changeVolume(storeVol);
                Sounds.toggleSound(false);
            }
        });
        
        ArrayVisualizer.runSortingThread();
    }
}