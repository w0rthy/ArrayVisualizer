package array.visualizer;

import array.visualizer.sort.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.util.ArrayList;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;

import static array.visualizer.utils.Swaps.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.swing.JOptionPane;

public class ArrayVisualizer
{
    private static final JFrame window = new JFrame();

    private static ArrayController arrayController = new ArrayController(1000);
    private static String heading = "";
    private static int frames;
    private static int snd = 0;
    private static Font fon = new Font("TimesRoman", Font.PLAIN, (int) (640 / 1280.0 * 25));

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
    private static UtilFrame uf;
    private static ViewPrompt v;
    private static Synthesizer synth;
    private static MidiChannel chan;
    private static Thread sortingThread;
    static boolean SHUFFLEANIM = true;

    private static long startTime = 0;
    private static long stopTime = 0;
    private static boolean running = false;
    private static long sleepTime = 0;

    private static long rtUpdateFreq = 100; //How frequently to update the real time display in ms
    private static double realt = 0d;
    private static long lastRtUpdate = 0;

    static int COLORSTRAT = 1; //0 = Solid, 1 = Rainbow, 2 = Segments
    static Color COLORSTRAT0col = new Color(0, 204, 0);
    static ArrayList<Color> COLORSTRAT2cols = new ArrayList<>();

    static String[] comparativeSorts = "Selection!Bubble!Insertion!Double Selection!Cocktail Shaker!Quick!Merge!Merge OOP!Weave Merge!Max Heap!Shell".split("!");
    static String[] distributiveSorts = "Radix LSD!Radix MSD!Radix LSD In-Place!Gravity!Shatter!Counting!Time!Bogo".split("!");

    private static int cx = 0;
    private static int cy = 0;

    private static double addamt = 0.0;

    public static double calcVel()
    {
        double count = 1;
        for (int i : arrayController.marked)
        {
            if (i != -5)
            {
                count += 0.75;
            }
        }
        return count;
    }

    public static synchronized void setSound(boolean val)
    {
        SOUND = val;
    }

    public static void sleep(double milis)
    {
        if (milis <= 0)
        {
            return;
        }
        double tmp = (milis * (1000.0 / arrayController.length));
        tmp = tmp * (1 / SLEEPRATIO);
        addamt += tmp;
        if (addamt < 1.0)
        {
            return;
        }
        try
        {
            long actual = System.nanoTime();
            Thread.sleep((long) addamt);
            actual = (System.nanoTime() - actual);
            addamt -= (double) actual / 1000000.0;
            if (running)
            {
                sleepTime += actual;
            }
        } catch (InterruptedException e)
        {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) throws Exception
    {

        //Segment Colors
        COLORSTRAT2cols.add(new Color(0, 204, 0));
        COLORSTRAT2cols.add(new Color(204, 0, 0));
        //COLORSTRAT2cols.add(new Color(204, 204, 0));

        synth = MidiSystem.getSynthesizer();
        synth.open();
        synth.loadAllInstruments(synth.getDefaultSoundbank());
//        int s = 0;
//        for(Instrument i : synth.getAvailableInstruments()){
//            System.out.println(s+" "+i.getName());
//            s++;
//        }
        chan = synth.getChannels()[0];
        for (Instrument i : synth.getLoadedInstruments())
        {
            if (i.getName().toLowerCase().trim().contains("sine"))
            {
                chan.programChange(i.getPatch().getProgram());
                break;
            }
        }

        if (chan.getProgram() == 0)
        {
            JOptionPane.showMessageDialog(null, "Could not find a valid instrument. Sound is disabled");
        }
        //chan.programChange(synth.getLoadedInstruments()[197].getPatch().getProgram());

        for (int i = 0; i < arrayController.length; i++)
        {
            arrayController.marked.add(-5);
        }
        rianr(arrayController.array);

        window.setSize(640, 480);
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Array Visualizer");

        uf = new UtilFrame(window);

        //AUDIO THREAD
        new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    chan.allNotesOff();
                    if (!SOUND)
                    {
                        continue;
                    }

                    int tmp = 1;
                    for (int i : arrayController.marked)
                    {
                        if (i != -5)
                        {
                            tmp++;
                        }
                    }
                    for (int i : arrayController.marked)
                    {
                        if (i != -5)
                        {
                            int pitch = (int) Math.round((double) arrayController.array[Math.min(Math.max(i, 0), arrayController.length - 1)] / arrayController.length * 96 + 16);
                            //int vel = (int)(((128-pitch)/320.0+0.4)   *   (128.0/Math.pow(tmp, 0.33)));
                            //int vel = (int)(64.0/Math.pow(tmp, 0.25));
                            //int vel = (int)((64.0-Math.pow((tmp-1)*10, 0.67d))*SOUNDMUL);
                            int vel = (int) (64.0 / (Math.pow((double) tmp, 0.6)) * ((128 - pitch) / 64.0 + 1.0) * 0.67 * SOUNDMUL); //one day
                            chan.noteOn(pitch, vel);
                        }
                    }
                    //((int)((127-(array[Math.min(Math.max(i, 0), array.length-1)]/16.0))/Math.sqrt(calcVel())))*5
                    //double tmp = (array[Math.min(Math.max(i, 0), array.length-1)]/32.0+47);
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
                        chan.noteOn(array[Math.min(Math.max(i, 0), array.length-1)]/32+47, 127);
                        double tmpd = (array[Math.min(Math.max(i, 0), array.length-1)]/32.0+47);
                        chan.setPitchBend(8192*2-(int)((tmpd-Math.floor(tmpd))*8192*2));
                        }while(false);}*/
                    try
                    {
                        sleep(1);
                    } catch (InterruptedException e)
                    {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //DRAW THREAD
        new Thread()
        {
            @Override
            public void run()
            {
                int cw = 640;
                int ch = 480;
                Image img = window.createVolatileImage(cw, ch);
                //Graphics g = window.getGraphics();
                Graphics2D g = (Graphics2D) img.getGraphics();

                double xscl, yscl;
                while (true)
                {

                    if (window.getWidth() != cw || window.getHeight() != ch || window.getX() != cx || window.getY() != cy)
                    {
                        uf.reposition();
                        if (v != null && v.isVisible())
                        {
                            v.reposition();
                        }
                        cx = window.getX();
                        cy = window.getY();
                    }

                    if (window.getWidth() != cw || window.getHeight() != ch)
                    {
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        fon = new Font("TimesRoman", Font.PLAIN, (int) (cw / 1280.0 * 25));
                        g = (Graphics2D) img.getGraphics();

                    }
                    int gamt = 32;//(int)(frames/1000.0%64);
                    g.setColor(new Color(gamt, gamt, gamt));
                    g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
                    xscl = (double) window.getWidth() / arrayController.length;
                    yscl = (double) (window.getHeight() - 30) / arrayController.length;
                    int amt = 0;
                    int circamt = arrayController.length / 2;
                    int linkedPixDrawX = 0;
                    int linkedPixDrawY= 0;
                    frames++;

                    int halfWidth = window.getWidth() / 2;
                    int halfHeight = window.getHeight() / 2;
                    int dotw = (int) (2 * (window.getWidth() / 640.0));
                    int doth = (int) (2 * (window.getHeight() / 480.0));

                    int sortErrors = 0;

                    float strokew = 3f * (window.getWidth() / 1920f);
                    g.setStroke(new BasicStroke(strokew));

                    //CHRISTMAS TREE?
                    if (XMASDRAW)
                    {

                        double trunkSize = (double) ch * 0.1;
                        int trunkStart = arrayController.length - (int) (trunkSize / yscl);

                        double ornamentSize = (double) ch * 0.1;
                        int ornamentEnd = (int) (ornamentSize / yscl);

                        int width = 0;

                        for (int i = 0; i < arrayController.length; i++)
                        {
                            //Check for sort errors in array
                            if (i > 0 && arrayController.array[i] < arrayController.array[i - 1])
                            {
                                sortErrors++;
                            }

                            int j = arrayController.array[i];

                            if (arrayController.marked.contains(i) || arrayController.marked.contains(i - 1) || arrayController.marked.contains(i - 2) || arrayController.marked.contains(i - 3))
                            {
                                g.setColor(Color.BLACK);
                            } else if (j < ornamentEnd)
                            {
                                g.setColor(new Color(255, 0, 0));
                            } else if (j > trunkStart)
                            {
                                g.setColor(new Color(102, 34, 0));
                            } else
                            {
                                g.setColor(new Color(0, 102, 0));
                            }

                            if (j < ornamentEnd)
                            { //ORNAMENT
                                width = (int) (Math.sin(Math.acos((j * yscl) / ornamentSize * 2.0 - 1.0)) * ornamentSize);
                            } else if (j > trunkStart)
                            { //TRUNK
                                width = (int) (cw * 0.2);
                            } else //TREE
                            {
                                width = (int) ((((j - ornamentEnd) % 85) * 0.005 + 0.075) * cw);
                            }

                            int step = (int) (i * yscl + yscl) - (int) (i * yscl);
                            g.fillRect(halfWidth - width / 2, (int) (i * yscl) + 32, width, step);

                        }
                    }
                    //HOOP DRAWING (PYRAMID CIRCLES)
                    else if (PYRAMIDDRAW && CIRCLEDRAW)
                    {

                        g.setStroke(new BasicStroke(1.0f)); //significantly increased performance

                        double diameter = (double) Math.min(cw, ch - 32);
                        double diamStep = Math.min(xscl, yscl);

                        for (int i = arrayController.length - 1; i >= 0; i--)
                        {
                            //Check for sort errors in array
                            if (i > 0 && arrayController.array[i] < arrayController.array[i - 1])
                            {
                                sortErrors++;
                            }

                            //Set Proper Color
                            if (arrayController.marked.contains(i) || arrayController.marked.contains(i - 1) || arrayController.marked.contains(i - 2) || arrayController.marked.contains(i - 3))
                            {
                                g.setColor(Color.BLACK);
                            } else
                            {
                                g.setColor(getIntColor(arrayController.array[i], sortErrors));
                            }

                            int radius = (int) (diameter / 2.0);

                            if (!DISPARITYDRAW)
                            {
                                g.drawOval(halfWidth - radius, halfHeight - radius + 15, (int) diameter, (int) diameter);
                            } else
                            {
                                int dist = Math.abs(i - arrayController.array[i]);
                                double disparity = 1.0 - ((double) Math.min(dist, arrayController.length - dist) / ((double) arrayController.length / 2.0));
                                int actualDiam = (int) (disparity * diameter);
                                g.drawOval(halfWidth - actualDiam / 2, halfHeight - radius + 15, actualDiam, actualDiam);
                            }
                            diameter -= diamStep;
                        }
                    }
                    //PYRAMID DRAW METHOD
                    else if (PYRAMIDDRAW)
                    {

                        //Setup
                        double curPos = 0.0;

                        //Draw loop
                        for (int i = 0; i < arrayController.length; i++)
                        {
                            //Check for sort errors in array
                            if (i > 0 && arrayController.array[i] < arrayController.array[i - 1])
                            {
                                sortErrors++;
                            }

                            //Set Proper Color
                            if (arrayController.marked.contains(i) || arrayController.marked.contains(i - 1) || arrayController.marked.contains(i - 2) || arrayController.marked.contains(i - 3))
                            {
                                g.setColor(Color.BLACK);
                            } else
                            {
                                g.setColor(getIntColor(arrayController.array[i], sortErrors));
                            }

                            //Draw
                            if (!DRAWFLIPPED)
                            {
                                int len = (int) ((double) arrayController.array[i] / (double) arrayController.length * (double) cw);
                                int step = (int) (curPos + yscl) - (int) curPos;
                                g.fillRect(halfWidth - len / 2, (int) curPos + 32, len, step);
                                curPos += yscl;
                            } else
                            {
                                int len = (int) ((double) arrayController.array[i] / (double) arrayController.length * ((double) ch - 32.0));
                                int step = (int) (curPos + xscl) - (int) curPos;
                                g.fillRect((int) curPos, halfHeight - len / 2 + 16, step, len);
                                curPos += xscl;
                            }
                        }
                    }
                    //MESH DRAW METHOD
                    else if (MESHDRAW)
                    {

                        int trih = window.getHeight() / 20; //Height of triangles to use, Width will be scaled accordingly

                        int triPerCol = window.getHeight() / trih * 2; //Triangles per vertical column
                        int triPerRow = arrayController.length / triPerCol; //Triangles per horizontal row
                        double triw = (double) window.getWidth() / triPerRow; //Width of triangles to use

                        double curx = 0;
                        int cury = 0;

                        int[] triptsx = new int[3];
                        int[] triptsy = new int[3];

                        for (int i = 0; i < arrayController.length; i++)
                        {
                            if (i > 0 && arrayController.array[i] < arrayController.array[i - 1])
                            {
                                sortErrors++;
                            }

                            if (arrayController.marked.contains(i)/*||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3)*/)
                            {
                                g.setColor(Color.BLACK);
                            } else
                            {
                                g.setColor(getIntColor(arrayController.array[i], sortErrors));
                            }

                            //If i/triPerRow is even, then triangle points right, else left
                            boolean direction = false;
                            if (((i - 1) / triPerRow) % 2 == 0)
                            {
                                direction = true;
                            }

                            //Make the triangle
                            if (!direction)
                            {
                                //Pointing right
                                triptsx[0] = (int) curx;
                                triptsx[1] = (int) curx;
                                curx += triw;
                                triptsx[2] = (int) curx;

                                if (!COLORONLY)
                                {
                                    triptsx[2] = (int) (triptsx[1] + triw * arrayController.array[i] / arrayController.length);
                                }

                                triptsy[0] = cury;
                                triptsy[2] = cury + trih / 2;
                                triptsy[1] = cury + trih;
                            } else
                            {
                                //Pointing left
                                triptsx[2] = (int) curx;
                                curx += triw;
                                triptsx[0] = (int) curx;
                                triptsx[1] = (int) curx;

                                if (!COLORONLY)
                                {
                                    triptsx[2] = (int) (triptsx[0] - triw * arrayController.array[i] / arrayController.length);
                                }

                                triptsy[0] = cury;
                                triptsy[2] = cury + trih / 2;
                                triptsy[1] = cury + trih;
                            }

                            //Draw it
                            g.fillPolygon(triptsx, triptsy, triptsx.length);

                            //If at the end of a row, reset curx
                            if (i != 0 && i % triPerRow == 0)
                            {
                                curx = 0d;
                                cury += trih / 2;
                            }
                        }
                    } else if (CIRCLEDRAW)
                    {
                        for (int i = 0; i < arrayController.length; i++)
                        {
                            if (i > 0 && arrayController.array[i] < arrayController.array[i - 1])
                            {
                                sortErrors++;
                            }

                            if (arrayController.marked.contains(i) || arrayController.marked.contains(i - 1) || arrayController.marked.contains(i - 2) || arrayController.marked.contains(i - 3))
                            {
                                g.setColor(Color.BLACK);
                            } else
                            {
                                g.setColor(getIntColor(arrayController.array[i], sortErrors));
                            }

                            double sinVal = Math.sin(i * Math.PI / circamt);
                            double cosVal = Math.cos(i * Math.PI / circamt);

                            //COLOR ONLY NO LENGTH
                            if (COLORONLY)
                            {
                                Polygon p = new Polygon();
                                p.addPoint(halfWidth, halfHeight);
                                p.addPoint(halfWidth + (int) (sinVal * (window.getWidth() - 64) / 2.0), halfHeight - (int) (cosVal * (window.getHeight() - 96) / 2.0));
                                p.addPoint(halfWidth + (int) (Math.sin((i + 1) * Math.PI / circamt) * (window.getWidth() - 64) / 2.0), halfHeight - (int) (Math.cos((i + 1) * Math.PI / circamt) * (window.getHeight() - 96) / 2.0));
                                g.fillPolygon(p);
                                //g.drawLine(halfWidth, halfHeight, halfWidth+(int)(sinVal*(window.getWidth()-64)/2.0), halfHeight-(int)(cosVal*(window.getHeight()-96)/2.0));
                            }
                            //DISPARITY
                            else if (DISPARITYDRAW)
                            {
                                double len = (500d - Math.min(Math.min(Math.abs(i - arrayController.array[i]), Math.abs(i - arrayController.array[i] + 1000)), Math.abs(i - arrayController.array[i] - 1000))) / 500d;

                                if (PIXELDRAW)
                                {
                                    int linkedPixX = halfWidth + (int) (sinVal * ((window.getWidth() - 64) / 2.0 * len)) + dotw / 2;
                                    int linkedPixY = halfHeight - (int) (cosVal * ((window.getHeight() - 96) / 2.0 * len)) + doth / 2;

                                    g.fillRect(linkedPixX - dotw / 2, linkedPixY - doth / 2, dotw, doth);
                                    if (LINKEDPIXELDRAW)
                                    {
                                        if (i > 0)
                                        {
                                            g.drawLine(linkedPixX, linkedPixY, linkedPixDrawX, linkedPixDrawY);
                                        }
                                        linkedPixDrawX = linkedPixX;
                                        linkedPixDrawY = linkedPixY;
                                    }
                                } else
                                {
                                    Polygon p = new Polygon();
                                    p.addPoint(halfWidth, halfHeight);
                                    p.addPoint(halfWidth + (int) (sinVal * ((window.getWidth() - 64) / 2.0 * len)), halfHeight - (int) (cosVal * ((window.getHeight() - 96) / 2.0 * len)));
                                    p.addPoint(halfWidth + (int) (Math.sin((i + 1) * Math.PI / circamt) * ((window.getWidth() - 64) / 2.0 * len)), halfHeight - (int) (Math.cos((i + 1) * Math.PI / circamt) * ((window.getHeight() - 96) / 2.0 * len)));
                                    g.fillPolygon(p);
                                }
                                //g.drawLine(halfWidth, halfHeight, halfWidth+(int)(sinVal*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfHeight-(int)(cosVal*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))));
                            }
                            //PIXELS ONLY
                            else if (PIXELDRAW)
                            {
                                g.fillRect(halfWidth + (int) (sinVal * ((window.getWidth() - 64) / 2.0 * (arrayController.array[i] / (double) arrayController.length))), halfHeight - (int) (cosVal * ((window.getHeight() - 96) / 2.0 * (arrayController.array[i] / (double) arrayController.length))), dotw, doth);
                                if (LINKEDPIXELDRAW)
                                {
                                    if (i > 0)
                                    {
                                        g.drawLine(halfWidth + (int) (sinVal * ((window.getWidth() - 64) / 2.0 * (arrayController.array[i] / (double) arrayController.length))), halfHeight - (int) (cosVal * ((window.getHeight() - 96) / 2.0 * (arrayController.array[i] / (double) arrayController.length))), linkedPixDrawX, linkedPixDrawY);
                                    }
                                    linkedPixDrawX = halfWidth + (int) (sinVal * ((window.getWidth() - 64) / 2.0 * (arrayController.array[i] / (double) arrayController.length)));
                                    linkedPixDrawY = halfHeight - (int) (cosVal * ((window.getHeight() - 96) / 2.0 * (arrayController.array[i] / (double) arrayController.length)));
                                }
                            }
                            //LENGTH AND COLOR
                            else
                            {
                                Polygon p = new Polygon();
                                p.addPoint(halfWidth, halfHeight);
                                p.addPoint(halfWidth + (int) (Math.sin((i) * Math.PI / circamt) * ((window.getWidth() - 64) / 2.0 * (arrayController.array[i] / (double) arrayController.length))), halfHeight - (int) (Math.cos((i) * Math.PI / circamt) * ((window.getHeight() - 96) / 2.0 * (arrayController.array[i] / (double) arrayController.length))));
                                p.addPoint(halfWidth + (int) (Math.sin((i + 1) * Math.PI / circamt) * ((window.getWidth() - 64) / 2.0 * (arrayController.array[Math.min(i + 1, arrayController.length - 1)] / (double) arrayController.length))), halfHeight - (int) (Math.cos((i + 1) * Math.PI / circamt) * ((window.getHeight() - 96) / 2.0 * (arrayController.array[Math.min(i + 1, arrayController.length - 1)] / (double) arrayController.length))));
                                g.fillPolygon(p);
                                //g.drawLine(halfWidth, halfHeight, halfWidth+(int)(sinVal*((window.getWidth()-64)/2.0*(array[i]/(double)array.length))), halfHeight-(int)(cosVal*((window.getHeight()-96)/2.0*(array[i]/(double)array.length))));
                            }
                        }
                    } else
                    {
                        for (int i = 0; i < arrayController.length; i++)
                        {
                            if (i > 0 && arrayController.array[i] < arrayController.array[i - 1])
                            {
                                sortErrors++;
                            }

                            if (arrayController.marked.contains(i) || arrayController.marked.contains(i - 1) || arrayController.marked.contains(i - 2) || arrayController.marked.contains(i - 3))
                            {
                                g.setColor(Color.BLACK);
                            } else
                            {
                                g.setColor(getIntColor(arrayController.array[i], sortErrors));
                            }

                            int y = 0;
                            int width = (int) (xscl * i) - amt;

                            if (width > 0)
                            {
                                if (COLORONLY)
                                {
                                    y = (int) (window.getHeight() - 750 * yscl);
                                    g.fillRect(amt, y, width, Math.max((int) (750 * yscl), 1));
                                    g.setColor(getRevColor());
                                    g.fillRect((int) (i * xscl), y, width, 6);
                                } else if (PIXELDRAW)
                                {
                                    y = (int) (window.getHeight() - arrayController.array[i] * yscl);
                                    g.fillRect(amt - dotw / 2, y - doth / 2, dotw, doth);
                                    if (LINKEDPIXELDRAW)
                                    {
                                        if (i > 0)
                                        {
                                            g.drawLine(amt, y, linkedPixDrawX, linkedPixDrawY);
                                        }
                                        linkedPixDrawX = amt;
                                        linkedPixDrawY = y;
                                    }
                                } else
                                {
                                    y = (int) (window.getHeight() - arrayController.array[i] * yscl);
                                    g.fillRect(amt, y, width, Math.max((int) (arrayController.array[i] * yscl), 1));
                                    g.setColor(getRevColor());
                                    //g.fillRect(amt, y, width, 6); FILAMENT
                                }
                            }
                            amt += width;
                        }
                    }

                    int coltmp = 255;//(int)Math.abs(Math.sin(frames*0.01)*255);
                    g.setColor(new Color(coltmp, coltmp, coltmp));
                    Font f = g.getFont();
                    g.setFont(fon);
                    g.drawString(heading, 10, (int) (cw / 1280.0 * 20) + 30);
                    g.drawString(formatNum(arrayController.comps) + " Comparison" + (arrayController.comps == 1 ? "" : "s"), 10, (int) (cw / 1280.0 * 40) + 30);
                    g.drawString(formatNum(arrayController.aa) + " Array Access" + (arrayController.aa == 1 ? "" : "es"), 10, (int) (cw / 1280.0 * 60) + 30);
                    int sortpercent = (int) ((double) (arrayController.length - sortErrors) / (double) arrayController.length * 100.0);
                    g.drawString(String.format("%d%% Sorted (%d Segment%c)", sortpercent, sortErrors + 1, sortErrors == 0 ? ' ' : 's'), 10, (int) (cw / 1280.0 * 80) + 30);
                    //g.drawString(String.format("%d Segment%c", sortErrors+1, sortErrors==0?' ':'s'), 10, (int)(cw/1280.0*100)+30);
                    double slpt = (double) sleepTime / 1000000000.0;
                    if (System.currentTimeMillis() - lastRtUpdate > rtUpdateFreq)
                    {
                        realt = (double) (running ? (System.nanoTime() - startTime - sleepTime) : (stopTime - startTime - sleepTime)) / 1000000.0;
                        lastRtUpdate = System.currentTimeMillis();
                    }
                    g.drawString(String.format("Real Time: %.2fms", realt), 10, (int) (cw / 1280.0 * 100) + 30);
                    g.drawString(String.format("Sleep Time: %.2fs", slpt), 10, (int) (cw / 1280.0 * 120) + 30);
                    g.setFont(f);
                    Graphics g2 = window.getGraphics();
                    g2.setColor(Color.BLACK);
                    g2.drawImage(img, 0, 0, null);
                }
            }

            //0 = Solid, 1 = Rainbow, 2 = Segments
            public Color getIntColor(int i, int segNum)
            {
                if (COLORSTRAT == 1)
                {
                    return Color.getHSBColor(((float) i / arrayController.length), 1.0F, 0.8F);
                } else if (COLORSTRAT == 2)
                {
                    return COLORSTRAT2cols.get(segNum % COLORSTRAT2cols.size());
                }
                return COLORSTRAT0col;
            }

            public Color getRevColor()
            {
                return getIntColor((int) (Math.sin(frames / 66.67) * arrayController.length), 0);
            }
        }.start();

        uf.setVisible(false);
        v = new ViewPrompt(window);
        while (v.isVisible())
        {
            Thread.sleep(1);
        }
        uf.setVisible(true);

        //keep on keeping on
        while (window.isActive())
        {
            Thread.sleep(100);
        }
    }

    public static void refreshArray() throws InterruptedException
    {
        clearMarked();
        if (running)
        {
            stopTime = System.nanoTime();
            running = false;
            Thread.sleep(1000);
        }

        Thread.sleep(1000);
        boolean solved = true;
        for (int i = 0; i < arrayController.length; i++)
        {
            if (arrayController.array[i] != i)
            {
                solved = false;
            }
            arrayController.marked.set(0, i);
        }
        for (int i = 0; i < arrayController.length; i++)
        {
            arrayController.array[i] = i;
        }
        //System.out.println(solved);
        arrayController.marked.set(0, -5);
        heading = "";
        arrayController.aa = 0;
        arrayController.comps = 0;
        shuffle(arrayController.array);
        clearMarked();
        Thread.sleep(500);
        startTime = System.nanoTime();
        sleepTime = 0;
        running = true;
    }

    public static int getDigit(int a, int power, int radix)
    {
        return (int) (a / Math.pow(radix, power)) % radix;
    }

    public static void rianr(int[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = i;
        }
        shuffle(arr);
    }

    public static void clearMarked()
    {
        arrayController.clearMarked();
    }

    public static void shuffle(int[] array)
    {
        String tmp = heading;
        heading = "Shuffling...";
        for (int i = 0; i < array.length; i++)
        {
            swap(arrayController, i, (int) (Math.random() * array.length));
            arrayController.aa -= 2;
            if (SHUFFLEANIM)
            {
                sleep(1);
            }
        }
        heading = tmp;
    }

    public static int sleepTime(double d)
    {
        return (int) (arrayController.length * d) / 4;
    }

    public synchronized static void runAllSorts()
    {
        if (sortingThread != null)
        {
            while (sortingThread.isAlive())
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        setSound(true);
        sortingThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    for (Sort sort : new Sort[]{
                            new SelectionSort(),
                            new BubbleSort(),
                            new InsertionSort(),
                            new CocktailShaker(),
                            new ShellSort(),
                            new MergeSortOOP(),
                            new MergeSort(),
                            new WeaveMerge(),
                            new MaxHeapSort(),
                            new QuickSort(),
                            new CountingSort(),
                            new TimeSort(4),
                            new GravitySort(),
                            new RadixLSD(4),
                            new RadixMSD(4),
                            new RadixLSDInPlace(2),
                            new RadixLSDInPlace(10)}
                    )
                    {
                        chan.allNotesOff();
                        refreshArray();
                        heading = sort.name();
                        sort.sort(arrayController);
                    }
                } catch (InterruptedException e)
                {
                    Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
                }
                setSound(false);
                stopTime = System.nanoTime();
                running = false;
                chan.allNotesOff();
                clearMarked();
            }
        };
        sortingThread.start();
    }

    public static void reportComparativeSort(int n)
    {
        if (sortingThread != null && sortingThread.isAlive())
        {
            return;
        }

        final int num = n;
        setSound(true);
        sortingThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    refreshArray();
                    Sort sort;
                    switch (num)
                    {
                        case 0:
                            sort = new SelectionSort();
                            break;
                        case 1:
                            sort = new BubbleSort();
                            break;
                        case 2:
                            sort = new InsertionSort();
                            break;
                        case 3:
                            sort = new DoubleSelection();
                            break;
                        case 4:
                            sort = new CocktailShaker();
                            break;
                        case 5:
                            sort = new QuickSort();
                            break;
                        case 6:
                            sort = new MergeSort();
                            break;
                        case 7:
                            sort = new MergeSortOOP();
                            break;
                        case 8:
                            sort = new WeaveMerge();
                            break;
                        case 9:
                            sort = new MaxHeapSort();
                            break;
                        case 10:
                            sort = new ShellSort();
                            break;
                        default:
                            sort = null;
                            break;
                    }
                    if (sort != null)
                    {
                        heading = sort.name();
                        sort.sort(arrayController);
                    }
                } catch (InterruptedException e)
                {
                    Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
                }
                setSound(false);
                stopTime = System.nanoTime();
                running = false;
            }
        };
        sortingThread.start();
    }

    public static void reportDistributiveSort(int n)
    {
        if (sortingThread != null && sortingThread.isAlive())
        {
            return;
        }
        int bas = 10;
        if (n != 3 && n != 5 && n != 7)
        {
            if (n != 4)
            {
                try
                {
                    bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Base for Sort"));
                } catch (NumberFormatException e)
                {
                    Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
                }
            } else
            {
                try
                {
                    bas = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Size of Partitions"));
                } catch (NumberFormatException e)
                {
                    Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        final int base = Math.max(bas, 2);
        final int num = n;
        setSound(true);
        sortingThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    refreshArray();
                    Sort sort;
                    switch (num)
                    {
                        case 0:
                            sort = new RadixLSD(base);
                            break;
                        case 1:
                            sort = new RadixMSD(base);
                            break;
                        case 2:
                            sort = new RadixLSDInPlace(base);
                            break;
                        case 3:
                            sort = new GravitySort();
                            break;
                        case 4:
                            sort = new ShatterSorts(base);
                            break;
                        case 5:
                            sort = new CountingSort();
                            break;
                        case 6:
                            sort = new TimeSort(base);
                            break;
                        case 7:
                            sort = new BogoSort();
                            break;
                        default:
                            sort = null;
                            break;
                    }
                    if (sort != null)
                    {
                        heading = sort.name();
                        sort.sort(arrayController);
                    }
                } catch (InterruptedException e)
                {
                    Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, e);
                }
                setSound(false);
                stopTime = System.nanoTime();
                running = false;
            }
        };
        sortingThread.start();
    }

    public static String formatNum(long a)
    {
        if (a < 0)
        {
            return "OVERFLOW";
        }
        if (a > 1000L * 1000L * 1000L * 1000L)
        {
            return String.format("%.2fT", (double) a / 1000000000000.0);
        }
        if (a > 1000L * 1000L * 1000L)
        {
            return String.format("%.2fB", (double) a / 1000000000.0);
        }
        if (a > 1000L * 1000L)
        {
            return String.format("%.2fM", (double) a / 1000000.0);
        } else if (a > 1000L)
        {
            return String.format("%.2fK", (double) a / 1000.0);
        } else
        {
            return "" + a;
        }
    }
}
