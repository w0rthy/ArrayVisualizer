/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.sort.InsertionSort.*;
import static array.visualizer.utils.Swaps.*;

/**
 * @author S630690
 */
public class WeaveMerge implements Sort
{
    private static void weaveMerge(final ArrayController ac, int min, int max, int mid)
    {
        //radixLSDSortNd(2, min, max);
        int i = 1;
        int target = (mid - min);
        while (i <= target)
        {
            //swapUpTo(mid+(i-min), min+(i-min)*2, 0.01);
            swapUpTo(ac, mid + i, min + i * 2 - 1, 0.01);
            i++;
            sleep(1);
        }
        insertionSort(ac, min, max + 1, 0.15);
        //sleep(100);
    }

    static void push(final ArrayController ac, int s, int e)
    {
        for (int i = s; i < e; i++)
        {
            if (ac.array[i] > ac.array[i + 1])
            {
                ac.comps++;
                swap(ac, i, i + 1);
            }
        }
    }

    private static void weaveMergeSort(final ArrayController ac, int min, int max)
    {
        if (max - min == 0)
        {//only one element.
            //no swap
        } else if (max - min == 1)
        {//only two elements and swaps them
            if (ac.array[min] > ac.array[max])
            {
                swap(ac, min, max);
            }
        } else
        {
            int mid = (min + max) / 2;//The midpoint

            weaveMergeSort(ac, min, mid);//sort the left side
            weaveMergeSort(ac, mid + 1, max);//sort the right side
            weaveMerge(ac, min, max, mid);//combines them
        }
    }

    @Override
    public String name()
    {
        return "Merge+Insertion Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        weaveMergeSort(ac, 0, ac.length - 1);
    }
}
