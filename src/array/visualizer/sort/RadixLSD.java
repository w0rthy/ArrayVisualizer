/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Analysis.*;
import static array.visualizer.utils.Transcriptions.*;
import java.util.ArrayList;

/**
 *
 * @author S630690
 */
public class RadixLSD {
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
}
