package array.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
public class ArrayVisualizer {

    static final int [] array = new int[1000];
    static final JFrame window = new JFrame();
    
    static String heading = "";
    static final ArrayList<Integer> marked = new ArrayList();
    static int frames;
    static int aa = 0;
    static int comps = 0;
    static long nanos;
    
    public static double calcVel(){
        double count = 1;
        for(int i : marked)
            if(i!=-1)
                count+=0.5;
        return count;
    }
    
    public static void main(String[] args) throws Exception {

        final Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        synth.loadAllInstruments(synth.getDefaultSoundbank());
        final MidiChannel chan = synth.getChannels()[0];
        chan.programChange(synth.getLoadedInstruments()[14].getPatch().getProgram());
        
        for(int i = 0; i < array.length; i++)
            marked.add(-1);
        rianr(array);
        window.setSize(new Dimension(640,480));
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Array Visualizer");
        
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
                    chan.allNotesOff();
                    for(int i : marked)
                        if(i != -1){
                            chan.noteOn(array[i]/32+47, (int)((127-(array[i]/16.0))/Math.sqrt(calcVel())));
                            chan.setPitchBend((int)((array[i]%32/32.0)*4096)+8192);
                        }
                    
                    if(window.getWidth()!=cw|| window.getHeight()!=ch){
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        g = img.getGraphics();
                    }
                    g.setColor(Color.GRAY);
                    g.fillRect(0,0,1920,1080);
                    xscl = (double)window.getWidth()/array.length;
                    yscl = (double)(window.getHeight()-30)/array.length;
                    frames++;
                    for(int i = 0; i < array.length; i++){
                        if(marked.contains(i)||marked.contains(i-1)||marked.contains(i-2)||marked.contains(i-3))
                            g.setColor(Color.BLACK);
                        else
                            g.setColor(getIntColor(array[i]));
                        g.fillRect((int)(i*xscl), (int)(window.getHeight()-array[i]*yscl), Math.max((int)xscl,1), Math.max((int)(array[i]*yscl),1));
                        g.setColor(Color.WHITE);
                        g.fillRect((int)(i*xscl), (int)(window.getHeight()-array[i]*yscl), Math.max((int)xscl,1), 6);
                    }
                    
                    g.setColor(new Color((int)Math.abs(Math.sin(frames*0.01)*255),0,0));
                    g.drawString(heading, 10, 40);
                    g.drawString("Comparisons: "+comps+" Array Accesses: "+aa, 10, 60);
                    Graphics g2 = window.getGraphics();
                    g2.setColor(Color.BLACK);
                    g2.drawImage(img, 0, 0, null);
                }
            }
            
            private Color getIntColor(int i) {
                return Color.getHSBColor(((float)i/array.length), 1.0F, 1.0F);
            }
            private Color getRevColor(){
                return getIntColor((int)(Math.sin(frames/66.67)*array.length));
            }
        }.start();

        while(true){
            heading = "Shatter-Time Sort";

            timeSort();

            chan.allNotesOff();
            refresharray();

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

            cocktailShakerSort(array);

            chan.allNotesOff();
            refresharray();
            heading = "Merge Sort";

            mergeSort(array, 0, array.length - 1);

            chan.allNotesOff();
            refresharray();
            heading = "Quick Sort";

            quickSort(array, 0, array.length-1);

            chan.allNotesOff();
            refresharray();
            heading = "Radix LSD Sort";

            radixLSDsort(array,4);

            chan.allNotesOff();
            refresharray();
            heading = "Radix LSD In-Place Sort";

            inPlaceRadixLSDSort(10);

            chan.allNotesOff();
            refresharray();
            heading = "Shatter Partition";

            shatterPartition(1);

            chan.allNotesOff();
            refresharray();
            heading = "Shatter Sort";

            shatterSort(128);

            chan.allNotesOff();
            refresharray();
            heading = "Simple Shatter Sort";

            simpleShatterSort(128, 4);

            chan.allNotesOff();
            refresharray();
        }
    }
    
    private static void refresharray() throws Exception {
        clearmarked();
        for(int i = 0; i < array.length; i++){
            marked.set(0,i);
            Thread.sleep(sleepTime(0.001));
        }
        marked.set(0, -1);
        Thread.sleep(1000);
        shuffle(array);
        aa = 0;
        comps = 0;
        clearmarked();
    }
    
    public static void quickSort(int[] a, int p, int r) throws InterruptedException
    {
        if(p<r)
        {
            int q=partition(a,p,r);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
            }
            quickSort(a,p,q);
            quickSort(a,q+1,r);
        }
    }

    private static int partition(int[] a, int p, int r) throws InterruptedException {

        int x = a[p];
        int i = p-1 ;
        int j = r+1 ;

        while (true) {
            Thread.sleep(sleepTime(0.005));
            i++;
            while ( i< r && a[i] < x){
                i++;
                comps+=2;
            }
            j--;
            while (j>p && a[j] > x){
                j--;
                comps+=2;
            }

            if (i < j)
                swap(a, i, j);
            else
                return j;
        }
    }

    private static void swap(int[] a, int i, int j) {
        marked.set(1, i);
        marked.set(2, j);
        aa+=2;
            //if(Math.random()*8<1.0)
                //Thread.sleep(1);
        // TODO Auto-generated method stub
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static void swap(int[] a, int i, int j, int pause) {
        marked.set(1, i);
        marked.set(2, j);
        aa+=2;
                try {
            Thread.sleep(pause);
        } catch (InterruptedException ex) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO Auto-generated method stub
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
        private static void swapnm(int[] a, int i, int j, int pause) {
                try {
            Thread.sleep(pause);
        } catch (InterruptedException ex) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        aa+=2;
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void selectionSort() throws Exception {
        for (int i = 0; i < array.length - 1; i++) {
            int lowestindex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[lowestindex]){
                    lowestindex = j;
                    comps++;
                }
            }
            swap(array, i, lowestindex);
                Thread.sleep(sleepTime(0.01));
        }
    }
    
    static void merge(int[]ints,int min,int max,int mid){
		int i=min;
		while(i<=mid){
			if(ints[i]>ints[mid+1]){
                                comps++;
				swap(ints,i,mid+1,sleepTime(0.005));
				push(ints,mid+1,max);
			}			
			i++;
		}		
	}
    
    static void push(int[] ints,int s,int e){
		for(int i=s;i<e;i++){
			if(ints[i]>ints[i+1]){
                            comps++;
                            swap(ints,i,i+1);
                        }
		}
	}
    
    static void mergeSort(int[] ints,int min,int max){
	if(max-min==0){//only one element.
		//no swap
	}
	else if(max-min==1){//only two elements and swaps them
            if(ints[min]>ints[max])
                swap(ints,min,max);
	}
        else{
            int mid=((int) Math.floor((min+max)/2));//The midpoint

            mergeSort(ints,min,mid);//sort the left side
            mergeSort(ints,mid+1,max);//sort the right side
            merge(ints,min,max,mid);//combines them
        }
    }

    public static void radixLSDsort(int[] a, int radix)throws Exception {
        clearmarked();
        int highestpower = analyze(radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int p = 0; p <= highestpower; p++){
            for(int i = 0; i < array.length; i++){
                registers[getDigit(array[i],p,radix)].add(array[i]);
            }
            //transcribe(registers,array);
            transcribe(registers, array);
        }
    }
    
    public static int getDigit(int a, int power, int radix){
        return (int) (a / Math.pow(radix, power)) % radix;
    }
    
    private static int[] rianr(int [] arr) {
        for (int i = 0; i < arr.length; i++)
            arr[i] = i;
        shuffle(arr);
        return arr;
    }

    private static void transcribe(ArrayList<Integer>[] registers, int[] array)throws Exception {
        int total = 0;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                Thread.sleep(1);
                array[total] = registers[ai].get(i);
                marked.set(1, total);
                total++;
                aa++;
            }
            registers[ai].clear();
        }
    }

    private static void inPlaceRadixLSDSort(int radix){
        int pos = 0;
        int[] vregs = new int[radix-1];
        int maxpower = analyze(radix);
        for(int p = 0; p <= maxpower; p++){
            for(int i = 0; i < vregs.length; i++)
                vregs[i]=array.length-1;
            pos = 0;
            for(int i = 0; i < array.length; i++){
                int digit = getDigit(array[pos], p, radix);
                if(digit==0) {
                    pos++;
                    marked.set(0, pos);
                } else {
                    for(int j = 0; j<vregs.length;j++)
                        marked.set(j+1,vregs[j]);
                    swapUpTo(pos,vregs[digit-1]);
                    for(int j = digit-1; j > 0; j--)
                        vregs[j-1]--;
                }
            }
                
        }
    }
     private static void swapUpTo(int pos, int to){
         for(int i = pos; i < to; i++)
            swapnm(array, i, i+1, i%240/239);
     }
    
    private static void swapUp(int pos) {
        for(int i = pos; i < array.length; i++)
            swap(array, i, i+1,i%180/179);
    }

    private static int analyze(int base) {
        int a = 0;
        for(int i = 0; i < array.length; i++)
            if((int)(Math.log(array[i])/Math.log(base))>a){
                a=(int)(Math.log(array[i])/Math.log(base));
            }
        return a;
    }
    
    private static void fancyTranscribe(ArrayList<Integer>[] registers, int [] arr) throws Exception {
        int[] tmp = new int[arr.length];
        int radix = registers.length;
        transcribe(registers, tmp);
        for(int i = 0; i < tmp.length; i++){
            int register = i%radix;
            if(register == 0)
                Thread.sleep(sleepTime(0.05));//radix
            int pos = (int)(((double)register*((double)tmp.length/radix))+((double)i/radix));
            arr[pos]=tmp[pos];
            marked.set(register,pos);
        }
        clearmarked();
    }
    
    public static void clearmarked(){
        for(int i = 0; i < array.length; i++)
            marked.set(i, -1);
    }

    private static void cocktailShakerSort(int[] arr) {
        int left = 0;
        int right = arr.length-1;
        int smallest = 0;
        int biggest = 0;
        while(left<=right){
            for(int i = left; i <= right; i++){
                if(arr[i]>arr[biggest])
                    biggest = i;
                if(arr[i]<arr[smallest])
                    smallest = i;
                comps+=2;
            }
            if(biggest==left)
                biggest = smallest;
            swap(arr, left, smallest, sleepTime(0.01));
            swap(arr, right, biggest, sleepTime(0.01));
            left++;
            right--;
            smallest = left;
            biggest = right;
        }
    }

    private static void shuffle(int[] array) {
        for(int i = 0; i < array.length; i++)
            swap(array, i, (int)(Math.random()*array.length));
    }
    
    private static void timeSort() throws Exception {
        final int A = 2;
        next = 0;
        shatterPartition(4);
        ArrayList<Thread> threads = new ArrayList<Thread>();
        final int[] tmp = array.clone();
        for(int i = 0; i < array.length; i++){
            final int c = i;
            threads.add(new Thread(){
                public void run() {
                    int a = tmp[c];
                    try {
                        sleep(a*A);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    report(a);
                }
            });
        }
        for(Thread t : threads)
            t.start();
        Thread.sleep(array.length * A);
        bubbleSort();
        
    }
    static volatile int next = 0;
    public static synchronized void report(int a){
        marked.set(0,next);
        array[next] = a;
        aa++;
        next++;
    }
    
    private static void bubbleSort() throws Exception{
        for(int i = array.length-1; i > 0; i--){
            marked.set(0,i);
            for(int j = 0; j < i; j++)
                if(array[j]>array[j+1]){
                    comps++;
                    swap(array, j, j+1, Math.max(j%50-48,0));
                }
        }
    }

    private static void insertionSort() {
        int pos;
        for(int i = 1; i < array.length; i++){
            pos = i;
            marked.set(0, i);
            while(pos>0&&array[pos]<=array[pos-1]){
                    comps+=2;
                    swap(array, pos, pos-1,Math.max(pos%50-48,0));
                    pos--;
            }
        }
    }
    
    /*private static void insertionSort(int slp) {
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
    
    private static void shatterPartition(int num) throws Exception {
        int shatters = (int)Math.ceil(array.length/(double)num);
        ArrayList<Integer>[] registers = new ArrayList[shatters];
        for(int i = 0; i < shatters; i++)
            registers[i] = new ArrayList<Integer>();
        for(int i = 0; i < array.length; i++)
            registers[array[i]/num].add(array[i]);
        transcribe(registers, array);
    }
    
    private static void shatterSort(int num) throws Exception {
        int shatters = (int)Math.ceil(array.length/(double)num);
        shatterPartition(num);
        int[] tmp = new int[num];
        for(int i = 0; i < shatters; i++){
            for(int j = 0; j < num; j++){
                if(i*num+j>=array.length)
                    tmp[j] = -1;
                else
                    tmp[j]=array[i*num+j];
            }
            for(int j = 0; j < tmp.length; j++){
                if(i*num+(tmp[j]%num)>=array.length || tmp[j] == -1)
                    break;
                array[i*num+(tmp[j]%num)]=tmp[j];
                aa++;
                marked.set(1, i*num+(tmp[j]%num));
                Thread.sleep(sleepTime(0.02));
            }
        }
    }
    
    private static void simpleShatterSort(int num, int rate) throws Exception {
        for(int i = num; i > 1; i = i/rate)
            shatterPartition(i);
        shatterPartition(1);
    }

    private static int sleepTime(double d) {
        return (int)(array.length*d)/4;
    }
}
