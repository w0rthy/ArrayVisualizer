package array.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
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
import static array.visualizer.BinaryQuickSort.*;
import static array.visualizer.RadixLSD.*;
import static array.visualizer.RadixMSD.*;
import static array.visualizer.Searches.*;
import static array.visualizer.SelectionSort.*;
import static array.visualizer.ShatterSorts.*;
import static array.visualizer.Swaps.*;
import static array.visualizer.TimeSort.*;
import static array.visualizer.WeaveMerge.*;
import static array.visualizer.RadixLSDInPlace.*;
import static array.visualizer.BogoSort.*;
import static array.visualizer.HeapSort.*;
import static array.visualizer.ShellSort.*;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.swing.JOptionPane;

public class ArrayVisualizer {

    static final int [] array = new int[1000];
    static final JFrame window = new JFrame();
    
    static String heading = "";
    static final ArrayList<Integer> marked = new ArrayList();
    static int frames;
    static long aa = 0;
    static int snd = 0;
    static long comps = 0;
    static long nanos;
    static Font fon = new Font("TimesRoman",Font.PLAIN,(int)(640/1280.0*25));
    
    static boolean CIRCLEDRAW = false;
    static boolean COLORONLY = false;
    static boolean PIXELDRAW = false;
    static boolean DISPARITYDRAW = false;
    static boolean LINKEDPIXELDRAW = false;
    static boolean MESHDRAW = false;
    static boolean PYRAMIDDRAW = false;
    static boolean DRAWFLIPPED = false;
    static boolean XMASDRAW = false;
    
    static boolean SOUND = false;
    static double SOUNDMUL = 1.0;
    static double SLEEPRATIO = 1.0;
    static UtilFrame uf;
    static ViewPrompt v;
    static Synthesizer synth;
    static MidiChannel chan;
    static Thread sortingThread;
    static boolean SHUFFLEANIM = true;
    
    static long starttime = 0;
    static long stoptime = 0;
    static boolean running = false;
    static long sleeptime = 0;
    
    static long rtupdatefreq = 100; //How frequently to update the real time display in ms
    static double realt = 0d;
    static long lastrtupdate = 0;
    
    static int COLORSTRAT = 1; //0 = Solid, 1 = Rainbow, 2 = Segments
    static Color COLORSTRAT0col = new Color(0,204,0);
    static ArrayList<Color> COLORSTRAT2cols = new ArrayList<Color>();
    
    static String[] ComparativeSorts = "Selection!Bubble!Insertion!Double Selection!Cocktail Shaker!Quick!Merge!Merge OOP!Weave Merge!Max Heap!Shell".split("!");
    static String[] DistributiveSorts = "Radix LSD!Radix MSD!Radix LSD In-Place!Binary Quick!Gravity!Shatter!Counting!Time!Bogo".split("!");
    
    static int cx = 0;
    static int cy = 0;
    
    public static double calcVel(){
        double count = 1;
        for(int i : marked)
            if(i!=-5)
                count+=0.75;
        return count;
    }
    
    public static synchronized void SetSound(boolean val){
        SOUND = val;
    }
    
    static double addamt = 0.0;
    public static void sleep(double milis){
        if(milis <= 0)
            return;
        double tmp = (milis*(1000.0/array.length));
        tmp = tmp * (1/SLEEPRATIO);
        addamt += tmp;
        if(addamt<1.0)
            return;
        try{
            long actual = System.nanoTime();
            Thread.sleep((long)addamt);
            actual = (System.nanoTime()-actual);
            addamt-=(double)actual/1000000.0;
            if(running)
                sleeptime+=actual;
        }catch(Throwable t){}
    }
    
    public static void main(String[] args) throws Exception {
        
        //Segment Colors
        COLORSTRAT2cols.add(new Color(0,204,0));
        COLORSTRAT2cols.add(new Color(204,0,0));
        //COLORSTRAT2cols.add(new Color(204,204,0));

        synth = MidiSystem.getSynthesizer();
        synth.open();
        synth.loadAllInstruments(synth.getDefaultSoundbank());
//        int s = 0;
//        for(Instrument i : synth.getAvailableInstruments()){
//            System.out.println(s+" "+i.getName());
//            s++;
//        }
        chan = synth.getChannels()[0];
        for(Instrument i : synth.getLoadedInstruments())
            if(i.getName().toLowerCase().trim().contains("sine")){
                chan.programChange(i.getPatch().getProgram());
                break;
            }
        
        if(chan.getProgram() == 0)
            JOptionPane.showMessageDialog(null, "Could not find a valid instrument. Sound is disabled");
        //chan.programChange(synth.getLoadedInstruments()[197].getPatch().getProgram());
        
        for(int i = 0; i < array.length; i++)
            marked.add(-5);
        rianr(array);
        
        window.setSize(new Dimension(640,480));
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Array Visualizer");
        
        uf = new UtilFrame(window);
        
        //AUDIO THREAD
        new Thread(){
            @Override
            public void run(){
                while(true){
                chan.allNotesOff();
                if(SOUND == false){
                    continue;
                }
                
                int tmp = 1;
                    for(int i : marked)
                        if(i != -5)
                            tmp++;
                    for(int i : marked)
                        if(i != -5){
                            int pitch = (int)Math.round((double)array[Math.min(Math.max(i, 0),array.length-1)]/array.length*96+16);
                            //int vel = (int)(((128-pitch)/320.0+0.4)   *   (128.0/Math.pow(tmp,0.33)));
                            //int vel = (int)(64.0/Math.pow(tmp,0.25));
                            //int vel = (int)((64.0-Math.pow((tmp-1)*10,0.67d))*SOUNDMUL);
                            int vel = (int)(64.0/(Math.pow((double)tmp,0.6)) * ((128-pitch)/64.0+1.0)*0.67*SOUNDMUL); //one day
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
                int cw = 640;
                int ch = 480;
                Image img = window.createVolatileImage(cw, ch);
                //Graphics g = window.getGraphics();
                Graphics2D g = (Graphics2D)img.getGraphics();
                
                double xscl, yscl;
                while(true){
                    
                    if(window.getWidth()!=cw|| window.getHeight()!=ch || window.getX() != cx || window.getY() != cy){
                        uf.reposition();
                        if(v != null && v.isVisible())
                            v.reposition();
                        cx = window.getX();
                        cy  = window.getY();
                    }
                    
                    if(window.getWidth()!=cw|| window.getHeight()!=ch){
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        fon = new Font("TimesRoman",Font.PLAIN,(int)(cw/1280.0*25));
                        g = (Graphics2D)img.getGraphics();
                        
                    }
                    int gamt = 32;//(int)(frames/1000.0%64);
                    g.setColor(new Color(gamt,gamt,gamt));
                    g.fillRect(0,0,img.getWidth(null),img.getHeight(null));
                    xscl = (double)window.getWidth()/array.length;
                    yscl = (double)(window.getHeight()-30)/array.length;
                    int amt = 0;
                    int circamt = array.length/2;
                    int linkedpixdrawx = 0;
                    int linkedpixdrawy = 0;
                    frames++;
                    
                    int halfwidth = window.getWidth()/2;
                    int halfheight = window.getHeight()/2;
                    int dotw = (int)(2*(window.getWidth()/640.0));
                    int doth = (int)(2*(window.getHeight()/480.0));
                    
                    int sortErrors = 0;
                    
                    float strokew = 3f*(window.getWidth()/1920f);
                    g.setStroke(new BasicStroke(strokew));
                    
                    //CHRISTMAS TREE?
                    if(XMASDRAW){
                        
                        double trunksize = (double)ch*0.1;
                        int trunkstart = array.length-(int)(trunksize/yscl);
                        
                        double ornamentsize = (double)ch*0.1;
                        int ornamentend = (int)(ornamentsize/yscl);
                        
                        int width = 0;
                        
                        for(int i = 0; i < array.length; i++){
                            //Check for sort errors in array
                            if(i>0 && array[i]<array[i-1])
                                sortErrors++;
                            
                            int j = array[i];
                            
                            if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else if(j<ornamentend)
                                g.setColor(new Color(255,0,0));
                            else if(j>trunkstart)
                                g.setColor(new Color(102,34,0));
                            else
                                g.setColor(new Color(0,102,0));
                            
                            if(j<ornamentend){ //ORNAMENT
                                width = (int)(Math.sin(Math.acos((j*yscl)/ornamentsize*2.0-1.0))*ornamentsize);
                            }else if(j>trunkstart){ //TRUNK
                                width = (int)(cw*0.2);
                            }else //TREE
                                width = (int)((((j-ornamentend)%85)*0.005+0.075)*cw);
                            
                            int step = (int)(i*yscl+yscl)-(int)(i*yscl);
                            g.fillRect(halfwidth-width/2, (int)(i*yscl)+32, width, step);
                            
                        }
                    }
                    //HOOP DRAWING (PYRAMID CIRCLES)
                    else if(PYRAMIDDRAW && CIRCLEDRAW){
                        
                        g.setStroke(new BasicStroke(1.0f)); //significantly increased performance
                        
                        double maxdiam = (double)Math.min(cw, ch-32);
                        double diameter = maxdiam;
                        double diamstep = Math.min(xscl,yscl);
                        
                        for(int i = array.length-1; i >= 0; i--){
                            //Check for sort errors in array
                            if(i>0 && array[i]<array[i-1])
                                sortErrors++;
                            
                            //Set Proper Color
                            if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i],sortErrors));
                            
                            int radius = (int)(diameter/2.0);
                            
                            if(!DISPARITYDRAW)
                                g.drawOval(halfwidth-radius, halfheight-radius+15, (int)diameter, (int)diameter);
                            else{
                                int dist = Math.abs(i-array[i]);
                                double disparity = 1.0-((double)Math.min(dist,array.length-dist)/((double)array.length/2.0));
                                int actualdiam = (int)(disparity*diameter);
                                g.drawOval(halfwidth-actualdiam/2, halfheight-radius+15, (int)actualdiam, (int)actualdiam);
                            }
                            diameter-=diamstep;
                        }
                    }
                    //PYRAMID DRAW METHOD
                    else if(PYRAMIDDRAW){
                        
                        //Setup
                        double curpos = 0.0;
                        
                        //Draw loop
                        for(int i = 0; i < array.length; i++){
                            //Check for sort errors in array
                            if(i>0 && array[i]<array[i-1])
                                sortErrors++;
                            
                            //Set Proper Color
                            if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i],sortErrors));
                            
                            //Draw
                            if(!DRAWFLIPPED){
                                int len = (int)((double)array[i]/(double)array.length*(double)cw);
                                int step = (int)(curpos+yscl) - (int)curpos;
                                g.fillRect(halfwidth-len/2, (int)curpos+32, len, step);
                                curpos += yscl;
                            }else{
                                int len = (int)((double)array[i]/(double)array.length*((double)ch-32.0));
                                int step = (int)(curpos+xscl) - (int)curpos;
                                g.fillRect((int)curpos,halfheight-len/2+16,step,len);
                                curpos += xscl;
                            }
                        }
                    }
                    //MESH DRAW METHOD
                    else if(MESHDRAW){
                        
                        int trih = window.getHeight()/20; //Height of triangles to use, Width will be scaled accordingly
                        
                        int tripercol = window.getHeight()/trih*2; //Triangles per vertical column
                        int triperrow = array.length/tripercol; //Triangles per horizontal row
                        double triw = (double)window.getWidth()/triperrow; //Width of triangles to use
                        
                        double curx = 0;
                        int cury = 0;
                        
                        int[] triptsx = new int[3];
                        int[] triptsy = new int[3];
                        
                        for(int i = 0; i < array.length; i++){
                            if(i>0 && array[i]<array[i-1])
                                sortErrors++;
                            
                            if(marked.contains(i)/*||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3)*/)
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i],sortErrors));
                            
                            //If i/triperrow is even, then triangle points right, else left
                            boolean direction = false;
                            if((i/triperrow)%2==0)
                                direction = true;
                            
                            //Make the triangle
                            if(!direction){
                                //Pointing right
                                triptsx[0] = (int)curx;
                                triptsx[1] = (int)curx;
                                curx+=triw;
                                triptsx[2] = (int)curx;
                                
                                if(!COLORONLY)
                                    triptsx[2] = (int)(triptsx[1]+triw*array[i]/array.length);
                                
                                triptsy[0] = cury;
                                triptsy[2] = cury + trih/2;
                                triptsy[1] = cury + trih;
                            }else{
                                //Pointing left
                                triptsx[2] = (int)curx;
                                curx+=triw;
                                triptsx[0] = (int)curx;
                                triptsx[1] = (int)curx;
                                
                                if(!COLORONLY)
                                    triptsx[2] = (int)(triptsx[0]-triw*array[i]/array.length);
                                
                                triptsy[0] = cury;
                                triptsy[2] = cury + trih/2;
                                triptsy[1] = cury + trih;
                            }
                            
                            //Draw it
                            g.fillPolygon(triptsx,triptsy,triptsx.length);
                            
                            //If at the end of a row, reset curx
                            if(i != 0 && i%triperrow == 0){
                                curx = 0d;
                                cury+=trih/2;
                            }
                        }
                    }
                    else if(CIRCLEDRAW)
                        for(int i = 0; i < array.length; i++){
                            if(i>0 && array[i]<array[i-1])
                                sortErrors++;
                            
                            if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i],sortErrors));
                            
                            double sinval = Math.sin(i*Math.PI/circamt);
                            double cosval = Math.cos(i*Math.PI/circamt);
                            
                            //COLOR ONLY NO LENGTH
                            if(COLORONLY){
                                Polygon p = new Polygon();
                                p.addPoint(halfwidth, halfheight);
                                p.addPoint(halfwidth+(int)(sinval*(window.getWidth()-64)/2.0), halfheight-(int)(cosval*(window.getHeight()-96)/2.0));
                                p.addPoint(halfwidth+(int)(Math.sin((i+1)*Math.PI/circamt)*(window.getWidth()-64)/2.0), halfheight-(int)(Math.cos((i+1)*Math.PI/circamt)*(window.getHeight()-96)/2.0));
                                g.fillPolygon(p);
                                //g.drawLine(halfwidth, halfheight, halfwidth+(int)(sinval*(window.getWidth()-64)/2.0), halfheight-(int)(cosval*(window.getHeight()-96)/2.0));
                            }
                            //DISPARITY
                            else if (DISPARITYDRAW){
                                double len = (500d-Math.min(Math.min(Math.abs(i-array[i]), Math.abs(i-array[i]+1000)),Math.abs(i-array[i]-1000)))/500d;
                                
                                if(PIXELDRAW){
                                    int linkedpixX = halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*len)) + dotw/2;
                                    int linkedpixY = halfheight-(int)(cosval*((window.getHeight()-96)/2.0*len)) + doth/2;
                                    
                                    g.fillRect(linkedpixX - dotw/2, linkedpixY - doth/2, dotw, doth);
                                    if(LINKEDPIXELDRAW){
                                        if(i>0)
                                            g.drawLine(linkedpixX, linkedpixY, linkedpixdrawx, linkedpixdrawy);
                                        linkedpixdrawx = linkedpixX;
                                        linkedpixdrawy = linkedpixY;
                                    }
                                }
                                else{
                                    Polygon p = new Polygon();
                                    p.addPoint(halfwidth, halfheight);
                                    p.addPoint(halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*len)), halfheight-(int)(cosval*((window.getHeight()-96)/2.0*len)));
                                    p.addPoint(halfwidth+(int)(Math.sin((i+1)*Math.PI/circamt)*((window.getWidth()-64)/2.0*len)), halfheight-(int)(Math.cos((i+1)*Math.PI/circamt)*((window.getHeight()-96)/2.0*len)));
                                    g.fillPolygon(p);
                                }
                                //g.drawLine(halfwidth, halfheight, halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))));
                            }
                            //PIXELS ONLY
                            else if(PIXELDRAW){
                                g.fillRect(halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))), dotw, doth);
                                if(LINKEDPIXELDRAW){
                                    if(i>0)
                                        g.drawLine(halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))), linkedpixdrawx, linkedpixdrawy);
                                    linkedpixdrawx = halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*(array[i]/(double)array.length)));
                                    linkedpixdrawy = halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)array.length)));
                                }
                            }
                            //LENGTH AND COLOR
                            else{
                                Polygon p = new Polygon();
                                p.addPoint(halfwidth, halfheight);
                                p.addPoint(halfwidth+(int)(Math.sin((i)*Math.PI/circamt)*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfheight-(int)(Math.cos((i)*Math.PI/circamt)*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))));
                                p.addPoint(halfwidth+(int)(Math.sin((i+1)*Math.PI/circamt)*((window.getWidth()-64)/2.0*(array[Math.min(i+1,array.length-1)]/(double)array.length))), halfheight-(int)(Math.cos((i+1)*Math.PI/circamt)*((window.getHeight()-96)/2.0*(array[Math.min(i+1,array.length-1)]/(double)array.length))));
                                g.fillPolygon(p);
                                //g.drawLine(halfwidth, halfheight, halfwidth+(int)(sinval*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfheight-(int)(cosval*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))));
                            }
                        }
                    else
                        for(int i = 0; i < array.length; i++){
                            if(i>0 && array[i]<array[i-1])
                                sortErrors++;
                            
                            if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                                g.setColor(Color.BLACK);
                            else
                                g.setColor(getIntColor(array[i],sortErrors));
                            
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
                                    g.fillRect(amt-dotw/2, y-doth/2, dotw, doth);
                                    if(LINKEDPIXELDRAW){
                                        if(i>0)
                                            g.drawLine(amt, y, linkedpixdrawx, linkedpixdrawy);
                                        linkedpixdrawx = amt;
                                        linkedpixdrawy = y;
                                    }
                                }
                                else{
                                    y = (int)(window.getHeight()-array[i]*yscl);
                                    g.fillRect(amt, y, width, Math.max((int)(array[i]*yscl),1));
                                    g.setColor(getRevColor());
                                    //g.fillRect(amt, y, width, 6); FILAMENT
                                }
                            }
                            amt+=width;
                        }
                    
                    int coltmp = 255;//(int)Math.abs(Math.sin(frames*0.01)*255);
                    g.setColor(new Color(coltmp,coltmp,coltmp));
                    Font f = g.getFont();
                    g.setFont(fon);
                    g.drawString(heading, 10, (int)(cw/1280.0*20)+30);
                    g.drawString(formatNum(comps)+" Comparison"+(comps==1?"":"s"), 10, (int)(cw/1280.0*40)+30);
                    g.drawString(formatNum(aa)+" Array Access"+(aa==1?"":"es"), 10, (int)(cw/1280.0*60)+30);
                    int sortpercent = (int)((double)(array.length-sortErrors)/(double)array.length*100.0);
                    g.drawString(String.format("%d%% Sorted (%d Segment%c)", sortpercent, sortErrors+1, sortErrors==0?' ':'s'), 10, (int)(cw/1280.0*80)+30);
                    //g.drawString(String.format("%d Segment%c", sortErrors+1, sortErrors==0?' ':'s'), 10, (int)(cw/1280.0*100)+30);
                    double slpt = (double)sleeptime/1000000000.0;
                    if(System.currentTimeMillis()-lastrtupdate > rtupdatefreq){
                        realt = (double)(running?(System.nanoTime()-starttime-sleeptime):(stoptime-starttime-sleeptime))/1000000.0;
                        lastrtupdate = System.currentTimeMillis();
                    }
                    g.drawString(String.format("Real Time: %.2fms",realt), 10, (int)(cw/1280.0*100)+30);
                    g.drawString(String.format("Sleep Time: %.2fs",slpt), 10, (int)(cw/1280.0*120)+30);
                    g.setFont(f);
                    Graphics g2 = window.getGraphics();
                    g2.setColor(Color.BLACK);
                    g2.drawImage(img, 0, 0, null);
                }
            }
            
            //0 = Solid, 1 = Rainbow, 2 = Segments
            public Color getIntColor(int i, int segnum) {
                if(COLORSTRAT == 1)
                    return Color.getHSBColor(((float)i/array.length), 1.0F, 0.8F);
                else if(COLORSTRAT == 2){
                    return COLORSTRAT2cols.get(segnum%COLORSTRAT2cols.size());
                }
                return COLORSTRAT0col;
            }
            public Color getRevColor(){
                return getIntColor((int)(Math.sin(frames/66.67)*array.length),0);
            }
        }.start();
        
        uf.setVisible(false);
        v = new ViewPrompt(window);
        while(v.isVisible()) Thread.sleep(1);
        uf.setVisible(true);
 
        //keep on keeping on
        while(window.isActive())Thread.sleep(100);
    }
    
    public static void refresharray() throws Exception {
        clearmarked();
        if(running){
            stoptime = System.nanoTime();
            running = false;
            Thread.sleep(1000);
        }
        
        Thread.sleep(1000);
        boolean solved = true;
        for(int i = 0; i < array.length; i++){
            if(array[i]!=i)
                solved = false;
            marked.set(0,i);
        }
        for(int i = 0; i < array.length; i++)
            array[i] = i;
        //System.out.println(solved);
        marked.set(0, -5);
        heading = "";
        aa = 0;
        comps = 0;
        shuffle(array);
        clearmarked();
        Thread.sleep(500);
        starttime = System.nanoTime();
        sleeptime = 0;
        running = true;
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
        String tmp = heading;
        heading = "Shuffling...";
        for(int i = 0; i < array.length; i++){
            swap(array, i, (int)(Math.random()*array.length));
            aa-=2;
            if(SHUFFLEANIM)
                sleep(1);
        }
        heading = tmp;
    }

    public static int sleepTime(double d) {
        return (int)(array.length*d)/4;
    }
    
    public synchronized static void RunAllSorts(){
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

                    refresharray();
                    heading = "Selection Sort";
                    selectionSort();
                    
                    chan.allNotesOff();
                    refresharray();
                    heading = "Bubble Sort";
                    bubbleSort();

                    chan.allNotesOff();
                    refresharray();
                    heading = "Insertion Sort";
                    insertionSort();

                    chan.allNotesOff();
                    refresharray();
                    heading = "Cocktail Shaker Sort";
                    cocktailShakerSort();

                    chan.allNotesOff();
                    refresharray();
                    heading = "Double Selection Sort";
                    doubleSelectionSort(array);
                    
                    chan.allNotesOff();
                    refresharray();
                    heading = "Shell Sort";
                    shellSort(array.length, 2);
                    
                    chan.allNotesOff();
                    refresharray();
                    heading = "Merge Sort";
                    mergeSortOP();
                    
//                    chan.allNotesOff();
//                    refresharray();
//                    heading = "Merge Sort In-Place";
//                    mergeSort(0, array.length - 1);

                    chan.allNotesOff();
                    refresharray();
                    heading = "Merge+Insertion Sort";
                    weaveMergeSort(0, array.length-1);

                    chan.allNotesOff();
                    refresharray();
                    heading = "Max Heap Sort";
                    maxheapsort();

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
                    heading = "Time+Insertion Sort (Mul 4)";
                    timeSort(4);
                    
                    chan.allNotesOff();
                    refresharray();
                    heading = "Gravity Sort";
                    gravitySort();

                    chan.allNotesOff();
                    refresharray();
                    heading = "Radix LSD Sort (Base 4)";
                    radixLSDsort(4);

                    chan.allNotesOff();
                    refresharray();
                    heading = "Radix MSD Sort (Base 4)";
                    radixMSDSort(4);
                    
                    chan.allNotesOff();
                    refresharray();
                    heading = "Radix LSD In-Place Sort (Base 2)";
                    inPlaceRadixLSDSort(2);
                    
                    chan.allNotesOff();
                    refresharray();
                    heading = "Radix LSD In-Place Sort (Base 10)";
                    inPlaceRadixLSDSort(10);

                    chan.allNotesOff();
                    refresharray();
                    heading = "Binary Quick Sort";
                    bquickSort();

                }catch (Exception e){}
                SetSound(false);
                stoptime = System.nanoTime();
                running = false;
                chan.allNotesOff();
                clearmarked();
            }
        };
        sortingThread.start();
    }
    
    public static void ReportComparativeSort(int n){
        if(sortingThread != null && sortingThread.isAlive())
            return;
        
        final int num = n;
        SetSound(true);
        sortingThread = new Thread(){
            @Override
            public void run(){
                try{
                    
                    refresharray();
                    heading = ComparativeSorts[num]+" Sort";
                switch (num){
                    case 0:
                        selectionSort();break;
                    case 1:
                        bubbleSort();break;
                    case 2:
                        insertionSort();break;
                    case 3:
                        doubleSelectionSort(array);break;
                    case 4:
                        cocktailShakerSort();break;
                    case 5:
                        quickSort(array, 0, array.length-1);break;
                    case 6:
                        mergeSort(0, array.length-1);break;
                    case 7:
                        mergeSortOP();break;
                    case 8:
                        weaveMergeSort(0, array.length-1);break;
                    case 9:
                        maxheapsort();break;
                    case 10:
                        shellSort(array.length, 2);break;
                }
                }catch(Exception e){e.printStackTrace();}
                SetSound(false);
                stoptime = System.nanoTime();
                running = false;
            }
        };
        sortingThread.start();
    }
    
    public static void ReportDistributiveSort(int n){
        if(sortingThread != null && sortingThread.isAlive())
            return;
        int bas = 10;
        if(n != 3 && n != 5 && n != 7)
            if(n != 4)
                try{bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Base for Sort"));}catch(Exception e){}
            else
                try{bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Size of Partitions"));}catch(Exception e){}
        
        final int base = Math.max(bas,2);
        final int num = n;
        SetSound(true);
        sortingThread = new Thread(){
            @Override
            public void run(){
        try{
            refresharray();
            heading = DistributiveSorts[num]+" Sort";
        switch (num){
            case 0:
                radixLSDsort(base);break;
            case 1:
                radixMSDSort(base);break;
            case 2:
                RadixLSDInPlace.inPlaceRadixLSDSort(base);break;
            case 3:
                bquickSort();break;
            case 4:
                gravitySort();break;
            case 5:
                shatterSort(base);break;
            case 6:
                countingSort();break;
            case 7:
                timeSort(base);break;
            case 8:
                bogoSort();break;
        }
        }catch(Exception e){}
        SetSound(false);
        stoptime = System.nanoTime();
        running = false;
            }
        };
        sortingThread.start();
    }
    
    public static String formatNum(long a){
        if(a<0)
            return "OVERFLOW";
        if(a>1000L*1000L*1000L*1000L)
            return String.format("%.2fT",(double)a/1000000000000.0);
        if(a>1000L*1000L*1000L)
            return String.format("%.2fB",(double)a/1000000000.0);
        if(a>1000L*1000L)
            return String.format("%.2fM",(double)a/1000000.0);
        else if(a>1000L)
            return String.format("%.2fK",(double)a/1000.0);
        else
            return ""+a;
    }
}
