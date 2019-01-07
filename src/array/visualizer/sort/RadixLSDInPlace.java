package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.utils.Analysis.*;
import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

public class RadixLSDInPlace implements Sort
{
    private final int radix;

    public RadixLSDInPlace(int radix)
    {
        this.radix = radix;
    }

    private static void inPlaceRadixLSDSort(final ArrayController ac, int radix)
    {
        int[] vregs = new int[radix - 1];
        int maxPower = analyze(ac, radix);
        double smul = Math.sqrt(radix);
        for (int p = 0; p <= maxPower; p++)
        {
            int pos = 0;
            for (int i = 0; i < vregs.length; i++)
            {
                vregs[i] = ac.length - 1;
            }
            for (int i = 0; i < ac.length; i++)
            {
                int digit = getDigit(ac.array[pos], p, radix);
                if (digit == 0)
                {
                    pos++;
                    ac.marked.set(0, pos);
                } else
                {
                    for (int j = 0; j < vregs.length; j++)
                    {
                        ac.marked.set(j + 1, vregs[j]);
                    }
                    swapUpToNM(ac, pos, vregs[digit - 1], 0.0011 * smul);
                    for (int j = digit - 1; j > 0; j--)
                    {
                        vregs[j - 1]--;
                    }
                }
            }

        }
    }

    @Override
    public String name()
    {
        return "Radix LSD In-Place Sort (Base " + radix + ")";
    }

    @Override
    public void sort(ArrayController ac)
    {
        inPlaceRadixLSDSort(ac, radix);
    }
}
