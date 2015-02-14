/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Transcriptions.*;
import static array.visualizer.Analysis.*;
import java.util.ArrayList;

/**
 *
 * @author S630690
 */
public class RadixMSD {
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
}
