/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.utils;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;

/**
 * @author S630690
 */
public class Analysis
{
    public static int analyze(final ArrayController ac, int base)
    {
        int a = 0;
        for (int i = 0; i < ac.length; i++)
        {
            ac.marked.set(1, i);
            ac.aa++;
            sleep(1);
            if ((int) (Math.log(ac.array[i]) / Math.log(base)) > a)
            {
                a = (int) (Math.log(ac.array[i]) / Math.log(base));
            }
        }
        return a;
    }

    public static int analyzeMax(final ArrayController ac)
    {
        int a = 0;
        for (int i = 0; i < ac.length; i++)
        {
            if (ac.array[i] > a)
            {
                a = ac.array[i];
            }
            ac.marked.set(1, i);
            ac.aa++;
            sleep(1.0);
        }
        return a;
    }
}
