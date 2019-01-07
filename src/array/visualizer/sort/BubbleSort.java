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
public class BubbleSort implements Sort
{
    public static void bubbleSort(final ArrayController ac)
    {
        for (int i = ac.length - 1; i > 0; i--)
        {
            for (int j = 0; j < i; j++)
            {
                sleep(0.005);
                if (ac.array[j] > ac.array[j + 1])
                {
                    ac.comps++;
                    swap(ac, j, j + 1, 0.01);
                } else
                {
                    ac.marked.set(1, j + 1);
                    ac.marked.set(2, -5);
                }
            }
            //marked.set(0, i);
        }
    }

    @Override
    public String name()
    {
        return "Bubble Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        bubbleSort(ac);
    }
}
