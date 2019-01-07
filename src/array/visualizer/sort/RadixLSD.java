/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Analysis.*;
import static array.visualizer.utils.Transcriptions.*;

import java.util.ArrayList;

/**
 * @author S630690
 */
public class RadixLSD implements Sort
{

    private final int radix;

    public RadixLSD(int radix)
    {
        this.radix = radix;
    }

    public static void radixLSDsort_(final ArrayController ac, int radix)
    {
        clearmarked();
        int highestpower = analyze(ac, radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for (int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for (int p = 0; p <= highestpower; p++)
        {
            for (int i = 0; i < ac.length; i++)
            {
                ac.marked.set(1, i);
                sleep(1);
                registers[getDigit(ac.array[i], p, radix)].add(ac.array[i]);
            }
            //transcribe(registers, array);
            transcribe(registers, ac);
        }
    }

    public static void radixLSDsort(final ArrayController ac, int radix)
    {
        clearmarked();
        int highestpower = analyze(ac, radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for (int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for (int p = 0; p <= highestpower; p++)
        {
            for (int i = 0; i < ac.length; i++)
            {
                ac.aa++;
                ac.marked.set(1, i);
                if (i % 2 == 0)
                    sleep(1);
                registers[getDigit(ac.array[i], p, radix)].add(ac.array[i]);
            }
            fancyTranscribe(ac, registers);
        }
    }

    public static void radixLSDsortnd(final ArrayController ac, int radix, int min, int max)
    {
        clearmarked();
        int highestpower = analyze(ac, radix);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for (int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for (int p = 0; p <= highestpower; p++)
        {
            for (int i = min; i < max; i++)
            {
                registers[getDigit(ac.array[i], p, radix)].add(ac.array[i]);
            }
            //transcribe(registers, array);
            transcribend(ac, registers, min);
        }
    }

    @Override
    public String name()
    {
        return "Radix LSD Sort (Base " + radix + ")";
    }

    @Override
    public void sort(ArrayController ac)
    {
        radixLSDsort(ac, radix);
    }
}
