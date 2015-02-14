package array.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;

import static array.visualizer.Swaps.*;
import static array.visualizer.BitonicSort.*;
import static array.visualizer.BubbleSort.*;
import static array.visualizer.CocktailShaker.*;
import static array.visualizer.CountingSort.*;
import static array.visualizer.DoubleSelection.*;
import static array.visualizer.GravitySort.*;
import static array.visualizer.InsertionSort.*;
import static array.visualizer.MergeSort.*;
import static array.visualizer.MergeSortOOP.*;
import static array.visualizer.QuickSort.*;
import static array.visualizer.RadixLSD.*;
import static array.visualizer.RadixMSD.*;
import static array.visualizer.Searches.*;
import static array.visualizer.SelectionSort.*;
import static array.visualizer.ShatterSorts.*;
import static array.visualizer.Swaps.*;
import static array.visualizer.TimeSort.*;
import static array.visualizer.RadixLSDInPlace.*;
import static java.lang.Thread.sleep;

public class ArrayVisualizer {

    static final int [] array = new int[1000];
    static final JFrame window = new JFrame();
    
    static String heading = "";
    static final ArrayList<Integer> marked = new ArrayList();
    static int frames;
    static int aa = 0;
    static int snd = 0;
    static int comps = 0;
    static long nanos;
    static Font fon = new Font("TimesRoman",Font.PLAIN,(int)(640/1280.0*25));
    static boolean CIRCLEDRAW = false;
    static boolean COLORONLY = false;
    static boolean PIXELDRAW = false;
    
    public static double calcVel(){
        double count = 1;
        for(int i : marked)
            if(i!=-5)
                count+=0.75;
        return count;
    }
    
    static boolean add = false;
    public static void sleep(long milis){
        if(milis <= 0)
            return;
        long amt = (long)(milis/(array.length/1000.0));
        if(amt == 0)
            amt+=add ? 1 : 0;
        add = !add;
        amt = (long)(amt*2);
        try{
            Thread.sleep(amt);
        }catch(Throwable t){}
    }
    
    public static void main(String[] args) throws Exception {

        final Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        synth.loadAllInstruments(synth.getDefaultSoundbank());
        final MidiChannel chan = synth.getChannels()[0];
        chan.programChange(synth.getLoadedInstruments()[18].getPatch().getProgram());
        chan.setSolo(true);
        
        for(int i = 0; i < array.length; i++)
            marked.add(-5);
        rianr(array);
        window.setSize(new Dimension(640,480));
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Array Visualizer");
        
        //AUDIO THREAD
        new Thread(){
            public void run(){
                while(true){
                chan.allNotesOff();
                int tmp = 1;
                    for(int i : marked)
                        if(i != -5)
                            tmp++;
                    for(int i : marked)
                        if(i != -5){
                            int pitch = (int)Math.round((double)array[Math.min(Math.max(i, 0),array.length-1)]/array.length*97+15);
                            int vel = (int)(((128-pitch)/192.0+0.333)   *   (64.0/Math.pow(tmp,0.58)));
                            chan.noteOn(pitch, vel);
                        }
                            //((int)((127-(array[Math.min(Math.max(i, 0),array.length-1)]/16.0))/Math.sqrt(calcVel())))*5
                            //double tmp = (array[Math.min(Math.max(i, 0),array.length-1)]/32.0+47);
                            //chan.setPitchBend(8192*2-(int)((tmp-Math.floor(tmp))*8192*2));
                    /*
                    ArrayList<Integer> tmp = new ArrayList<Integer>();
                    for(int i : marked)
                        if(i != -5)
                            tmp.add(i);
                    
                    if(tmp.size() > 0){
                        do{
                            int i = tmp.get(snd%tmp.size());
                            snd++;
                        chan.noteOn(array[Math.min(Math.max(i, 0),array.length-1)]/32+47, 127);
                        double tmpd = (array[Math.min(Math.max(i, 0),array.length-1)]/32.0+47);
                        chan.setPitchBend(8192*2-(int)((tmpd-Math.floor(tmpd))*8192*2));
                        }while(false);}*/
                    try{sleep(1);}catch(Exception e){}
                }
            }
        }.start();
        
        //DRAW THREAD
        new Thread(){
            @Override
            public void run(){
                int cw = window.getWidth();
                int ch = window.getHeight();
                Image img = window.createVolatileImage(cw, ch);
                //Graphics g = window.getGraphics();
                Graphics g = img.getGraphics();
                double xscl, yscl;
                while(true){
                    
                    if(window.getWidth()!=cw|| window.getHeight()!=ch){
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        fon = new Font("TimesRoman",Font.PLAIN,(int)(cw/1280.0*25));
                        g = img.getGraphics();
                    }
                    int gamt = 32;//(int)(frames/1000.0%64);
                    g.setColor(new Color(gamt,gamt,gamt));
                    g.fillRect(0,0,img.getWidth(null),img.getHeight(null));
                    xscl = (double)window.getWidth()/array.length;
                    yscl = (double)(window.getHeight()-30)/array.length;
                    int amt = 0;
                    int circamt = array.length/2;
                    frames++;
                    if(CIRCLEDRAW)
                        for(int i = 0; i < array.length; i++){
                            if(i == 0 || marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i]));
                            //COLOR ONLY NO LENGTH
                            if(COLORONLY)
                                g.drawLine(window.getWidth()/2, window.getHeight()/2, window.getWidth()/2+(int)(Math.sin(i*Math.PI/circamt)*(window.getWidth()-64)/2.0), window.getHeight()/2-(int)(Math.cos(i*Math.PI/circamt)*(window.getHeight()-96)/2.0));
                            
                            else if(PIXELDRAW)
                                g.drawRect(window.getWidth()/2+(int)(Math.sin(i*Math.PI/circamt)*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), window.getHeight()/2-(int)(Math.cos(i*Math.PI/circamt)*((window.getHeight()-96)/2.0*(array[i]/1000.0))), 1, 1);
                            //LENGTH AND COLOR
                            else
                                g.drawLine(window.getWidth()/2, window.getHeight()/2, window.getWidth()/2+(int)(Math.sin(i*Math.PI/circamt)*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), window.getHeight()/2-(int)(Math.cos(i*Math.PI/circamt)*((window.getHeight()-96)/2.0*(array[i]/1000.0))));
                        }
                    else
                        for(int i = 0; i < array.length; i++){
                            if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i]));
                            
                            int y = 0;
                            int width = (int)(xscl*i)-amt;
                            
                            if(width>0){
                                if(COLORONLY){
                                    y = (int)(window.getHeight()-750*yscl);
                                    g.fillRect(amt, y, width, Math.max((int)(750*yscl),1));
                                    g.setColor(getRevColor());
                                    g.fillRect((int)(i*xscl), y, width, 6);
                                }
                                else if(PIXELDRAW){
                                    y = (int)(window.getHeight()-array[i]*yscl);
                                    g.fillRect(amt, y, width, 3);
                                }
                                else{
                                    y = (int)(window.getHeight()-array[i]*yscl);
                                    g.fillRect(amt, y, width, Math.max((int)(array[i]*yscl),1));
                                    g.setColor(getRevColor());
                                    g.fillRect(amt, y, width, 6);
                                }
                            }
                            amt+=width;
                        }
                    
                    int coltmp = 255;//(int)Math.abs(Math.sin(frames*0.01)*255);
                    g.setColor(new Color(coltmp,coltmp,coltmp));
                    Font f = g.getFont();
                    g.setFont(fon);
                    g.drawString(heading, 10, (int)(cw/1280.0*20)+30);
                    g.drawString("Comparisons: "+comps+" Array Accesses: "+aa, 10, (int)(cw/1280.0*40)+30);
                    g.setFont(f);
                    Graphics g2 = window.getGraphics();
                    g2.setColor(Color.BLACK);
                    g2.drawImage(img, 0, 0, null);
                }
            }
            
            public Color getIntColor(int i) {
                return Color.getHSBColor(((float)i/array.length), 1.0F, 1.0F);
            }
            public Color getRevColor(){
                return getIntColor((int)(Math.sin(frames/66.67)*array.length));
            }
        }.start();
        
        while(true){
            clearmarked();
            
            //heading = "Linear Search";
            
            //Arrays.sort(array);
            //marked.set(1,730);
            //linearSearch(730);
            //refresharray();
            
            //heading = "Binary Search";
            
            //Arrays.sort(array);
            //marked.set(1, 730);
            //binarySearch(730);
            
            //heading = "Shatter-Time Sort";

            //timeSort();

            //chan.allNotesOff();
            //refresharray();

            heading = "Insertion Sort";

            insertionSort();

            chan.allNotesOff();
            refresharray();
            heading = "Bubble Sort";

            bubbleSort();

            chan.allNotesOff();
            refresharray();
            heading = "Selection Sort";

            selectionSort();

            chan.allNotesOff();
            refresharray();
            heading = "Cocktail Shaker Sort";

            cocktailShakerSort();

            chan.allNotesOff();
            refresharray();
            heading = "Merge Sort In-Place";

            mergeSort(0, array.length - 1);

            chan.allNotesOff();
            refresharray();
            heading = "Merge Sort Out-of-Place";
            
            mergeSortOP();
            
            chan.allNotesOff();
            refresharray();
            
            heading = "Gravity Sort (Abacus/Bead)";
            
            gravitySort();
            
            chan.allNotesOff();
            refresharray();

            heading = "Quick Sort";

            quickSort(array, 0, array.length-1);

            chan.allNotesOff();
            refresharray();
            heading = "Counting Sort";
            
            countingSort();
            
            chan.allNotesOff();
            refresharray();
            heading = "Radix LSD Sort";

            radixLSDsort(4);

            chan.allNotesOff();
            refresharray();
            heading = "Radix LSD In-Place Sort";

            inPlaceRadixLSDSort(10);

            chan.allNotesOff();
            refresharray();
            heading = "Radix MSD Sort";
            
            radixMSDSort(4);
            refresharray();
            //heading = "Shatter Partition";

            //shatterPartition(1);

            //chan.allNotesOff();
            //refresharray();
            heading = "Shatter Sort";

            shatterSort(128);

            chan.allNotesOff();
            refresharray();
            //heading = "Simple Shatter Sort";

            //simpleShatterSort(128, 4);

            //chan.allNotesOff();
            //refresharray();
        }
    }
    
    public static void refresharray() throws Exception {
        clearmarked();
        boolean solved = true;
        for(int i = 0; i < array.length; i++){
            if(array[i]!=i)
                solved = false;
            marked.set(0,i);
            sleep(sleepTime(0.001));
        }
        System.out.println(solved);
        marked.set(0, -5);
        Thread.sleep(1000);
        shuffle(array);
        aa = 0;
        comps = 0;
        clearmarked();
    }
    
    public static int getDigit(int a, int power, int radix){
        return (int) (a / Math.pow(radix, power)) % radix;
    }
    
    public static int[] rianr(int [] arr) {
        for (int i = 0; i < arr.length; i++)
            arr[i] = i;
        shuffle(arr);
        return arr;
    }
    
    public static void clearmarked(){
        for(int i = 0; i < array.length; i++)
            marked.set(i, -5);
    }

    public static void shuffle(int[] array) {
        for(int i = 0; i < array.length; i++)
            swap(array, i, (int)(Math.random()*array.length));
    }
    
    /*public static void insertionSort(int slp) {
        int pos;
        for(int i = 1; i < array.length; i++){
            pos = i;
            marked.set(0, i);
            while(pos>0&&array[pos]<=array[pos-1]){
                    swap(array, pos, pos-1,slp);
                    pos--;
            }
        }
    }*/

    public static int sleepTime(double d) {
        return (int)(array.length*d)/4;
    }
}
