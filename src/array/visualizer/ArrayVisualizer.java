package array.visualizer;

import static array.visualizer.Writes.swap;
import static sorts.AmericanFlagSort.flagSort;
import static sorts.BadSort.badSort;
import static sorts.BitonicSort.bitonicSort;
import static sorts.BogoSorts.bogoSort;
import static sorts.BogoSorts.doubleBogoSort;
import static sorts.BogoSorts.lessBogoSort;
import static sorts.BubbleSort.bubbleSort;
import static sorts.CocktailShaker.cocktailShakerSort;
import static sorts.CombSort.combSort;
import static sorts.CountingSort.countingSort;
import static sorts.CycleSort.cycleSort;
import static sorts.FlashSort.flashSort;
import static sorts.GnomeSorts.gnomeSort;
import static sorts.GnomeSorts.smartGnome;
import static sorts.GrailSort.grailSortWithoutBuffer;
import static sorts.GravitySort.gravitySort;
import static sorts.HeapSorts.heapSort;
import static sorts.HeapSorts.smoothSort;
import static sorts.HeapSorts.ternaryHeapSort;
import static sorts.InsertionSorts.binaryInsertionSort;
import static sorts.InsertionSorts.insertionSort;
import static sorts.IntroSort.introSort;
import static sorts.MergeSort.mergeSort;
import static sorts.MergeSortOOP.mergeSortOOP;
import static sorts.MergeSortOOP.stableSort;
import static sorts.OddEvenMergeSort.oddEvenMergeSort;
import static sorts.OddEvenSort.oddEvenSort;
import static sorts.PancakeSort.pancakeSort;
import static sorts.PatienceSort.patienceSort;
import static sorts.PigeonholeSort.pigeonSort;
import static sorts.QuickSorts.dualPivot;
import static sorts.QuickSorts.quickSort;
import static sorts.QuickSorts.stableQuickSort;
import static sorts.RadixLSD.inPlaceRadixLSDSort;
import static sorts.RadixLSD.radixLSDsort;
import static sorts.RadixMSD.radixMSDSort;
import static sorts.SelectionSorts.doubleSelectionSort;
import static sorts.SelectionSorts.selectionSort;
import static sorts.ShatterSorts.shatterSort;
import static sorts.ShatterSorts.simpleShatterSort;
import static sorts.ShellSort.shellSort;
import static sorts.SillySort.sillySort;
import static sorts.SlowSort.slowSort;
import static sorts.StoogeSort.stoogeSort;
import static sorts.TimSort.timSort;
import static sorts.TimeSort.timeSort;
import static sorts.TournamentSort.tournamentSort;
import static sorts.WeakHeapSort.weakHeapSort;
import static sorts.WeaveMerge.weaveMergeSort;
import static sorts.WikiSort.startWikiSort;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
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
 */
public class ArrayVisualizer {

    static String[] ComparativeSorts =  ("Bad!Binary Insert!Binary Merge!Bitonic!Bottom-Up Merge!Bubble!Cocktail Shaker!Comb!Cycle"
            + "!Double Selection!Dual-Pivot Quick!Gnome!Grail!Hybrid Comb!Insertion!Intro!Max Heap!Merge"
            + "!Merge In-Place!Min Heap!Odd-Even!Odd-Even Merge!Pancake!Patience!Quick!Quick Shell"
            + "!Selection!Shell!Silly!Slow!Smart Gnome!Smooth!Stable Quick!Stooge!Ternary Heap!Tim"
            + "!Tournament!Weak Heap!Weave Merge!Wiki").split("!");

    static String[] DistributiveSorts = ("American Flag!Bogo!Cocktail Bogo!Counting!Flash!Gravity!Less Bogo!Pigeonhole!Radix LSD"
            + "!Radix LSD In-Place!Radix MSD!Shatter!Simple Shatter!Time").split("!");

    static final JFrame window = new JFrame();
    static Thread sortingThread;
    static UtilFrame uf;
    static ArrayFrame af;
    static ViewPrompt v;
    static Synthesizer synth;
    static MidiChannel[] chan;

    public static final int MIN_ARRAY_VAL = 2;
    public static final int MAX_ARRAY_VAL = 4096;

    private static final int [] array = new int[MAX_ARRAY_VAL];
    public static final ArrayList<Integer> marked = new ArrayList<Integer>();

    public static volatile int currentLen = 2048;
    static volatile int lenLog = (int) (Math.log(currentLen)/Math.log(2));
    static volatile boolean MUTABLE = true;

    static String[] ShuffleTypes = ("Random!Reversed!Mostly Similar!Almost Sorted!Already Sorted").split("!");
    static volatile String shuffleType = "random";

    static String category = "";
    static String heading = "";
    static Font typeFace = new Font("Times New Roman",Font.PLAIN,(int)(640/1280.0*25));
    static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    static DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    public static long swaps = 0;
    public static long comps = 0;
    public static long writes = 0;
    public static long tempStores = 0;

    static int frames;
    static long nanos;
    static double SLEEPRATIO = 1.0;

    static volatile boolean TEXTDRAW = true;
    static volatile boolean COLOR = false;
    static volatile boolean DISPARITYDRAW = false;
    static volatile boolean LINEDRAW = false;
    static volatile boolean PIXELDRAW = false;
    static volatile boolean RAINBOW = false;
    static volatile boolean SPIRALDRAW = false;

    final static int CIRCULAR = 1;
    final static int HOOPS = 2;
    final static int MESH = 3;
    final static int BARS = 4;
    final static int PIXELS = 5;

    public static volatile int VISUALS = BARS;

    static boolean SHUFFLEANIM = true;
    static boolean FANCYFINISH = true;

    public static volatile boolean SKIPPED = false;

    static volatile boolean ANALYZE = false;

    static volatile boolean fancyFinish;
    static int trackFinish;
    static boolean drawRect;

    static volatile boolean POINTER;

    static int pointerBase;
    static int pointerHeight;
    static int pointerWidth;
    static double sinVal;

    private static final double PNT_WIDTH_B = 255.1075269;
    private static final double PNT_WIDTH_A = -.1123571909;
    private static final double PNT_HEIGHT_B = 180.3225806;
    private static final double PNT_HEIGHT_A = -.0831653226;
    private static final double PNT_BASE_B = .7741935484;
    private static final double PNT_BASE_A = .0035282258;

    static volatile boolean SOUND = true;
    static boolean MIDI = false;
    static int NUMCHANNELS = 10; //Number of Audio Channels
    static double PITCHMIN = 25d; //Minimum Pitch
    static double PITCHMAX = 105d; //Maximum Pitch (up to 112)
    static double SOUNDMUL = 1.0;
    static int snd = 0;
    static final int REVERB = 91;

    static double delay;
    public static volatile double realTimer;
    static volatile boolean REALTIMER = true;
    public static volatile boolean calcReal = false;

    static double addamt = 0.0;
    private static int base;
    private static int num;

    static int cx = 0;
    static int cy = 0;

    public static int compare(int left, int right) {
        comps++;
        int cmp = 0;
        long time = System.nanoTime();

        if(left > right) cmp = 1;
        else if(left < right) cmp = -1;
        else cmp = 0;

        if(calcReal) realTimer += ((System.nanoTime() - time) / 1e+6); 
        return cmp;
    }

    public static String checkSwapOverflow() {
        if(swaps < 0) {
            swaps = Long.MIN_VALUE;
            return "Over " + formatter.format(Integer.MAX_VALUE);
        }
        else {
            return formatter.format(swaps);
        }
    }

    public static String checkCompOverflow() {
        if(comps < 0) {
            comps = Long.MIN_VALUE;
            return "Over " + formatter.format(Integer.MAX_VALUE);
        }
        else {
            return formatter.format(comps);
        }
    }

    public static String checkWriteOverflow() {
        if(writes < 0) {
            writes = Long.MIN_VALUE;
            return "Over " + formatter.format(Integer.MAX_VALUE);
        }
        else {
            return formatter.format(writes);
        }
    }

    public static String checkTempOverflow() {
        if(tempStores < 0) {
            tempStores = Long.MIN_VALUE;
            return "Over " + formatter.format(Integer.MAX_VALUE);
        }
        else {
            return formatter.format(tempStores);
        }
    }

    public static void sleep(double milis){
        if(milis <= 0) {
            return;
        }
        delay = (milis * (1/SLEEPRATIO));
        addamt += delay;
        long amt = (long)delay;
        if(addamt >= 1){
            amt+=(int)addamt;
            addamt -= (int)addamt;
        }
        try {
            Thread.sleep(amt);
        }catch(Exception ex){
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void SetSound(boolean val){
        MIDI = val;
    }

    private static void startRealTimer() {
        if(REALTIMER) calcReal = true;
        realTimer = 0;
    }


    private static String getRealTime() {
        if(!REALTIMER) {
            return "Disabled";
        }
        else if(realTimer == 0) {
            if(calcReal) return "0.000ms";
            else return "---ms";
        }
        else if(realTimer < 0.001) return "<0.001ms";
        else return String.valueOf(formatter.format(realTimer)) + "ms";
    }

    public static void main(String[] args) throws Exception {
        VISUALS = BARS;
        category = "Select a Sort";

        window.setSize(new Dimension(1280,720));
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("w0rthy's Array Visualizer - 54 Sorting Algorithms with 10 Different Visual Styles");
        window.setBackground(Color.BLACK);

        uf = new UtilFrame(window);
        af = new ArrayFrame(window);
        uf.reposition();

        synth = MidiSystem.getSynthesizer();
        synth.open();
        synth.loadAllInstruments(synth.getDefaultSoundbank());

        chan = new MidiChannel[NUMCHANNELS];

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);

        for(int i = 0; i < MAX_ARRAY_VAL; i++) marked.add(-5);

        for(int i = 0; i < NUMCHANNELS; i++) {
            chan[i] = synth.getChannels()[i];
            chan[i].programChange(synth.getLoadedInstruments()[16].getPatch().getProgram());
            chan[i].setChannelPressure(1);
        }
        if(chan[0].getProgram() == -1) {
            JOptionPane.showMessageDialog(null, "Could not find a valid instrument. Sound is disabled");
        }

        initArr(array);

        //AUDIO THREAD
        new Thread(){
            @Override
            public void run(){
                while(true){

                    for(MidiChannel c : chan)
                        c.allNotesOff();
                    if(SOUND == false || MIDI == false){
                        continue;
                    }

                    int tmp = 0;
                    int cchan = 0;
                    for(int i : marked)
                        if(i != -5)
                            tmp++;

                    tmp = Math.min(tmp, NUMCHANNELS);

                    for(int i : marked)
                        if(i != -5){
                            //PITCH
                            double pitch = (double) array[Math.min(Math.max(i, 0),currentLen-1)]/currentLen*(PITCHMAX-PITCHMIN)+PITCHMIN;
                            int pitchmajor = (int)pitch;
                            int pitchminor = (int)((pitch-(double)((int)pitch))*8192d)+8192;

                            int vel = (int)(Math.pow(113d-pitchmajor,2d)   *   (Math.pow(tmp, -0.25))   *   64d*SOUNDMUL) / 2; //I'VE SOLVED IT!!

                            chan[cchan].noteOn(pitchmajor, vel);
                            chan[cchan].setPitchBend(pitchminor);
                            chan[cchan].controlChange(REVERB, 10);
                            if(++cchan%NUMCHANNELS==0)
                                break;
                        }
                    try{sleep(1);}catch(Exception e){}
                }
            }
        }.start();

        //DRAW THREAD
        new Thread(){
            private void lineMark(Graphics2D line) {
                line.setStroke(new BasicStroke(9f*(window.getWidth()/1280f)));
                if(COLOR) line.setColor(Color.BLACK);
                else if(ANALYZE) line.setColor(Color.BLUE);
                else line.setColor(Color.RED);
            }
            private void lineClear(Graphics2D line, int i) {
                if(COLOR) line.setColor(getIntColor(array[i])); 
                else line.setColor(Color.WHITE);
                line.setStroke(new BasicStroke(3f*(window.getWidth()/1280f)));
            }
            private void lineFancy(Graphics2D line) {
                line.setColor(Color.GREEN);
                line.setStroke(new BasicStroke(9f*(window.getWidth()/1280f)));
            }
            private void rectColor(Graphics2D rect) {
                if(COLOR) rect.setColor(Color.WHITE);
                else if(ANALYZE) rect.setColor(Color.BLUE);
                else rect.setColor(Color.RED);
            }
            private void markBarFancy(Graphics2D line) {
                if(!COLOR && !RAINBOW) line.setColor(Color.RED);
                else line.setColor(Color.BLACK);
            }
            private void markBar(Graphics2D bar) {
                if(COLOR || RAINBOW) bar.setColor(Color.BLACK);
                else if(ANALYZE) bar.setColor(Color.BLUE);
                else bar.setColor(Color.RED);
            }
            private Color getIntColor(int i) {
                return Color.getHSBColor(((float)i/currentLen), 1.0F, 0.8F);
            }

            @Override
            public void run() {
                int cw = window.getWidth();
                int ch = window.getHeight();
                Image img = window.createVolatileImage(cw, ch);
                Graphics2D g = (Graphics2D)img.getGraphics();
                Graphics2D g2 = (Graphics2D)img.getGraphics();
                Stroke thickStroke = new BasicStroke(8);
                Stroke oldStroke = g2.getStroke();

                double xscl, yscl;
                while(true){
                    if(window.getWidth()!=cw|| window.getHeight()!=ch || window.getX() != cx || window.getY() != cy){
                        uf.reposition();
                        af.reposition();
                        if(v != null && v.isVisible())
                            v.reposition();
                        cx = window.getX();
                        cy  = window.getY();
                    }

                    if(window.getWidth()!=cw|| window.getHeight()!=ch){
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        typeFace = new Font("Times New Roman",Font.PLAIN,(int)(cw/1280.0*25));
                        g = (Graphics2D)img.getGraphics();
                        g2 = (Graphics2D)img.getGraphics();

                    }
                    int gamt = 32;
                    g.setColor(new Color(gamt,gamt,gamt));
                    g.fillRect(0,0,img.getWidth(null),img.getHeight(null));
                    int circamt;
                    circamt = currentLen/2;
                    xscl = (double)(window.getWidth() - 40)/currentLen;
                    yscl = (double)(window.getHeight() - 96)/currentLen;
                    int amt = 0;
                    int linkedpixdrawx = 0;
                    int linkedpixdrawy = 0;
                    frames++;

                    int halfwidth = window.getWidth()/2;
                    int halfheight = (window.getHeight()/2 + 24);
                    int dotw = (int)(2*(window.getWidth()/640.0));
                    int doth = (int)(2*(window.getHeight()/480.0));
                    int dots = (dotw+doth)/2;
                    g.setStroke(new BasicStroke(3f*(window.getWidth()/1280f)));

                    switch(VISUALS) {
                    case CIRCULAR:
                        for(int i = 0; i < currentLen; i++){
                            if(i < trackFinish) {
                                g.setColor(Color.getHSBColor((1f/3f), 1f, 0.8f));
                            }
                            else if(!COLOR && (SPIRALDRAW || DISPARITYDRAW || PIXELDRAW)) g.setColor(Color.WHITE);
                            else g.setColor(getIntColor(array[i]));

                            if(fancyFinish) {
                                switch(lenLog) {
                                case 12: if(i == trackFinish - 11) markBarFancy(g);
                                case 11: if(i == trackFinish - 10) markBarFancy(g);
                                case 10: if(i == trackFinish - 9) markBarFancy(g);
                                case 9: if(i == trackFinish - 8) markBarFancy(g);
                                case 8: if(i == trackFinish - 7) markBarFancy(g);
                                case 7: if(i == trackFinish - 6) markBarFancy(g);
                                case 6: if(i == trackFinish - 5) markBarFancy(g);
                                case 5: if(i == trackFinish - 4) markBarFancy(g);
                                case 4: if(i == trackFinish - 3) markBarFancy(g);
                                case 3: if(i == trackFinish - 2) markBarFancy(g);
                                case 2: if(i == trackFinish - 1) markBarFancy(g);
                                default: if(i == trackFinish) markBarFancy(g);
                                }
                            }
                            else {
                                if(POINTER) {
                                    if(marked.contains(i)) {
                                        g2.setColor(Color.WHITE);

                                        if(currentLen > 16) {
                                            pointerBase = (int) Math.ceil(((PNT_BASE_A * currentLen) + PNT_BASE_B));
                                            pointerHeight = (int) (((PNT_HEIGHT_A * currentLen) + PNT_HEIGHT_B)*(ch/1080));
                                            pointerWidth = (int) (((PNT_WIDTH_A * currentLen) + PNT_WIDTH_B)*(cw/1280));
                                        }
                                        else {
                                            pointerBase = 0;
                                            pointerHeight = 0;
                                            pointerWidth = 0;
                                        }


                                        sinVal = ((i+(i+1.0))/2.0);

                                        Polygon pointer = new Polygon();
                                        pointer.addPoint(halfwidth+(int)(Math.sin((sinVal-pointerBase)*Math.PI/circamt)*(window.getWidth()+pointerWidth)/4.43), halfheight-(int)(Math.cos((sinVal-pointerBase)*Math.PI/circamt)*(window.getHeight()+pointerHeight)/2.6));
                                        pointer.addPoint(halfwidth+(int)(Math.sin(sinVal*Math.PI/circamt)*(window.getWidth()-50)/4.43), halfheight-(int)(Math.cos(sinVal*Math.PI/circamt)*(window.getHeight()-50)/2.6));
                                        pointer.addPoint(halfwidth+(int)(Math.sin((sinVal+pointerBase)*Math.PI/circamt)*(window.getWidth()+pointerWidth)/4.43), halfheight-(int)(Math.cos((sinVal+pointerBase)*Math.PI/circamt)*(window.getHeight()+pointerHeight)/2.6));
                                        g2.fillPolygon(pointer);
                                    }
                                }
                            }

                            double sinval = Math.sin(i*Math.PI/circamt);
                            double cosval = Math.cos(i*Math.PI/circamt);

                            if(DISPARITYDRAW) {
                                double len = ((currentLen/2d)-Math.min(Math.min(Math.abs(i-array[i]), Math.abs(i-array[i]+currentLen)),Math.abs(i-array[i]-currentLen)))/(currentLen/2d);

                                if(PIXELDRAW){
                                    int linkedpixX = halfwidth+(int)(sinval*((window.getWidth()-64)/4.675*len)) + dotw/2;
                                    int linkedpixY = halfheight-(int)(cosval*((window.getHeight()-96)/2.63*len)) + doth/2;

                                    if(LINEDRAW){
                                        if(i>0) {
                                            if(fancyFinish) {
                                                if(i < trackFinish) lineFancy(g);
                                                else lineClear(g, i);

                                                switch(lenLog) {
                                                case 12: if(i == trackFinish - 11) lineMark(g);
                                                case 11: if(i == trackFinish - 10) lineMark(g);
                                                case 10: if(i == trackFinish - 9) lineMark(g);
                                                case 9: if(i == trackFinish - 8) lineMark(g);
                                                case 8: if(i == trackFinish - 7) lineMark(g);
                                                case 7: if(i == trackFinish - 6) lineMark(g);
                                                case 6: if(i == trackFinish - 5) lineMark(g);
                                                case 5: if(i == trackFinish - 4) lineMark(g);
                                                case 4: if(i == trackFinish - 3) lineMark(g);
                                                case 3: if(i == trackFinish - 2) lineMark(g);
                                                case 2: if(i == trackFinish - 1) lineMark(g);
                                                default: if(i == trackFinish) lineMark(g);
                                                }
                                            }
                                            else {
                                                if(marked.contains(i)) lineMark(g);
                                                else lineClear(g, i);
                                            }
                                            g.drawLine(linkedpixX, linkedpixY, linkedpixdrawx, linkedpixdrawy);
                                        }
                                        linkedpixdrawx = linkedpixX;
                                        linkedpixdrawy = linkedpixY;
                                    }
                                    else {
                                        if(marked.contains(i)) {
                                            rectColor(g2);
                                            drawRect = true;
                                        }
                                        else drawRect = false;

                                        if(drawRect) {
                                            g2.setStroke(thickStroke);
                                            if(fancyFinish) {
                                                g2.fillRect((linkedpixX - dotw/2) - 10, (linkedpixY - doth/2) - 10, dotw + 20, doth + 20);
                                            }
                                            else {
                                                g2.drawRect((linkedpixX - dotw/2) - 10, (linkedpixY - doth/2) - 10, dotw + 20, doth + 20);
                                            }
                                            g2.setStroke(oldStroke);
                                        }

                                        g.fillRect(linkedpixX - dotw/2, linkedpixY - doth/2, dotw, doth);
                                    }
                                }
                                else{
                                    Polygon p = new Polygon();
                                    p.addPoint(halfwidth, halfheight);
                                    p.addPoint(halfwidth+(int)(sinval*((window.getWidth()-64)/4.675*len)), halfheight-(int)(cosval*((window.getHeight()-96)/2.63*len)));
                                    p.addPoint(halfwidth+(int)(Math.sin((i+1)*Math.PI/circamt)*((window.getWidth()-64)/4.675*len)), halfheight-(int)(Math.cos((i+1)*Math.PI/circamt)*((window.getHeight()-96)/2.63*len)));
                                    g.fillPolygon(p);
                                }
                            }
                            else if(SPIRALDRAW) {
                                if(PIXELDRAW){
                                    if(LINEDRAW){
                                        if(i>0) {
                                            if(fancyFinish) {
                                                if(i < trackFinish) lineFancy(g);
                                                else lineClear(g, i);

                                                switch(lenLog) {
                                                case 12: if(i == trackFinish - 11) lineMark(g);
                                                case 11: if(i == trackFinish - 10) lineMark(g);
                                                case 10: if(i == trackFinish - 9) lineMark(g);
                                                case 9: if(i == trackFinish - 8) lineMark(g);
                                                case 8: if(i == trackFinish - 7) lineMark(g);
                                                case 7: if(i == trackFinish - 6) lineMark(g);
                                                case 6: if(i == trackFinish - 5) lineMark(g);
                                                case 5: if(i == trackFinish - 4) lineMark(g);
                                                case 4: if(i == trackFinish - 3) lineMark(g);
                                                case 3: if(i == trackFinish - 2) lineMark(g);
                                                case 2: if(i == trackFinish - 1) lineMark(g);
                                                default: if(i == trackFinish) lineMark(g);
                                                }
                                            }
                                            else {
                                                if(marked.contains(i)) lineMark(g);
                                                else lineClear(g, i);
                                            }
                                            g.drawLine(halfwidth+(int)(sinval*((window.getWidth()-64)/3.0*(array[i]/(double)currentLen))), halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)currentLen))), linkedpixdrawx, linkedpixdrawy);
                                        }
                                        linkedpixdrawx = halfwidth+(int)(sinval*((window.getWidth()-64)/3.0*(array[i]/(double)currentLen)));
                                        linkedpixdrawy = halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)currentLen)));		
                                    }
                                    else {
                                        if(marked.contains(i)) {
                                            rectColor(g2);
                                            drawRect = true;
                                        }
                                        else drawRect = false;

                                        int rectx = halfwidth+(int)(sinval*((window.getWidth()-64)/3*(array[i]/(double)currentLen)));
                                        int recty = halfheight-(int)(cosval*((window.getHeight()-96)/2*(array[i]/(double)currentLen)));

                                        g.fillRect(rectx, recty, dotw, doth);

                                        if(drawRect) {
                                            g2.setStroke(thickStroke);
                                            if(fancyFinish) {
                                                g2.fillRect(rectx - 10, recty - 10, dotw + 20, doth + 20);
                                            }
                                            else {
                                                g2.drawRect(rectx - 10, recty - 10, dotw + 20, doth + 20);
                                            }
                                            g2.setStroke(oldStroke);
                                        }
                                    }
                                }
                                else {
                                    if(marked.contains(i)) {
                                        switch(lenLog) {
                                        case 12: if(marked.contains(i - 10)) markBarFancy(g);
                                        case 11: if(marked.contains(i - 10)) markBarFancy(g);
                                        case 10: if(marked.contains(i - 9)) markBarFancy(g);
                                        case 9: if(marked.contains(i - 8)) markBarFancy(g);
                                        case 8: if(marked.contains(i - 7)) markBarFancy(g);
                                        case 7: if(marked.contains(i - 6)) markBarFancy(g);
                                        case 6: if(marked.contains(i - 5)) markBarFancy(g);
                                        case 5: if(marked.contains(i - 4)) markBarFancy(g);
                                        case 4: if(marked.contains(i - 3)) markBarFancy(g);
                                        case 3: if(marked.contains(i - 2)) markBarFancy(g);
                                        case 2: if(marked.contains(i - 1)) markBarFancy(g);
                                        default: markBarFancy(g);
                                        }
                                    }
                                    Polygon p = new Polygon();
                                    p.addPoint(halfwidth, halfheight);
                                    p.addPoint(halfwidth+(int)(Math.sin((i)*Math.PI/circamt)*((window.getWidth()-64)/3*(array[i]/(double)currentLen))), halfheight-(int)(Math.cos((i)*Math.PI/circamt)*((window.getHeight()-96)/2*(array[i]/(double)currentLen))));
                                    p.addPoint(halfwidth+(int)(Math.sin((i+1)*Math.PI/circamt)*((window.getWidth()-64)/3*(array[Math.min(i+1,currentLen-1)]/(double)currentLen))), halfheight-(int)(Math.cos((i+1)*Math.PI/circamt)*((window.getHeight()-96)/2*(array[Math.min(i+1,currentLen-1)]/(double)currentLen))));
                                    g.fillPolygon(p);
                                }
                            }
                            else {
                                Polygon p = new Polygon();
                                p.addPoint(halfwidth, halfheight);
                                p.addPoint(halfwidth+(int)(sinval*(window.getWidth()-64)/4.675), halfheight-(int)(cosval*(window.getHeight()-96)/2.63));
                                p.addPoint(halfwidth+(int)(Math.sin((i+1)*Math.PI/circamt)*(window.getWidth()-64)/4.675), halfheight-(int)(Math.cos((i+1)*Math.PI/circamt)*(window.getHeight()-96)/2.63));
                                g.fillPolygon(p);
                            }
                        }
                        break;
                    case HOOPS:
                        g.setStroke(new BasicStroke(1.0f)); //significantly increased performance

                        double maxdiam = (double)Math.min(cw, ch-72);
                        double diameter = maxdiam;
                        double diamstep = Math.min(xscl, yscl);

                        for(int i = currentLen; i >= 0; i--){
                            if(fancyFinish) {
                                if(i < trackFinish) g.setColor(Color.GREEN);
                                else g.setColor(getIntColor(array[i]));

                                switch(lenLog) {
                                case 12: if(i == trackFinish - 11) g.setColor(Color.BLACK);
                                case 11: if(i == trackFinish - 10) g.setColor(Color.BLACK);
                                case 10: if(i == trackFinish - 9) g.setColor(Color.BLACK);
                                case 9: if(i == trackFinish - 8) g.setColor(Color.BLACK);
                                case 8: if(i == trackFinish - 7) g.setColor(Color.BLACK);
                                case 7: if(i == trackFinish - 6) g.setColor(Color.BLACK);
                                case 6: if(i == trackFinish - 5) g.setColor(Color.BLACK);
                                case 5: if(i == trackFinish - 4) g.setColor(Color.BLACK);
                                case 4: if(i == trackFinish - 3) g.setColor(Color.BLACK);
                                case 3: if(i == trackFinish - 2) g.setColor(Color.BLACK);
                                case 2: if(i == trackFinish - 1) g.setColor(Color.BLACK);
                                default: if(i == trackFinish) g.setColor(Color.BLACK);
                                }
                            }
                            else if(marked.contains(i)) g.setColor(Color.BLACK);
                            else g.setColor(getIntColor(array[i]));

                            int radius = (int)(diameter/2.0);

                            g.drawOval(halfwidth-radius, halfheight-radius-12, (int)diameter, (int)diameter);
                            diameter-=diamstep;
                        }
                        break;
                    case MESH:
                        int trih = window.getHeight()/20; //Height of triangles to use, Width will be scaled accordingly

                        switch(currentLen) {
                        case 2: trih *= 20; break;
                        case 4: trih *= 13; break;
                        case 8: trih *= 8; break;
                        case 16:
                        case 32: trih *= 4.4; break; 
                        case 64: trih *= 2.3; break;
                        case 128: trih *= 2.35; break;
                        case 256: trih *= 1.22; break;
                        default: trih *= 1;
                        }

                        int tripercol = window.getHeight()/trih*2; //Triangles per vertical column
                        int triperrow;

                        switch(currentLen) {
                        case 32: 
                        case 64: triperrow = 4; break;
                        case 128:
                        case 256: triperrow = 8; break;
                        default: triperrow = Math.max(currentLen/tripercol,2); //Triangles per horizontal row
                        }

                        double triw = (double)window.getWidth()/triperrow; //Width of triangles to use

                        double curx = 0;
                        int cury = 15;

                        int[] triptsx = new int[3];
                        int[] triptsy = new int[3];

                        for(int i = 0; i < currentLen; i++){
                            if(marked.contains(i)) g.setColor(Color.BLACK);
                            else {
                                if(fancyFinish && (i < trackFinish && i > trackFinish - (lenLog * 10))) g.setColor(Color.GREEN);
                                else g.setColor(getIntColor(array[i]));
                            }
                            //If i/triperrow is even, then triangle points right, else left
                            boolean direction = false;
                            if(i/triperrow % 2 == 0) direction = true;
                            else direction = false;

                            //Make the triangle
                            if(!direction){
                                //Pointing right
                                triptsx[0] = (int)curx;
                                triptsx[1] = (int)curx;
                                curx+=triw;
                                triptsx[2] = (int)curx;

                                triptsy[0] = cury;
                                triptsy[2] = cury + trih/2;
                                triptsy[1] = cury + trih;
                            }else{
                                //Pointing left
                                triptsx[2] = (int)curx;
                                curx+=triw;
                                triptsx[0] = (int)curx;
                                triptsx[1] = (int)curx;

                                triptsy[0] = cury;
                                triptsy[2] = cury + trih/2;
                                triptsy[1] = cury + trih;
                            }

                            //Draw it
                            g.fillPolygon(triptsx,triptsy,triptsx.length);

                            //If at the end of a row, reset curx
                            //(i != 0 || i != currentLen - 1)
                            if((i + 1) % triperrow == 0){
                                curx = 0d;
                                cury+=trih/2;
                            }
                        }
                        break;
                    case BARS:
                        for(int i = 0; i < currentLen; i++){
                            if(fancyFinish) {
                                if(i < trackFinish) g.setColor(Color.GREEN);
                                else if(RAINBOW || COLOR) g.setColor(getIntColor(array[i]));
                                else g.setColor(Color.WHITE);

                                switch(lenLog) {
                                case 12: if(i == trackFinish - 11) markBarFancy(g);
                                case 11: if(i == trackFinish - 10) markBarFancy(g);
                                case 10: if(i == trackFinish - 9) markBarFancy(g);
                                case 9: if(i == trackFinish - 8) markBarFancy(g);
                                case 8: if(i == trackFinish - 7) markBarFancy(g);
                                case 7: if(i == trackFinish - 6) markBarFancy(g);
                                case 6: if(i == trackFinish - 5) markBarFancy(g);
                                case 5: if(i == trackFinish - 4) markBarFancy(g);
                                case 4: if(i == trackFinish - 3) markBarFancy(g);
                                case 3: if(i == trackFinish - 2) markBarFancy(g);
                                case 2: if(i == trackFinish - 1) markBarFancy(g);
                                default: if(i == trackFinish) markBarFancy(g);
                                }
                            }
                            else {
                                if(RAINBOW || COLOR) g.setColor(getIntColor(array[i]));
                                else g.setColor(Color.WHITE);

                                if(marked.contains(i)) markBar(g);
                            }

                            int y = 0;
                            int width = (int)(xscl*(i+1))-amt;

                            if(RAINBOW) {
                                if(width>0) g.fillRect(amt+20, 0, width, window.getHeight());
                                amt+=width;
                            }
                            else {
                                if(width>0){
                                    y = (int)((window.getHeight()-20)-(array[i])*yscl);
                                    g.fillRect(amt+20, y, width, (int) Math.max(array[i]*yscl,1));
                                }
                                amt+=width;
                            }
                        }
                        break;
                    case PIXELS:
                        if(LINEDRAW) {
                            for(int i = 0; i < currentLen; i++){
                                int y = 0;
                                int width = (int)(xscl*(i+1))-amt;

                                if(width>0){
                                    y = (int)((window.getHeight()-20)-array[i]*yscl);
                                    if(i>0) {
                                        if(fancyFinish) {
                                            if(i < trackFinish) lineFancy(g);
                                            else lineClear(g, i);

                                            switch(lenLog) {
                                            case 12: if(i == trackFinish - 11) lineMark(g);
                                            case 11: if(i == trackFinish - 10) lineMark(g);
                                            case 10: if(i == trackFinish - 9) lineMark(g);
                                            case 9: if(i == trackFinish - 8) lineMark(g);
                                            case 8: if(i == trackFinish - 7) lineMark(g);
                                            case 7: if(i == trackFinish - 6) lineMark(g);
                                            case 6: if(i == trackFinish - 5) lineMark(g);
                                            case 5: if(i == trackFinish - 4) lineMark(g);
                                            case 4: if(i == trackFinish - 3) lineMark(g);
                                            case 3: if(i == trackFinish - 2) lineMark(g);
                                            case 2: if(i == trackFinish - 1) lineMark(g);
                                            default: if(i == trackFinish) lineMark(g);
                                            }
                                        }
                                        else if(marked.contains(i)) lineMark(g);
                                        else lineClear(g, i);

                                        g.drawLine(amt, y, linkedpixdrawx, linkedpixdrawy);
                                    }
                                    linkedpixdrawx = amt;
                                    linkedpixdrawy = y;
                                }
                                amt+=width;
                            }
                        }
                        else {
                            for(int i = 0; i < currentLen; i++){
                                if(i < trackFinish) g.setColor(Color.GREEN);
                                else if(i == trackFinish && fancyFinish) {
                                    if(COLOR) g.setColor(Color.WHITE);
                                    else g.setColor(Color.RED);
                                }
                                else if(COLOR) g.setColor(getIntColor(array[i]));
                                else g.setColor(Color.WHITE);

                                int y = 0;
                                int width = (int)(xscl*(i+1))-amt;

                                if(marked.contains(i)) {
                                    rectColor(g2);
                                    drawRect = true;
                                }
                                else drawRect = false;

                                if(width>0){
                                    y = (int)((window.getHeight()-20)-array[i]*yscl);
                                    g.fillRect(amt + 20, y, dots, dots);
                                    if(drawRect) {
                                        g2.setStroke(thickStroke);
                                        if(fancyFinish) {
                                            g2.fillRect(amt + 10, y - 10, dots + 20, dots + 20);
                                        }
                                        else {
                                            g2.drawRect(amt + 10, y - 10, dots + 20, dots + 20);
                                        }
                                        g2.setStroke(oldStroke);
                                    }
                                }
                                amt+=width;
                            }
                        }
                        break;
                    default:
                        try {
                            throw new Exception("Invalid graphic setting!");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    }
                    int coltmp = 255;
                    g.setColor(new Color(coltmp,coltmp,coltmp));
                    if(TEXTDRAW) {
                        Font f = g.getFont();
                        g.setFont(typeFace);
                        g.drawString(category + ": " + heading, 15, (int)(cw/1280.0*40)+30);
                        g.drawString(formatter.format(currentLen) + " Numbers", 15, (int)(cw/1280.0*65)+30);
                        g.drawString(String.format("Delay: " + formatter.format(delay) + "ms"), 15, (int)(cw/1280.0*105)+30);
                        g.drawString(String.format("Estimated Real Time: " + getRealTime()), 15, (int)(cw/1280.0*130)+30);
                        g.drawString(checkCompOverflow() + " Comparisons", 15, (int)(cw/1280.0*170)+30);
                        g.drawString(checkSwapOverflow() + " Swaps", 15, (int)(cw/1280.0*195)+30);
                        g.drawString(checkWriteOverflow() + " Writes to Main Array", 15, (int)(cw/1280.0*220)+30);
                        g.drawString(checkTempOverflow() + " Writes to Auxillary Array(s)", 15, (int)(cw/1280.0*245)+30);
                        g.setFont(f);
                    }
                    Graphics g3 = window.getGraphics();
                    g3.setColor(Color.BLACK);
                    g3.drawImage(img, 0, 0, null);
                }
            }
        }.start();

        uf.setVisible(true);
        af.setVisible(true);

        //keep on keeping on
        while(window.isActive())Thread.sleep(100);
    }

    public static void fancyFinish() {
        fancyFinish = true;
        trackFinish = -1;

        SLEEPRATIO = 1;

        for(int i = 0; i < currentLen + lenLog; i++) {
            if(i < currentLen) marked.set(1, i);
            trackFinish++;
            sleep(100 * (4.0 / (double) currentLen));
        }
        marked.set(1, -5);

        trackFinish = -1;
        fancyFinish = false;
    }

    public static void refresharray() throws Exception {
        Thread.sleep(1000);
        for(int i = 0; i < currentLen; i++){
            marked.set(1,i);
        }
        marked.set(1, -5);
        heading = "";
        writes = 0;
        shuffle(array);
        clearmarked();
        Thread.sleep(500);
        swaps = 0;
        comps = 0;
        writes = 0;
        tempStores = 0;
        delay = 0;
    }

    public static int getDigit(int a, int power, int radix){
        return (int) (a / Math.pow(radix, power)) % radix;
    }

    public static int[] initArr(int [] arr) {
        for (int i = 0; i < arr.length; i++) {
            //NORMAL
            arr[i] = i;
        }
        return arr;
    }

    public static void endSort() {
        calcReal = false;
        clearmarked();

        if(FANCYFINISH) {
            fancyFinish();
        }
    }
    public static void clearmarked(){
        for(int i = 0; i < currentLen; i++) {
            marked.set(i, -5);
        }
    }

    public static void shuffle(int[] array) throws Exception {
        initArr(array);

        String tmp = heading;
        heading = "Shuffling...";

        if(shuffleType.equals("random")) {
            //RANDOM ORDER
            for(int i = 0; i < currentLen; i++){
                swap(array, i, (int)(Math.random()*currentLen), 0, true);
                swaps--;
                if(swaps < 0) {
                    swaps = 0;
                }
                if(SHUFFLEANIM)
                    sleep(1);
            }
        }
        else if (shuffleType.equals("reverse")) {
            //REVERSE ORDER
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                swap(array, left, right, 0, true);
                if(SHUFFLEANIM)
                    sleep(1);
            }
        }
        else if(shuffleType.equals("similar")) {
            for(int i = 0; i < currentLen - 8; i++) {
                array[i] = currentLen / 2;
                if(SHUFFLEANIM)
                    sleep(1);
            }
            for(int i = currentLen - 8; i < currentLen; i++) {
                array[i] = (int) (Math.random() < 0.5 ? currentLen * 0.75 : currentLen * 0.25);
                if(SHUFFLEANIM)
                    sleep(1);
            }
            for(int i = 0; i < currentLen; i++){
                swap(array, i, (int)(Math.random()*currentLen), 0, true);
                swaps--;
                if(swaps < 0) {
                    swaps = 0;
                }
                if(SHUFFLEANIM)
                    sleep(1);
            }
        }
        else if(shuffleType.equals("sorted")) {
            for(int i = 0; i < currentLen; i++) {
                if(SHUFFLEANIM) {
                    marked.set(1, i);
                    sleep(1);
                }
            }
        }
        else if(shuffleType.equals("almost")) {
            for(int i = 0; i < (int) (currentLen / 10); i++){
                swap(array, (int)(Math.random()*currentLen), (int)(Math.random()*currentLen), 0, true);
                swaps--;
                if(swaps < 0) {
                    swaps = 0;
                }
                if(SHUFFLEANIM)
                    sleep(1);
            }
        }
        else {
            throw new Exception("Unrecognized shuffle");
        }
        clearmarked();
        heading = tmp;
    }

    public synchronized static void RunAllSorts() throws Exception{
        if(sortingThread != null)
            while(sortingThread.isAlive()) try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
            }

        SetSound(true);
        sortingThread = new Thread(){
            @Override
            public void run(){
                try{
                    double storeVol = SOUNDMUL;
                    currentLen = 2048;
                    MUTABLE = false;
                    lenLog = (int) (Math.log(currentLen)/Math.log(2));

                    category = "Exchange Sorts";

                    SLEEPRATIO = 2.5; 
                    SOUNDMUL = 1;                    

                    refresharray();
                    heading = "Bubble Sort";
                    SLEEPRATIO = 22;
                    startRealTimer();
                    bubbleSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    SLEEPRATIO = 22;
                    heading = "Cocktail Shaker Sort";
                    startRealTimer();
                    cocktailShakerSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    SLEEPRATIO = 30;
                    heading = "Gnome Sort";
                    startRealTimer();
                    gnomeSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Optimized Gnome Sort";
                    SLEEPRATIO = 30;
                    startRealTimer();
                    smartGnome(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Odd-Even Sort";
                    SLEEPRATIO = 50;
                    startRealTimer();
                    oddEvenSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Comb Sort";
                    SLEEPRATIO = 2.75;
                    startRealTimer();
                    combSort(array, currentLen, false);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Quick Sort";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    quickSort(array, 0, currentLen-1);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Stable Quick Sort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    stableQuickSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Dual-Pivot Quick Sort";
                    SLEEPRATIO = 1.25;
                    startRealTimer();
                    dualPivot(array, 0, currentLen-1);

                    endSort();
                    Thread.sleep(1000);

                    category = "Selection Sorts";

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Selection Sort";
                    SLEEPRATIO = 50;
                    startRealTimer();
                    selectionSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Double Selection Sort";
                    SLEEPRATIO = 25;
                    startRealTimer();
                    doubleSelectionSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Max Heap Sort";
                    SLEEPRATIO = 1.85;
                    startRealTimer();
                    heapSort(array, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Min Heap Sort";
                    SLEEPRATIO = 1.85;
                    startRealTimer();
                    heapSort(array, currentLen, false);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Ternary Heap Sort";
                    SLEEPRATIO = 1.65;
                    startRealTimer();
                    ternaryHeapSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Weak Heap Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    weakHeapSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Smooth Sort";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    smoothSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Cycle Sort";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    cycleSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Tournament Sort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    tournamentSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);            

                    category = "Insertion Sorts";

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Insertion Sort";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    insertionSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Binary Insertion Sort";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    binaryInsertionSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Shell Sort";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    shellSort(array, currentLen, 0);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Patience Sort";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    patienceSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Merge Sorts";

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Merge Sort";
                    SLEEPRATIO = 1.75;
                    startRealTimer();
                    mergeSortOOP(array, currentLen, false);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "In-Place Merge Sort";
                    SLEEPRATIO = 7;
                    startRealTimer();
                    mergeSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    category = "Distribution Sorts";

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "American Flag Sort, 256 Buckets";
                    startRealTimer();
                    flagSort(array, currentLen, 256);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Bead (Gravity) Sort";
                    SLEEPRATIO = Integer.MAX_VALUE;
                    startRealTimer();
                    gravitySort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Counting Sort";
                    SLEEPRATIO = 5;
                    startRealTimer();
                    countingSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Pigeonhole Sort";
                    SLEEPRATIO = 5;
                    startRealTimer();
                    pigeonSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Radix LSD Sort, Base 4";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    radixLSDsort(array, currentLen, 4);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;
                    SOUNDMUL = 0.01;

                    refresharray();
                    heading = "In-Place Radix LSD Sort, Base 16";
                    SLEEPRATIO = 2;
                    startRealTimer();
                    inPlaceRadixLSDSort(array, currentLen, 16);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;
                    SOUNDMUL = 1;

                    refresharray();
                    heading = "Radix MSD Sort, Base 8";
                    SLEEPRATIO = 1.75;
                    startRealTimer();
                    radixMSDSort(array, currentLen, 8);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Flash Sort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    flashSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Shatter Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    shatterSort(array, currentLen, 64);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Simple Shatter Sort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    simpleShatterSort(array, currentLen, 256, 4);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Time Sort (Mul 4)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    timeSort(array, currentLen, 4);

                    endSort();
                    Thread.sleep(1000);

                    category = "Concurrent Sorts";

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Batcher's Bitonic Sort";
                    SLEEPRATIO = 4.5;
                    startRealTimer();
                    bitonicSort(array, 0, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "Batcher's Odd-Even Merge Sort";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    oddEvenMergeSort(array, 0, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Hybrid Sorts";

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Hybrid Comb Sort (Comb/Insertion)";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    combSort(array, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Binary Merge Sort (Merge/Binary Insertion)";
                    SLEEPRATIO = 1.25;
                    startRealTimer();
                    mergeSortOOP(array, currentLen, true);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Weave Merge Sort (Merge/Insertion)";
                    SLEEPRATIO = 2.5;
                    startRealTimer();
                    weaveMergeSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "TimSort";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    timSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "WikiSort (Block Merge Sort)";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    startWikiSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "GrailSort (Block Merge Sort)";
                    SLEEPRATIO = 3;
                    startRealTimer();
                    grailSortWithoutBuffer(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5;

                    refresharray();
                    heading = "std::sort (Introsort)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    introSort(array, currentLen, 32, 1, 0);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "Quick Shell Sort (Introsort with Shellsort)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    introSort(array, currentLen, 48, 2, 12);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 2.5; 

                    refresharray();
                    heading = "std::stable_sort (Insert/Bottom-up Merge)";
                    SLEEPRATIO = 1.5;
                    startRealTimer();
                    stableSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Miscellaneous Sorts";
                    currentLen = 256;
                    SLEEPRATIO = 1; 

                    refresharray();
                    heading = "Pancake Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    pancakeSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Impractical Sorts";

                    refresharray();
                    heading = "Stooge Sort";
                    SLEEPRATIO = 2.5;
                    startRealTimer();
                    stoogeSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 1;

                    refresharray();
                    heading = "Bad Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    badSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    refresharray();
                    heading = "Silly Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    sillySort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    refresharray();
                    heading = "Slow Sort";
                    SLEEPRATIO = 1;
                    startRealTimer();
                    slowSort(array, 0, currentLen - 1);

                    endSort();
                    Thread.sleep(1000);

                    refresharray();
                    heading = "Less Bogo Sort";
                    SLEEPRATIO = 300;
                    startRealTimer();
                    lessBogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    SLEEPRATIO = 1;

                    refresharray();
                    heading = "Cocktail Bogo Sort";
                    startRealTimer();
                    SLEEPRATIO = 300;
                    doubleBogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    currentLen = 8;
                    SLEEPRATIO = 0.01;

                    refresharray();
                    heading = "Bogo Sort";
                    startRealTimer();
                    SLEEPRATIO = 5;
                    bogoSort(array, currentLen);

                    endSort();
                    Thread.sleep(1000);

                    category = "Run All";
                    heading = "Done";

                    MUTABLE = true;
                    SOUNDMUL = storeVol;
                }catch (Exception e){}
                SetSound(false);
            }
        };
        sortingThread.start();
    }

    public static void ReportComparativeSort(int n){
        if(sortingThread != null && sortingThread.isAlive())
            return;

        if(SKIPPED) {
            SLEEPRATIO = 1;
            SKIPPED = false;
        }

        SetSound(true);
        sortingThread = new Thread(){
            public void run(){
                try{
                    boolean goAhead;
                    MUTABLE = false;
                    lenLog = (int) (Math.log(currentLen)/Math.log(2));
                    refresharray();
                    heading = ComparativeSorts[n] + " Sort";
                    startRealTimer();
                    switch (n){
                    case 0:
                        category = "Selection Sorts";
                        badSort(array, currentLen);
                        break;
                    case 1:
                        category = "Insertion Sorts";
                        binaryInsertionSort(array, currentLen);
                        break;
                    case 2:
                        category = "Hybrid Sorts";
                        mergeSortOOP(array, currentLen, true);
                        break;
                    case 3:
                        category = "Concurrent Sorts";
                        bitonicSort(array, 0, currentLen, true);
                        break;
                    case 4:
                        category = "Hybrid Sorts";
                        stableSort(array, currentLen);
                        break;
                    case 5:
                        category = "Exchange Sorts";
                        bubbleSort(array, currentLen);
                        break;
                    case 6:
                        category = "Exchange Sorts";
                        cocktailShakerSort(array, currentLen);
                        break;
                    case 7:
                        category = "Exchange Sorts";
                        combSort(array, currentLen, false);
                        break;
                    case 8:
                        category = "Selection Sorts";
                        cycleSort(array, currentLen);
                        break;
                    case 9:
                        category = "Selection Sorts";
                        doubleSelectionSort(array, currentLen);
                        break;
                    case 10:
                        category = "Exchange Sorts";
                        dualPivot(array, 0, currentLen - 1);
                        break;
                    case 11:
                        category = "Exchange Sorts";
                        gnomeSort(array, currentLen);
                        break;
                    case 12:
                        category = "Hybrid Sorts";
                        grailSortWithoutBuffer(array, currentLen);
                        break;
                    case 13:
                        category = "Hybrid Sorts";
                        combSort(array, currentLen, true);
                        break;
                    case 14:
                        category = "Insertion Sorts";
                        insertionSort(array, currentLen);
                        break;
                    case 15:
                        category = "Hybrid Sorts";
                        introSort(array, currentLen, 32, 1, 0);
                        break;
                    case 16:
                        category = "Selection Sorts";
                        heapSort(array, currentLen, true);
                        break;
                    case 17:
                        category = "Merge Sorts";
                        mergeSortOOP(array, currentLen, false);
                        break;
                    case 18:
                        category = "Merge Sorts";
                        mergeSort(array, 0, currentLen - 1);
                        break;
                    case 19:
                        category = "Selection Sorts";
                        heapSort(array, currentLen, false);
                        break;
                    case 20:
                        category = "Exchange Sorts";
                        oddEvenSort(array, currentLen);
                        break;
                    case 21:
                        category = "Concurrent Sorts";
                        oddEvenMergeSort(array, 0, currentLen);
                        break;
                    case 22:
                        if(currentLen > 64) {
                            SLEEPRATIO = 10;
                        }
                        category = "Miscellaneous Sorts";
                        pancakeSort(array, currentLen);
                        break;
                    case 23:
                        category = "Insertion Sorts";
                        patienceSort(array, currentLen);
                        break;
                    case 24:
                        category = "Exchange Sorts";
                        quickSort(array, 0, currentLen - 1);
                        break;
                    case 25:
                        category = "Hybrid Sorts";
                        introSort(array, currentLen, 48, 2, 12);
                        break;
                    case 26:
                        category = "Selection Sorts";
                        selectionSort(array, currentLen);
                        break;
                    case 27:
                        category = "Insertion Sorts";
                        shellSort(array, currentLen, 0);
                        break;
                    case 28:
                        goAhead = false;
                        if(currentLen > 256) {
                            Object[] options = { "Let's see how bad Silly Sort is!", "Cancel" };
                            int n = JOptionPane.showOptionDialog(window, "Even at a high speed, Silly Sorting " + currentLen + 
                                    " numbers will not finish in a reasonable amount of time. "
                                    + "Are you sure you want to continue?", "Warning!", 2, 
                                    JOptionPane.WARNING_MESSAGE, null, options, options[1]);

                            if(n == 0) goAhead = true;
                            else goAhead = false;
                        }
                        else goAhead = true;

                        if(goAhead) {
                            category = "Exchange Sorts";
                            sillySort(array, 0, currentLen - 1);
                            break;
                        }
                        else break;
                    case 29:
                        goAhead = false;
                        if(currentLen > 256) {
                            Object[] options = { "Let's see how bad Slow Sort is!", "Cancel" };
                            int n = JOptionPane.showOptionDialog(window, "Even at a high speed, Slow Sorting " + currentLen + 
                                    " numbers will not finish in a reasonable amount of time. "
                                    + "Are you sure you want to continue?", "Warning!", 2, 
                                    JOptionPane.WARNING_MESSAGE, null, options, options[1]);

                            if(n == 0) goAhead = true;
                            else goAhead = false;
                        }
                        else goAhead = true;

                        if(goAhead) {
                            category = "Exchange Sorts";
                            slowSort(array, 0, currentLen - 1);
                            break;
                        }
                        else break;
                    case 30:
                        category = "Exchange Sorts";
                        smartGnome(array, currentLen);
                        break;
                    case 31:
                        category = "Selection Sorts";
                        smoothSort(array, currentLen);
                        break;
                    case 32:
                        category = "Exchange Sorts";
                        stableQuickSort(array, currentLen);
                        break;
                    case 33:
                        goAhead = false;
                        if(currentLen > 1024) {
                            Object[] options = { "Let's see how bad Stooge Sort is!", "Cancel" };
                            int n = JOptionPane.showOptionDialog(window, "Even at a high speed, Stooge Sorting " + currentLen + 
                                    " numbers will not finish in a reasonable amount of time. "
                                    + "Are you sure you want to continue?", "Warning!", 2, 
                                    JOptionPane.WARNING_MESSAGE, null, options, options[1]);

                            if(n == 0) goAhead = true;
                            else goAhead = false;
                        }
                        else goAhead = true;

                        if(goAhead) {
                            category = "Exchange Sorts";
                            stoogeSort(array, 0, currentLen - 1);
                            break;
                        }
                        else break;
                    case 34:
                        category = "Selection Sorts";
                        ternaryHeapSort(array, currentLen);
                        break;
                    case 35:
                        category = "Hybrid Sorts";
                        timSort(array, currentLen);
                        break;
                    case 36:
                        category = "Selection Sorts";
                        tournamentSort(array, currentLen);
                        break;
                    case 37:
                        category = "Selection Sorts";
                        weakHeapSort(array, currentLen);
                        break;
                    case 38:
                        category = "Hybrid Sorts";
                        weaveMergeSort(array, 0, currentLen - 1);
                        break;
                    case 39:
                        category = "Hybrid Sorts";
                        startWikiSort(array, currentLen);
                        break;
                    }
                    MUTABLE = true;
                    endSort();
                }catch(Exception e){e.printStackTrace();}
                SetSound(false);
            }
        };
        sortingThread.start();
    }

    public static void ReportDistributiveSort(int n) {
        if(sortingThread != null && sortingThread.isAlive())
            return;

        if(SKIPPED) {
            SLEEPRATIO = 1;
            SKIPPED = false;
        }

        double storeVol = SOUNDMUL;
        category = "Distributive Sorts";

        int bas = 10;
        if(n==8||n==9||n==10) {
            try{bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Base for Sort"));}catch(Exception e){}
            SOUNDMUL = .25;
        }
        else if(n==0||n==11||n==12) {
            try{bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Size/Number of Partitions"));}catch(Exception e){}
            SOUNDMUL = .25;
        }
        else {
            try{bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Delay per Element in Milliseconds"));}catch(Exception e){}
            SOUNDMUL = .25;
        }

        if(bas < 2 && n != 13) base = 2;
        else base = bas;

        num = n;
        SetSound(true);
        sortingThread = new Thread(){
            @Override
            public void run(){
                try {
                    boolean goAhead;
                    MUTABLE = false;
                    lenLog = (int) (Math.log(currentLen)/Math.log(2));
                    refresharray();
                    heading = DistributiveSorts[num]+" Sort";
                    startRealTimer();
                    switch (num){
                    case 0:
                        flagSort(array, currentLen, base);
                        break;
                    case 1:
                        goAhead = false;
                        if(currentLen > 8) {
                            Object[] options = { "Let's see how bad Bogo Sort is!", "Cancel" };
                            int n = JOptionPane.showOptionDialog(window, "Even at a high speed, Bogo Sorting " + currentLen + 
                                    " numbers will almost certainly not finish in a reasonable amount of time. "
                                    + "Are you sure you want to continue?", "Warning!", 2, 
                                    JOptionPane.WARNING_MESSAGE, null, options, options[1]);

                            if(n == 0) goAhead = true;
                            else goAhead = false;
                        }
                        else goAhead = true;

                        if(goAhead) {
                            bogoSort(array, currentLen);
                            break;
                        }
                        else break;
                    case 2:
                        goAhead = false;
                        if(currentLen > 256) {
                            Object[] options = { "Let's see how bad Cocktail Bogo Sort is!", "Cancel" };
                            int n = JOptionPane.showOptionDialog(window, "Even at a high speed, Cocktail Bogo Sorting " 
                                    + currentLen + " numbers will almost certainly not finish in a reasonable amount "
                                    + "of time. Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE, 
                                    null, options, options[1]);

                            if(n == 0) {
                                goAhead = true;
                                SLEEPRATIO = 1000;
                            }
                            else goAhead = false;
                        }
                        else goAhead = true;

                        if(goAhead) {
                            doubleBogoSort(array, currentLen);
                            break;
                        }
                        else break;
                    case 3:
                        countingSort(array, currentLen);
                        break;
                    case 4:
                        flashSort(array, currentLen);
                        break;
                    case 5:
                        gravitySort(array, currentLen);
                        break;
                    case 6:
                        goAhead = false;
                        if(currentLen > 256) {
                            Object[] options = { "Let's see how bad Less Bogo Sort is!", "Cancel" };
                            int n = JOptionPane.showOptionDialog(window, "Even at a high speed, Less Bogo Sorting " 
                                    + currentLen + " numbers will almost certainly not finish in a reasonable amount "
                                    + "of time. Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE, 
                                    null, options, options[1]);

                            if(n == 0) {
                                goAhead = true;
                                SLEEPRATIO = 1000;
                            }
                            else goAhead = false;
                        }
                        else goAhead = true;

                        if(goAhead) {
                            lessBogoSort(array, currentLen);
                            break;
                        }
                        else break;
                    case 7:
                        pigeonSort(array, currentLen);
                        break;
                    case 8:
                        radixLSDsort(array, currentLen, base);
                        break;
                    case 9:
                        SOUNDMUL = 0.01;
                        inPlaceRadixLSDSort(array, currentLen, base);
                        break;
                    case 10:
                        radixMSDSort(array, currentLen, base);
                        break;
                    case 11:
                        shatterSort(array, currentLen, base);
                        break;
                    case 12:
                        simpleShatterSort(array, currentLen, base, 8);
                        break;
                    case 13:
                        goAhead = false;
                        Object[] options = { "Continue", "Cancel" };
                        int n = JOptionPane.showOptionDialog(window, "Time Sort will take at least " 
                                + formatter.format((double) (currentLen * base) / 1000) + " seconds to complete. Once it starts,"
                                + " you cannot skip this sort. Proceed?", "Warning!", 2, JOptionPane.WARNING_MESSAGE, null, 
                                options, options[1]);

                        if(n == 0) goAhead = true;
                        else goAhead = false;

                        if(goAhead) {
                            timeSort(array, currentLen, base);
                            break;
                        }
                        else break;
                    }
                    MUTABLE = true;
                    endSort();
                    SOUNDMUL = storeVol;
                }catch(Exception e){}
                SetSound(false);
            }
        };
        sortingThread.start();
    }
}