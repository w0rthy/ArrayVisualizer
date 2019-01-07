/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

/**
 * @author S630690
 */
public class BitonicSort implements Sort
{
    public static void bitonicSort(final ArrayController ac)
    {
        bitonicMerge(ac, 0, ac.length, true);
    }

    public static void bitonicMerge(final ArrayController ac, int start, int end, boolean dir)
    {
        int mid = (start + end) / 2;
        if (start == mid)
            return;
        bitonicMerge(ac, start, mid, true);
        bitonicMerge(ac, mid, end, false);

        int low = start;
        int high = end;

        if (dir)
            for (int i = 0; i < end - start; i++) ;
    }

    @Override
    public String name()
    {
        return "Bitonic Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        bitonicSort(ac);
    }
}
