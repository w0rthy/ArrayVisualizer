/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;


import array.visualizer.ArrayController;
import array.visualizer.ArrayVisualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.sort.InsertionSort.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author S630690
 */
public class TimeSort implements Sort
{
    private final int magnitude;

    public TimeSort(int magnitude)
    {
        this.magnitude = magnitude;
    }

    public static void timeSort(final ArrayController ac, int magnitude)
    {
        final int A = magnitude;
        next = 0;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        final int[] tmp = ac.array.clone();
        for (int i = 0; i < ac.length; i++)
        {
            ac.marked.set(0, i);
            sleep(1);
            final int c = i;
            threads.add(new Thread()
            {
                public void run()
                {
                    int a = tmp[c];
                    try
                    {
                        sleep(a * A);
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    report(ac, a);
                }
            });
        }
        for (Thread t : threads)
            t.start();
        sleep(ac.length * A);
        insertionSort(ac, 0, ac.length, 0.2d);

    }

    static volatile int next = 0;

    public static synchronized void report(final ArrayController ac, int a)
    {
        ac.marked.set(0, next);
        ac.array[next] = a;
        ac.aa++;
        next++;
    }

    @Override
    public String name()
    {
        return "Time+Insertion Sort (Mul " + magnitude + ")";
    }

    @Override
    public void sort(ArrayController ac)
    {
        timeSort(ac, magnitude);
    }
}
