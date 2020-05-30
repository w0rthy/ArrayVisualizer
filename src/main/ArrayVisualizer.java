package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Window;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import frames.ArrayFrame;
import frames.UtilFrame;
import utils.Delays;
import utils.Renderer;
import utils.Highlights;
import utils.Reads;
import utils.Sounds;
import utils.Timer;
import utils.Writes;
import visuals.VisualStyles;

/* TO-DO LIST:
 * - JAVA DOCS FOR EVERYTHING
 * - FINALIZE CLASSES/METHODS (already finished?)
 * - CLEAN UP GETTERS AND SETTERS
 * - CLEAN UP METHOD PARAMETERS
 * - SHOW/HIDE BASIC SORTS, ADVANCED SORTS, AND OBSCURE SORTS
 * - GIVE COMPARE METHODS DELAY AND MARK ARGUMENTS
 * - Implement:
 * - - Entering in your own set of data
 * - - Pass ArrayVisualizer as "observer object" to sorts
 * - - Pass formatter/symbols from ArrayVisualizer into utils
 * - Fix:
 * - - Circular pointer
 * - - Combsort / Radix Sorts not complying with "Skip Sort"
 * - - "Skip Sort" / changing array size saving previous speed
 * - - .dls file in soundfont dir
 * - - 1080p in OBS(?)
 * - Create:
 * - - Better code design for ViewPrompt
 * - - options to choose comb gap
 * - - options to choose shell gap
 * - - options to choose timsort minrun
 * - - subheadings for sort customizations (e.g. number of buckets for all bucket sorts)
 * - - toggle between pointer and black bar
 * - - SINE WAVE VISUAL!!!
 * - Make:
 * - - "Many Similar" distribution ((i / 5) * 5, as an example)
 * - - Better names/clean up code in frames/prompts
 * - - Better names for variables
 * - - Analysis color for all visuals
 * - - Pipe Organ (ascend -> descend) distribution
 * - - option for pre-shuffled array
 * - - option to stop run all
 * - - sort list categories
 * - - run all sorts in specific category
 * - - option for custom parts for intro sorts
 * - - option for simple shatter rate???
 * - Finish:
 * - - SkaSort
 * - - HolyGrailSort
 * - - Run All Sorts in Category
 * - Cleanup:
 * - - Treesort
 */

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

/*
 * 
The MIT License (MIT)

Copyright (c) 2019 Luke Hutchison

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

final public class ArrayVisualizer {
    final JFrame window = new JFrame();
    
    final boolean OBS = false; // Change to true if you want 1080p for recording with OBS
    
    final private int MIN_ARRAY_VAL = 2;
    final private int MAX_ARRAY_VAL = 16384;

    final int[] array = new int[this.MAX_ARRAY_VAL];
    
    private String[][] ComparisonSorts;     // First row of Comparison/DistributionSorts arrays consists of class names
    private String[][] DistributionSorts;   // Second row consists of user-friendly names
    private String[] InvalidSorts;
    
    private volatile int currentLen;
    private volatile int equalItems;
    
    private ArrayManager ArrayManager;
    private SortAnalyzer SortAnalyzer;
    
    private UtilFrame UtilFrame;
    private ArrayFrame ArrayFrame;
    
    private Thread sortingThread;
    private Thread visualsThread;
    
    private String category;
    private String heading;
    private Font typeFace;
    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;

    private volatile int currentGap;
    
    private boolean SHUFFLEANIM;

    private volatile boolean ANALYZE;

    private volatile boolean POINTER;
    
    private volatile boolean TEXTDRAW;
    private volatile boolean COLOR;
    private volatile boolean DISPARITYDRAW;
    private volatile boolean LINEDRAW;
    private volatile boolean PIXELDRAW;
    private volatile boolean RAINBOW;
    private volatile boolean SPIRALDRAW;
    
    private volatile int cx;
    private volatile int cy;
    private volatile int ch;
    private volatile int cw;
    
    private Image img;
    private Graphics2D mainRender;
    private Graphics2D extraRender;
    private Stroke thickStroke;
    
    private Delays Delays;
    private Highlights Highlights;
    private Reads Reads;
    private Renderer Renderer;
    private Sounds Sounds;
    private Timer Timer;
    private VisualStyles VisualStyles;
    private Writes Writes;

    public ArrayVisualizer() {
        this.currentLen = 2048;
        this.equalItems = 1;
        
        this.Highlights = new Highlights(this, this.MAX_ARRAY_VAL);
        this.Sounds = new Sounds(this.array, this);
        this.Delays = new Delays(this);
        this.Timer = new Timer();
        this.Reads = new Reads(this);
        this.Renderer = new Renderer(this);
        this.Writes = new Writes(this);
        
        this.ArrayManager = new ArrayManager(this);
        this.SortAnalyzer = new SortAnalyzer(this);
        
        SortAnalyzer.analyzeSorts();
        this.ComparisonSorts = SortAnalyzer.getComparisonSorts();
        this.DistributionSorts = SortAnalyzer.getDistributionSorts();
        this.InvalidSorts = SortAnalyzer.getInvalidSorts();
        
        this.category = "";
        this.heading = "";
        this.typeFace = new Font("Times New Roman", Font.PLAIN, (int) (640 / (1280.0 * 25)));
        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        this.symbols = formatter.getDecimalFormatSymbols();
        
        this.formatter.setRoundingMode(RoundingMode.HALF_UP);
        
        this.UtilFrame = new UtilFrame(this.array, this);
        this.ArrayFrame = new ArrayFrame(this.array, this);
       
        UtilFrame.reposition(this.ArrayFrame);
        
        this.SHUFFLEANIM = true;
        this.ANALYZE = false;
        this.POINTER = false;
        this.TEXTDRAW = true;
        
        this.COLOR = false;
        this.DISPARITYDRAW = false;
        this.LINEDRAW = false;
        this.PIXELDRAW = false;
        this.RAINBOW = false;
        this.SPIRALDRAW = false;
 
        this.cx = 0;
        this.cy = 0;
        this.ch = 0;
        this.cw = 0;

        ArrayManager.initializeArray(this.array);
        
        //TODO: Overhaul visual code to properly reflect Swing (JavaFX?) style and conventions
        
        //DRAW THREAD
        this.visualsThread = new Thread() {
            @Override
            public void run() {
                Renderer.initializeVisuals(ArrayVisualizer.this, ArrayVisualizer.this.VisualStyles);
                
                Graphics background = window.getGraphics();
                background.setColor(Color.BLACK);
                int coltmp = 255;
                
                while(true) {
                    Renderer.updateVisuals(ArrayVisualizer.this);
                    Renderer.drawVisual(ArrayVisualizer.this.VisualStyles, array, ArrayVisualizer.this, mainRender, extraRender, Highlights);
                    
                    mainRender.setColor(new Color(coltmp,coltmp,coltmp));
                    if(TEXTDRAW) {
                        Font f = mainRender.getFont();
                        mainRender.setFont(typeFace);
                        mainRender.drawString(category + ": " + heading, 15, (int)(cw/1280.0*30)+30);
                        mainRender.drawString(formatter.format(currentLen) + " Numbers", 15, (int)(cw/1280.0*55)+30);
                        mainRender.drawString(String.format("Delay: " + Delays.displayCurrentDelay() + "ms"), 15, (int)(cw/1280.0*95)+30);
                        mainRender.drawString(String.format("Visual Time: " + Timer.getVisualTime()), 15, (int)(cw/1280.0*120)+30);
                        mainRender.drawString(String.format("Sort Time: " + Timer.getRealTime()), 15, (int)(cw/1280.0*145)+30);
                        mainRender.drawString(Reads.displayComparisons(), 15, (int)(cw/1280.0*185)+30);
                        mainRender.drawString(Writes.getSwaps(), 15, (int)(cw/1280.0*210)+30);
                        mainRender.drawString(Writes.getReversals(), 15, (int)(cw/1280.0*235)+30);
                        mainRender.drawString(Writes.getWrites(), 15, (int)(cw/1280.0*275)+30);
                        mainRender.drawString(Writes.getTempWrites(), 15, (int)(cw/1280.0*300)+30);
                        mainRender.setFont(f);
                    }
                    background.drawImage(img, 0, 0, null);
                }}};
        
        Sounds.startAudioThread();
        this.drawWindows();
    }
    
    public ArrayManager getArrayManager() {
        return this.ArrayManager;
    }
    public Delays getDelays() {
        return this.Delays;
    }
    public Highlights getHighlights() {
        return this.Highlights;
    }
    public Reads getReads() {
        return this.Reads;
    }
    public Renderer getRender() {
        return this.Renderer;
    }
    public Sounds getSounds() {
        return this.Sounds;
    }
    public Timer getTimer() {
        return this.Timer;
    }
    public VisualStyles getVisualStyles() {
        return this.VisualStyles;
    }
    public Writes getWrites() {
        return this.Writes;
    }
    
    public UtilFrame getUtilFrame() {
        return this.UtilFrame;
    }
    
    public String[][] getComparisonSorts() {
        return this.ComparisonSorts;
    }
    public String[][] getDistributionSorts() {
        return this.DistributionSorts;
    }
    
    public Thread getSortingThread() {
        return this.sortingThread;
    }
    public void setSortingThread(Thread thread) {
        this.sortingThread = thread;
    }
    public void runSortingThread() {
        sortingThread.start();
    }
    
    public int getMinimumLength() {
        return this.MIN_ARRAY_VAL;
    }
    public int getMaximumLength() {
        return this.MAX_ARRAY_VAL;
    }
    
    public void resetAllStatistics() {
        Reads.resetStatistics();
        Writes.resetStatistics();
        Timer.manualSetTime(0);
    }
    
    // These next five methods should be part of ArrayManager
    public int getCurrentLength() {
        return this.currentLen;
    }
    public void setCurrentLength(int newLength) {
        this.currentLen = newLength;
    }
    
    public int getEqualItems() {
        return this.equalItems;
    }
    public void setEqualItems(int newCount) {
        this.equalItems = newCount;
    }
    
    public int getLogBaseTwoOfLength() {
        return (int) (Math.log(this.currentLen) / Math.log(2)); 
    }
    
    public boolean shuffleEnabled() {
        return this.SHUFFLEANIM;
    }
    public void toggleShuffleAnimation(boolean Bool) {
        this.SHUFFLEANIM = Bool;
    }
    
    public String getHeading() {
        return this.heading;
    }
    public void setHeading(String text) {
        this.heading = text;
    }
    public void setCategory(String text) {
        this.category = text;
    }

    public boolean pointerActive() {
        return this.POINTER;
    }
    
    public JFrame getMainWindow() {
        return this.window;
    }
    
    public void setWindowHeight() {
        this.ch = window.getHeight();
    }
    public void setWindowWidth() {
        this.cw = window.getWidth();
    }
    
    // TODO:
    // CURRENT HEIGHT/WIDTH/X/Y SHOULD CORRESPOND TO "C" VARIABLES
    // AND WINDOW HEIGHT/WIDTH/X/Y SHOULD CORRESPOND TO WINDOW FIELD
    
    public int currentHeight() {
        return window.getHeight();
    }
    public int currentWidth() {
        return window.getWidth();
    }
    public int currentX() {
        return window.getX();
    }
    public int currentY() {
        return window.getY();
    }
    
    public int windowHeight() {
        return this.ch;
    }
    public int windowWidth() {
        return this.cw;
    }
    public int windowHalfHeight() {
        return (this.ch / 2);
    }
    public int windowHalfWidth() {
        return (this.cw / 2);
    }
    public int windowXCoordinate() {
        return this.cx;
    }
    public int windowYCoordinate() {
        return this.cy;
    }
    
    public void createVolatileImage() {
        this.img = window.createVolatileImage(cw, ch);
    }
    public void setThickStroke(Stroke stroke) {
        this.thickStroke = stroke;
    }
    public Stroke getThickStroke() {
        return this.thickStroke;
    }
    public Stroke getDefaultStroke() {
        return new BasicStroke(3f * (this.currentWidth() / 1280f));
    }
    public void setMainRender() {
        this.mainRender = (Graphics2D) img.getGraphics();
    }
    public void setExtraRender() {
        this.extraRender = (Graphics2D) img.getGraphics();
    }
    public void resetMainStroke() {
        mainRender.setStroke(this.getDefaultStroke());
    }
    
    public void renderBackground() {
        mainRender.setColor(new Color(0, 0, 0)); // Pure black
        mainRender.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
    }
    
    public void updateCoordinates() {
        this.cx = window.getX();
        this.cy = window.getY();
    }
    public void updateDimensions() {
        this.cw = window.getWidth();
        this.ch = window.getHeight();
    }
    public void updateFontSize() {
        this.typeFace = new Font("Times New Roman",Font.PLAIN,(int)(this.cw/1280.0*25));
    }
    
    public void toggleAnalysis(boolean Bool) {
        this.ANALYZE = Bool;
    }
    public boolean analysisEnabled() {
        return this.ANALYZE;
    }
    
    public int halfCircle() {
        return (this.currentLen / 2);
    }
    
    public synchronized void verifySortAndSweep() {
        Highlights.toggleFancyFinish(true);
        Highlights.resetFancyFinish();

        Delays.setSleepRatio(1);

        double sleepRatio = 0;
        
        switch(this.getLogBaseTwoOfLength()) {
        case 14: sleepRatio =  0.25; break;
        case 13: sleepRatio =   0.5; break;
        case 12: sleepRatio =     1; break;
        case 11: sleepRatio =     2; break;
        case 10: sleepRatio =     4; break;
        case 9:  sleepRatio =     6; break;
        case 8:  sleepRatio =     8; break;
        case 7:  sleepRatio =    16; break;
        case 6:  sleepRatio =    24; break;
        case 5:
        case 4:  sleepRatio =   32; break;
        case 3:
        case 2:
        default: sleepRatio = 64;
        }
        
        long tempComps = Reads.getComparisons();
        Reads.setComparisons(0);
        
        String temp = this.heading;
        this.heading = "Verifying sort...";
        
        for(int i = 0; i < this.currentLen + this.getLogBaseTwoOfLength(); i++) {
            if(i < this.currentLen) Highlights.markArray(1, i);
            Highlights.incrementFancyFinishPosition();
            
            if(i < this.currentLen - 1) {
                if(Reads.compare(array[i], array[i + 1]) == 1) {
                    Highlights.clearMark(1);
                    
                    Sounds.toggleSound(false);
                    Highlights.toggleFancyFinish(false);
                    
                    for(int j = i + 1; j < this.currentLen; j++) {
                        Highlights.markArray(j, j);
                        Delays.sleep(sleepRatio / this.getLogBaseTwoOfLength());
                    }
                    
                    JOptionPane.showMessageDialog(window, "The sort was unsuccessful;\nIndices " + i + " and " + (i + 1) + " are out of order!", "Error", JOptionPane.OK_OPTION, null);
                    
                    Highlights.clearAllMarks();
                    
                    i = this.currentLen + this.getLogBaseTwoOfLength();
                    
                    Sounds.toggleSound(true);
                }
            }
            
            if(Highlights.fancyFinishEnabled()) {
                Delays.sleep(sleepRatio / this.getLogBaseTwoOfLength());
            }
        }
        Highlights.clearMark(1);

        this.heading = temp;
        Reads.setComparisons(tempComps);
        
        if(Highlights.fancyFinishActive()) {
            Highlights.toggleFancyFinish(false);
        }
        Highlights.resetFancyFinish();
    }

    public void endSort() {
        Timer.disableRealTimer();
        Highlights.clearAllMarks();

        double speed = Delays.getSleepRatio(); 
        this.verifySortAndSweep();
        Delays.setSleepRatio(speed);
        Delays.changeSkipped(false);

        Highlights.clearAllMarks();
    }
    
    public void togglePointer(boolean Bool) {
        this.POINTER = Bool;
    }
    public void toggleDistance(boolean Bool) {
        this.DISPARITYDRAW = Bool;
    }
    public void togglePixels(boolean Bool) {
        this.PIXELDRAW = Bool;
    }
    public void toggleRainbow(boolean Bool) {
        this.RAINBOW = Bool;
    }
    public void toggleSpiral(boolean Bool) {
        this.SPIRALDRAW = Bool;
    }
    public void toggleLinkedDots(boolean Bool) {
        this.LINEDRAW = Bool;
    }
    public void toggleStatistics(boolean Bool) {
        this.TEXTDRAW = Bool;
    }
    public void toggleColor(boolean Bool) {
        this.COLOR = Bool;
    }
    
    public void setVisual(VisualStyles choice) {
        this.VisualStyles = choice;
    }
    
    public int getCurrentGap() {
        return this.currentGap;
    }
    public void setCurrentGap(int gap) {
        this.currentGap = gap;
    }
    
    public void repositionFrames() {
        ArrayFrame.reposition();
        UtilFrame.reposition(ArrayFrame);
    }
    
    public boolean rainbowEnabled() {
        return this.RAINBOW;
    }
    public boolean colorEnabled() {
        return this.COLOR;
    }
    public boolean spiralEnabled() {
        return this.SPIRALDRAW;
    }
    public boolean distanceEnabled() {
        return this.DISPARITYDRAW;
    }
    public boolean pixelsEnabled() {
        return this.PIXELDRAW;
    }
    public boolean linesEnabled() {
        return this.LINEDRAW;
    }
    
    public DecimalFormat getNumberFormat() {
        return this.formatter;
    }
    
    private void drawWindows() {
        this.VisualStyles = visuals.VisualStyles.BARS;
        this.category = "Select a Sort";
        
        // For recording with OBS (don't ask where these numbers came from. I don't get it myself)
        if(OBS) {
            int x = (int) (1920 * (double) (1920 / 1916));
            int y = (int) (1080 * (double) (1080 / 1020));
            window.setSize(new Dimension(x, y));        
        }
        else { // Consider changing back to discrete 16:9 dimension
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            window.setSize((int) (screenSize.getWidth() / 2d), (int) (screenSize.getHeight() / 2d));    
        }
        
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("w0rthy's Array Visualizer - " + (ComparisonSorts[0].length + DistributionSorts[0].length) + " Sorting Algorithms with 10 Different Visual Styles");
        window.setBackground(Color.BLACK);

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        
        //TODO: Consider removing insets from window size
        this.cw = window.getWidth();
        this.ch = window.getHeight();
        
        visualsThread.start();
        
        UtilFrame.setVisible(true);
        ArrayFrame.setVisible(true);
        
        if(this.InvalidSorts != null) {
            String output = "";
            for(int i = 0; i < InvalidSorts.length; i++) {
                output += InvalidSorts[i] + "\n";
            }
            JOptionPane.showMessageDialog(window, "The following algorithms were not loaded due to errors:\n" + output, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        new ArrayVisualizer();
    }
}