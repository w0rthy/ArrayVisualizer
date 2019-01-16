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
public class GravitySort implements Sort
{
    private static void gravitySort(final ArrayController ac)
    {
        int max = analyzeMax(ac);
        int[][] abacus = new int[ac.length][max];
        for (int i = 0; i < ac.length; i++)
        {
            for (int j = 0; j < ac.array[i]; j++)
            {
                abacus[i][abacus[0].length - j - 1] = 1;
            }
        }
        //apply gravity
        for (int i = 0; i < abacus[0].length; i++)
        {
            for (int j = 0; j < abacus.length; j++)
            {
                if (abacus[j][i] == 1)
                {
                    //Drop it
                    int dropPos = j;
                    while (dropPos + 1 < abacus.length && abacus[dropPos][i] == 1)
                    {
                        dropPos++;
                    }
                    if (abacus[dropPos][i] == 0)
                    {
                        abacus[j][i] = 0;
                        abacus[dropPos][i] = 1;
                        ac.aa += 2;
                    }
                }
            }

            for (int x = 0; x < abacus.length; x++)
            {
                int count = 0;
                for (int y = 0; y < abacus[0].length; y++)
                {
                    count += abacus[x][y];
                }
                ac.array[x] = count;
                sleep(0.002);
            }
            ac.marked.set(1, ac.length - i - 1);
            sleep(2);
        }
    }

    @Override
    public String name()
    {
        return "Gravity Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        gravitySort(ac);
    }
}
