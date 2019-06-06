package array.visualizer;

import static array.visualizer.ArrayVisualizer.calcReal;
import static array.visualizer.ArrayVisualizer.clearmarked;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.realTimer;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.ArrayVisualizer.swaps;
import static array.visualizer.ArrayVisualizer.tempStores;
import static array.visualizer.ArrayVisualizer.writes;

import java.util.ArrayList;

/**
 *
 * @author S630690
 */
public class Writes {

    private static void updateSwap() {
        swaps++;
        writes += 2;
    }

    private static void markSwap(int a, int b) {
        marked.set(1, a);
        marked.set(2, b);
    }

    public static void swap(int[] array, int a, int b, double pause, boolean mark) {
        if(mark) {
            markSwap(a, b);
            sleep(pause);
        }

        long time = System.nanoTime();

        // TODO Auto-generated method stub
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;

        if(calcReal) realTimer += ((System.nanoTime() - time) / 1e+6);

        updateSwap();
    }

    public static void multiSwap(int[] array, int pos, int to, double sleep, boolean mark) {
        if(to - pos > 0) {
            for(int i = pos; i < to; i++) {
                swap(array, i, i + 1, 0, mark);
                sleep(sleep);
            }
        }
        else {
            for(int i = pos; i > to; i--) {
                swap(array, i, i - 1, 0, mark);
                sleep(sleep);
            }
        }
    }

    public static void write(int[] array, int at, int equals, double pause, boolean mark, boolean auxwrite) {
        if(mark) marked.set(1, at);
        if(auxwrite) tempStores++;
        else writes++;
        sleep(pause);

        long time = System.nanoTime();

        // TODO Auto-generated method stub
        array[at] = equals;

        if(calcReal) realTimer += ((System.nanoTime() - time) / 1e+6);
    }

    public static void multiDimWrite(int[][] array, int x, int y, int equals, double pause, boolean mark, boolean auxwrite) {
        if(mark) marked.set(1, x);
        if(auxwrite) tempStores++;
        else writes++;
        sleep(pause);

        long time = System.nanoTime();

        // TODO Auto-generated method stub
        array[x][y] = equals;

        if(calcReal) realTimer += ((System.nanoTime() - time) / 1e+6);
    }

    //Simulates a write in order to estimate time for values being written to an ArrayList
    public static void mockWrite(int[] array, double pause) {
        int[] mockArray = new int[array.length];
        tempStores++;
        sleep(pause);

        long time = System.nanoTime();

        // TODO Auto-generated method stub
        mockArray[mockArray.length - 1] = 1;

        if(calcReal) realTimer += ((System.nanoTime() - time) / 1e+6);
    }

    public static void transcribe(int[] array, ArrayList<Integer>[] registers, int start, boolean msd,
            int min, boolean mark, boolean auxwrite) throws Exception {
        int total = start;

        if(msd) {
            int temp = 0;

            for(ArrayList<Integer> list : registers) {
                total += list.size();
            }
            for(int index = registers.length - 1; index >= 0; index--) {
                for(int i = registers[index].size() - 1; i >= 0; i--) {
                    if(mark) sleep(1 + (2 / registers[index].size()));
                    write(array, total + min - temp++ - 1, registers[index].get(i), 0, mark, auxwrite);
                }
            }
        }
        else {
            for(int index = 0; index < registers.length; index++) {
                for(int i = 0; i < registers[index].size(); i++) {
                    if(mark) sleep(1);
                    write(array, total++, registers[index].get(i), 0, mark, auxwrite);
                }
                registers[index].clear();
            }
        }
    }

    public static void fancyTranscribe(int[] array, ArrayList<Integer>[] registers) throws Exception {
        int[] tempArray = new int[array.length];
        boolean[] tempWrite = new boolean[array.length];
        int radix = registers.length;

        transcribe(tempArray, registers, 0, false, 0, false, true);
        tempStores -= array.length;

        for(int i = 0; i < tempArray.length; i++) {
            int register = i % radix;
            if(register == 0) sleep(radix);

            int pos = (int) ((register * (tempArray.length / radix)) + (i / radix));
            if(!tempWrite[pos]) {
                write(array, pos, tempArray[pos], 0, false, false);
                tempWrite[pos] = true;
            }
            marked.set(register, pos);
        }
        for(int i = 0; i < tempWrite.length; i++) {
            if(!tempWrite[i]){
                write(array, i, tempArray[i], 0, false, false);
            }
        }
        clearmarked();
    }
}