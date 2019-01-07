/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.utils.Swaps.*;

/**
 * @author S630690
 */
public class MergeSort implements Sort
{
    private static void merge(final ArrayController ac, int min, int max, int mid)
    {
        //radixLSDSortNd(2, min, max);
        for (int i = min; i <= mid; i++)
        {
            if (ac.array[i] > ac.array[mid + 1])
            {
                ac.comps++;
                swap(ac, i, mid + 1, 1.5);
                push(ac, mid + 1, max);
            }
        }
    }

    private static void push(final ArrayController ac, int s, int e)
    {
        for (int i = s; i < e; i++)
        {
            if (ac.array[i] > ac.array[i + 1])
            {
                ac.comps++;
                swap(ac, i, i + 1, 0.0175);
            }
        }
    }

    private static void mergeSort(final ArrayController ac, int min, int max)
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
            mergeSort(ac, min, mid);//sort the left side
            mergeSort(ac, mid + 1, max);//sort the right side
            merge(ac, min, max, mid);//combines them
        }
    }

    @Override
    public String name()
    {
        return "Merge Sort In-Place";
    }

    @Override
    public void sort(ArrayController ac)
    {
        mergeSort(ac, 0, ac.length - 1);
    }
}
