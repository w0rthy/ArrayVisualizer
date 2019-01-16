/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Transcriptions.*;

import java.util.ArrayList;

/**
 * @author S630690
 */
public class ShatterSorts implements Sort
{
    private final int base;

    public ShatterSorts(int base)
    {
        this.base = base;
    }

    private static void shatterPartition(final ArrayController ac, int num)
    {
        int shatters = (int) Math.ceil(ac.length / (double) num);
        ArrayList<Integer>[] registers = new ArrayList[shatters];
        for (int i = 0; i < shatters; i++)
        {
            registers[i] = new ArrayList<>();
        }
        for (int i = 0; i < ac.length; i++)
        {
            registers[ac.array[i] / num].add(ac.array[i]);
            ac.aa++;
        }
        transcribe(registers, ac);
    }

    private static void shatterSort(final ArrayController ac, int num)
    {
        int shatters = (int) Math.ceil(ac.length / (double) num);
        shatterPartition(ac, num);
        int[] tmp = new int[num];
        for (int i = 0; i < shatters; i++)
        {
            for (int j = 0; j < num; j++)
            {
                if (i * num + j >= ac.length)
                {
                    tmp[j] = -1;
                } else
                {
                    tmp[j] = ac.array[i * num + j];
                }
                ac.aa++;
            }
            for (int tmpj : tmp)
            {
                if (i * num + (tmpj % num) >= ac.length || tmpj == -1)
                {
                    break;
                }
                ac.array[i * num + (tmpj % num)] = tmpj;
                ac.aa++;
                ac.marked.set(1, i * num + (tmpj % num));
                sleep(sleepTime(0.02));
            }
        }
    }

    public static void simpleShatterSort(final ArrayController ac, int num, int rate)
    {
        for (int i = num; i > 1; i = i / rate)
        {
            shatterPartition(ac, i);
        }
        shatterPartition(ac, 1);
    }

    @Override
    public String name()
    {
        return "Shatter Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        shatterSort(ac, base);
    }
}
