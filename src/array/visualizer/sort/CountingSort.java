/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Analysis.*;

/**
 * @author S630690
 */
public class CountingSort implements Sort
{
    private static void countingSort(final ArrayController ac)
    {
        int max = analyzeMax(ac);
        int[] counts = new int[max + 1];
        for (int i = 0; i < ac.length; i++)
        {
            ac.marked.set(1, i);
            sleep(2);
            counts[ac.array[i]]++;
            ac.aa++;
        }
        int x = 0;
        for (int i = 0; i < ac.length; i++)
        {
            if (counts[x] == 0)
            {
                x++;
            }
            ac.array[i] = x;
            ac.aa++;
            counts[x]--;
            ac.marked.set(1, i);
            sleep(2);
        }
    }

    @Override
    public String name()
    {
        return "Counting Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        countingSort(ac);
    }
}
