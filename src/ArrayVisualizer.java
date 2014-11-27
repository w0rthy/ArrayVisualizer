package array.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    static Font fon = new Font("TimesRoman",Font.PLAIN,(int)(640/1280.0*25));
    static boolean CIRCLEDRAW = false;
    static boolean COLORONLY = false;
    
    public static double calcVel(){
        double count = 1;
        for(int i : marked)
            if(i!=-1)
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
        try{
            Thread.sleep(amt);
        }catch(Throwable t){}
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
                        if(i != -5){
                            chan.noteOn(array[Math.min(Math.max(i, 0),array.length-1)]/32+47, ((int)((127-(array[Math.min(Math.max(i, 0),array.length-1)]/16.0))/Math.sqrt(calcVel())))*5);
                            double tmp = (array[Math.min(Math.max(i, 0),array.length-1)]/32.0+47);
                            chan.setPitchBend(8192*2-(int)((tmp-Math.floor(tmp))*8192*2));
                        }
                    
                    if(window.getWidth()!=cw|| window.getHeight()!=ch){
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        fon = new Font("TimesRoman",Font.PLAIN,(int)(cw/1280.0*25));
                        g = img.getGraphics();
                    }
                    g.setColor(Color.GRAY);
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
            
            private Color getIntColor(int i) {
                return Color.getHSBColor(((float)i/array.length), 1.0F, 1.0F);
            }
            private Color getRevColor(){
                return getIntColor((int)(Math.sin(frames/66.67)*array.length));
            }
        }.start();
        
        while(true){
            clearmarked();
            
            heading = "Linear Search";
            
            Arrays.sort(array);
            marked.set(1,730);
            linearSearch(730);
            
            chan.allNotesOff();
            refresharray();
            
            heading = "Binary Search";
            
            Arrays.sort(array);
            marked.set(1, 730);
            binarySearch(730);
            
            chan.allNotesOff();
            refresharray();
            
            //heading = "Shatter-Time Sort";

            //timeSort();

            //chan.allNotesOff();
            //refresharray();

            heading = "Gravity Sort (Abacus/Bead)";
            
            gravitySort();
            
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
    
    private static void refresharray() throws Exception {
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
    
    public static void quickSort(int[] a, int p, int r) throws InterruptedException
    {
        if(p<r)
        {
            int q=partition(a,p,r);
                sleep(1);
            quickSort(a,p,q);
            quickSort(a,q+1,r);
        }
    }

    private static int partition(int[] a, int p, int r) throws InterruptedException {

        int x = a[p];
        int i = p-1 ;
        int j = r+1 ;

        while (true) {
            sleep(sleepTime(0.005));
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
                //sleep(1);
        // TODO Auto-generated method stub
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static void swap(int[] a, int i, int j, int pause) {
        marked.set(1, i);
        marked.set(2, j);
        aa+=2;
            sleep(pause);
        // TODO Auto-generated method stub
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
        private static void swapnm(int[] a, int i, int j, int pause) {
            sleep(pause);
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
                sleep(sleepTime(0.01));
        }
    }
    
    static void merge(int min,int max,int mid){
        try {
            //radixLSDsortnd(2, min, max);
                
            
                    int i=min;
                    while(i<=mid){
                            if(array[i]>array[mid+1]){
                                    comps++;
                                    swap(array,i,mid+1,sleepTime(0.005));
                                    push(mid+1,max);
                            }			
                            i++;
                    }		
                    
        } catch (Exception ex) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
    
    static void push(int s,int e){
        
		for(int i=s;i<e;i++){
			if(array[i]>array[i+1]){
                            comps++;
                            swap(array,i,i+1);
                        }
		}
                
                
	}
    
    static void mergeSort(int min,int max){
	if(max-min==0){//only one element.
		//no swap
	}
	else if(max-min==1){//only two elements and swaps them
            if(array[min]>array[max])
                swap(array,min,max);
	}
        else{
            int mid=((int) Math.floor((min+max)/2));//The midpoint

            mergeSort(min,mid);//sort the left side
            mergeSort(mid+1,max);//sort the right side
            merge(min,max,mid);//combines them
        }
    }

    public static void radixLSDsort(int[] a, int radix)throws Exception {
        clearmarked();
        int highestpower = analyze(a, radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int p = 0; p <= highestpower; p++){
            for(int i = 0; i < a.length; i++){
                marked.set(1, i);
                sleep(1);
                registers[getDigit(a[i],p,radix)].add(a[i]);
            }
            //transcribe(registers,array);
            transcribe(registers, a);
        }
    }
    
    public static void radixLSDsort(int radix)throws Exception{
        clearmarked();
        int highestpower = analyze(array,radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int p = 0; p <= highestpower; p++){
            for(int i = 0; i < array.length; i++){
                aa++;
                marked.set(1, i);
                if(i%2==0)
                    sleep(1);
                registers[getDigit(array[i],p,radix)].add(array[i]);
            }
            fancyTranscribe(registers);
        }
    }
    
    public static void radixLSDsortnd(int radix, int min, int max)throws Exception {
        clearmarked();
        int highestpower = analyze(array, radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int p = 0; p <= highestpower; p++){
            for(int i = min; i < max; i++){
                registers[getDigit(array[i],p,radix)].add(array[i]);
            }
            //transcribe(registers,array);
            transcribend(registers, min);
        }
    }
    
    public static void radixMSDSort(int radix) throws Exception {
        clearmarked();
        int highestpower = analyze(radix);
        int[] tmp = new int[array.length];
        System.arraycopy(array, 0, tmp, 0, array.length);
        radixMSDRec(0,array.length,radix,highestpower);
    }
    
    public static void radixMSDRec(int min, int max, int radix, int pow)throws Exception{
        if(min >= max || pow < 0)
            return;
        marked.set(2,max);
        marked.set(3, min);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int i = min; i < max; i++){
            marked.set(1,i);
            registers[getDigit(array[i], pow, radix)].add(array[i]);
            aa++;
        }
        transcribermsd(registers,min);
        
        int sum = 0;
        for(int i = 0; i < registers.length; i++){
            radixMSDRec(sum+min, sum+min+registers[i].size(), radix, pow-1);
            sum+=registers[i].size();
            registers[i].clear();
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
                sleep(1);
                array[total] = registers[ai].get(i);
                marked.set(1, total);
                total++;
                aa++;
            }
            registers[ai].clear();
        }
    }
    
    private static void transcribenm(ArrayList<Integer>[] registers, int[] array)throws Exception {
        int total = 0;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                array[total] = registers[ai].get(i);
                total++;
            }
            registers[ai].clear();
        }
    }
    
    private static void transcribend(ArrayList<Integer>[] registers, int min)throws Exception {
        int total = 0;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                sleep((min+i)%5/4);
                array[total+min] = registers[ai].get(i);
                marked.set(1, total+min);
                total++;
                aa++;
            }
            registers[ai].clear();
        }
    }
    
    private static void transcribermsd(ArrayList<Integer>[] registers, int min)throws Exception {
        int total = 0;
        for(ArrayList<Integer> ai : registers)
            total+=ai.size();
        int tmp = 0;
        for(int ai = registers.length-1; ai >= 0; ai--){
            for(int i = registers[ai].size()-1; i >= 0; i--){
                sleep(1+2/registers[ai].size());
                array[total+min-tmp-1] = registers[ai].get(i);
                marked.set(1, total+min-tmp-1);
                tmp++;
                aa++;
            }
        }
    }
    
    private static void transcribe(ArrayList<Integer>[] registers, int [] array, int start) throws Exception {
        int total = start;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                sleep(1);
                array[total] = registers[ai].get(i);
                marked.set(1, total);
                total++;
                aa++;
            }
            registers[ai].clear();
        }
    }

    private static void inPlaceRadixLSDSort(int radix)throws Exception{
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

    private static int analyze(int base)throws Exception {
        int a = 0;
        for(int i = 0; i < array.length; i++){
            marked.set(1, i);
            aa++;
            sleep(1);
            if((int)(Math.log(array[i])/Math.log(base))>a){
                a=(int)(Math.log(array[i])/Math.log(base));
            }
        }
        return a;
    }
    
    private static int analyze(int[] ar, int base) {
        int a = 0;
        for(int i = 0; i < ar.length; i++)
            if((int)(Math.log(ar[i])/Math.log(base))>a){
                a=(int)(Math.log(ar[i])/Math.log(base));
            }
        return a;
    }
    
    private static int analyzemax() throws Exception{
        int a = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i]>a)
                a=array[i];
            marked.set(1,i);
            aa++;
            sleep(1);
        }
        return a;
    }
    
    private static void fancyTranscribe(ArrayList<Integer>[] registers) throws Exception {
        int[] tmp = new int[array.length];
        boolean[] tmpwrite = new boolean[array.length];
        int radix = registers.length;
        transcribenm(registers, tmp);
        for(int i = 0; i < tmp.length; i++){
            int register = i%radix;
            if(register == 0)
                sleep(radix);//radix
            int pos = (int)(((double)register*((double)tmp.length/radix))+((double)i/radix));
            if(tmpwrite[pos]==false){
                array[pos]=tmp[pos];
                aa++;
                tmpwrite[pos] = true;
            }
            marked.set(register,pos);
        }
        for(int i = 0; i < tmpwrite.length; i++)
            if(tmpwrite[i]==false){
                array[i]=tmp[i];
                aa++;
            }
        clearmarked();
    }
    
    public static void clearmarked(){
        for(int i = 0; i < array.length; i++)
            marked.set(i, -5);
    }

    private static void cocktailShakerSort(){
        int i = 0;
        while(i<array.length/2){
            for(int j = i; j < array.length-i-1; j++){
                comps++;
                if(array[j]>array[j+1])
                    swap(array, j, j+1, j%40/39);
            }
            for(int j = array.length-i-1; j > 1; j--){
                comps++;
                if(array[j]<array[j-1])
                    swap(array, j, j-1, j%40/39);
            }
            i++;
        }
    }
    
    private static void doubleSelectionSort(int[] arr) {
        
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
        sleep(array.length * A);
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
                sleep(sleepTime(0.02));
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

    private static void mergeSortOP()throws Exception {
        int start = 0;
        int end = array.length;
        int mid = (end+start)/2;
        mergeOP(start,mid,end);
    }

    private static void mergeOP(int start, int mid, int end)throws Exception{
        if(start==mid)
            return;
        mergeOP(start, (mid+start)/2, mid);
        mergeOP(mid, (mid+end)/2, end);
        
        int[] tmp = new int[end-start];
        
        int low = start;
        int high = mid;
        for(int nxt = 0; nxt < tmp.length; nxt++){
            if(low >= mid && high >= end)
                break;
            if(low < mid && high >= end){
                tmp[nxt]=array[low];
                low++;
                comps+=2;
            }
            else if(low >= mid && high < end){
                tmp[nxt]=array[high];
                high++;
                comps+=2;
            }
            else if(array[low]<array[high]){
                tmp[nxt]=array[low];
                low++;
                comps+=3;
            }
            else{
                tmp[nxt]=array[high];
                high++;
                comps+=3;
            }
            aa++;
            marked.set(1,low);
            marked.set(2, high);
            if(end-start>=array.length/10)
                sleep(1);
        }
        //System.arraycopy(tmp, 0, array, start, tmp.length);
        marked.set(2, -5);
        for(int i = 0; i < tmp.length; i++){
            array[start+i]=tmp[i];
            aa++;
            marked.set(1, start+i);
            if(end-start>=array.length/100)
                sleep(1);
        }
    }
    
    private static void bitonicSort(){
        bitonicMerge(0,array.length,true);
    }
    
    private static void bitonicMerge(int start, int end, boolean dir){
        int mid = (start+end)/2;
        if(start==mid)
            return;
        bitonicMerge(start, mid, true);
        bitonicMerge(mid, end, false);
        
        int low = start;
        int high = end;
        
        if(dir)
            for(int i = 0; i < end-start; i++);
    }
    
    private static void gravitySort() throws Exception {
        int max = analyzemax();
        int[][] abacus = new int[array.length][max];
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i]; j++)
                abacus[i][abacus[0].length-j-1] = 1;
        }
        //apply gravity
        for(int i = 0; i < abacus[0].length; i++){
            for(int j = 0; j < abacus.length; j++){
                if(abacus[j][i]==1){
                    //Drop it
                    int droppos = j;
                    while(droppos+1 < abacus.length && abacus[droppos][i] == 1)
                        droppos++;
                    if(abacus[droppos][i]==0){
                        abacus[j][i]=0;
                        abacus[droppos][i]=1;
                        aa+=2;
                    }
                }
            }
            
            int count = 0;
            for(int x = 0; x < abacus.length; x++){
                count = 0;
                for(int y = 0; y < abacus[0].length; y++)
                    count+=abacus[x][y];
                array[x] = count;
            }
            sleep(1);
        }
    }
    
    private static void countingSort() throws Exception {
        int max = analyzemax();
        int[] counts = new int[max+1];
        for(int i = 0; i < array.length; i++){
            marked.set(1,i);
            sleep(1);
            counts[array[i]]++;
            aa++;
        }
        int x = 0;
        for(int i = 0; i < array.length; i++){
            if(counts[x]==0)
                x++;
            array[i]=x;
            aa++;
            counts[x]--;
            marked.set(1, i);
            sleep(1);
        }
    }

    private static void linearSearch(int find) throws Exception {
        for(int i = 0; i < array.length; i++){
            aa++;
            comps++;
            if(array[i]==find)
                break;
            marked.set(0, i);
            sleep(5);
        }
        sleep(1000);
    }
    
    private static void binarySearch(int find) throws Exception {
        int at = array.length/2;
        int change = array.length/4;
        while(array[at]!=find && change > 0){
            marked.set(0, array[at]);
            comps+=2;
            aa++;
            Thread.sleep(1000);
            if(array[at]<find)
                at += change;
            else
                at -= change;
            change /= 2;
        }
        sleep(1000);
    }
}
