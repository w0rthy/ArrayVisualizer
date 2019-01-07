package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

public class ShellSort implements Sort
{
    private static void shellSort(final ArrayController ac, int gap, int divRate)
    {
        double sleepAmt = 1d;
        while (gap > 0)
        {
            for (int j = 0; j <= gap - 1; j++)
            {
                for (int i = j + gap; i < ac.length; i += gap)
                {
                    int pos = i;
                    int prev = pos - gap;
                    while (prev >= 0)
                    {
                        if (ac.array[pos] < ac.array[prev])
                        {
                            ac.comps++;
                            swap(ac, pos, prev);
                            sleep(sleepAmt);
                        } else
                        {
                            ac.aa += 2;
                            break;
                        }
                        pos = prev;
                        prev = pos - gap;
                    }
                }
            }

            if (gap == 1) //Done
            {
                break;
            }

            gap = Math.max(gap / divRate, 1); //Ensure that we do gap 1
            //sleepAmt /= divRate;
        }
    }

    @Override
    public String name()
    {
        return "Shell Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        shellSort(ac, ac.length, 2);
    }
}
