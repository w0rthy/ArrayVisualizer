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
 * @author S630690
 */
public class RadixMSD implements Sort
{
    private final int radix;

    public RadixMSD(int radix)
    {
        this.radix = radix;
    }

    private static void radixMSDSort(final ArrayController ac, int radix)
    {
        int highestPower = analyze(ac, radix);
        radixMSDRec(ac, 0, ac.length, radix, highestPower);
    }

    private static void radixMSDRec(final ArrayController ac, int min, int max, int radix, int pow)
    {
        if (min >= max || pow < 0)
        {
            return;
        }
        ac.marked.set(2, max);
        ac.marked.set(3, min);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for (int i = 0; i < radix; i++)
        {
            registers[i] = new ArrayList<>();
        }
        for (int i = min; i < max; i++)
        {
            ac.marked.set(1, i);
            registers[getDigit(ac.array[i], pow, radix)].add(ac.array[i]);
            ac.aa++;
        }
        transcribeRmsd(ac, registers, min);

        int sum = 0;
        for (ArrayList<Integer> register : registers)
        {
            radixMSDRec(ac, sum + min, sum + min + register.size(), radix, pow - 1);
            sum += register.size();
            register.clear();
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
