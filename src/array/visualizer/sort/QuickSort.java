/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

/**
 * @author S630690
 */
public class QuickSort implements Sort
{
    public static void quickSort(final ArrayController ac, int p, int r)
    {
        if (p < r)
        {
            int q = partition(ac, p, r);
            sleep(1);
            quickSort(ac, p, q);
            quickSort(ac, q + 1, r);
        }
    }

    public static int partition(final ArrayController ac, int p, int r)
    {

        int x = ac.array[p];
        int i = p - 1;
        int j = r + 1;

        while (true)
        {
            //sleep(0.);
            i++;
            while (i < r && ac.array[i] < x)
            {
                i++;
                ac.marked.set(1, i);
                sleep(0.45);
                ac.comps += 2;
            }
            j--;
            while (j > p && ac.array[j] > x)
            {
                j--;
                ac.marked.set(2, j);
                sleep(0.45);
                ac.comps += 2;
            }

            if (i < j)
                swap(ac, i, j);
            else
                return j;
        }
    }

    @Override
    public String name()
    {
        return "Quick Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        quickSort(ac, 0, ac.length - 1);
    }
}
