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
public class SelectionSort implements Sort
{

    private static void selectionSort(final ArrayController ac)
    {
        for (int i = 0; i < ac.length - 1; i++)
        {
            int lowestIndex = i;
            for (int j = i + 1; j < ac.length; j++)
            {
                if (ac.array[j] < ac.array[lowestIndex])
                {
                    lowestIndex = j;
                }
                sleep(0.01);
                ac.comps++;
            }
            swap(ac, i, lowestIndex);
        }
    }


    @Override
    public String name()
    {
        return "Selection Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        selectionSort(ac);
    }
}
