/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Transcriptions.*;
import static array.visualizer.utils.Analysis.*;
import java.util.ArrayList;

/**
 *
 * @author S630690
 */
public class RadixMSD implements Sort {
    private final int radix;

    public RadixMSD(int radix)
    {
        this.radix = radix;
    }

    public static void radixMSDSort(final ArrayController ac, int radix) {
        clearmarked();
        int highestpower = analyze(ac, radix);
        int[] tmp = new int[ac.length];
        System.arraycopy(ac.array, 0, tmp, 0, ac.length);
        radixMSDRec(ac, 0, ac.length, radix, highestpower);
    }
    
    public static void radixMSDRec(final ArrayController ac, int min, int max, int radix, int pow){
        if(min >= max || pow < 0)
            return;
        ac.marked.set(2, max);
        ac.marked.set(3, min);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int i = min; i < max; i++){
            ac.marked.set(1, i);
            registers[getDigit(ac.array[i], pow, radix)].add(ac.array[i]);
            ac.aa++;
        }
        transcribermsd(ac, registers, min);
        
        int sum = 0;
        for(int i = 0; i < registers.length; i++){
            radixMSDRec(ac, sum+min, sum+min+registers[i].size(), radix, pow-1);
            sum+=registers[i].size();
            registers[i].clear();
        }
    }

    @Override
    public String name()
    {
        return "Radix MSD Sort (Base " + radix + ")";
    }

    @Override
    public void sort(ArrayController ac)
    {
        radixMSDSort(ac, radix);
    }
}
