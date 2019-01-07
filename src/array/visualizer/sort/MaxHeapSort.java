package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.utils.Swaps.*;

public class MaxHeapSort implements Sort
{

    private static int SLP = 1;

    private static void maxheapifyrec(final ArrayController ac, int pos, boolean max)
    {
        if (pos >= ac.length)
        {
            return;
        }

        int child1 = pos * 2 + 1;
        int child2 = pos * 2 + 2;

        maxheapifyrec(ac, child1, max);
        maxheapifyrec(ac, child2, max);

        if (child2 >= ac.length)
        {
            if (child1 >= ac.length)
            {
                return; //Done, no children
            }
            ac.comps++;
            if (ac.array[child1] > ac.array[pos])
            {
                swap(ac, pos, child1, SLP);
            }
            return;
        }

        //Find largest child
        int lrg = child1;
        ac.comps++;
        if (ac.array[child2] > ac.array[child1])
        {
            lrg = child2;
        }

        //Swap with largest child
        ac.comps++;
        if (ac.array[lrg] > ac.array[pos])
        {
            swap(ac, pos, lrg, SLP);
            percdwn(ac, lrg, true, ac.length);
        }
    }

    private static void percdwn(final ArrayController ac, int pos, boolean max, int len)
    {
        int child1 = pos * 2 + 1;
        int child2 = pos * 2 + 2;

        if (child2 >= len)
        {
            if (child1 >= len) //Done
            {
                return;
            } else
            {
                //Single Child
                ac.comps++;
                if ((max && (ac.array[child1] > ac.array[pos])) || (!max && (ac.array[child1] < ac.array[pos])))
                {
                    swap(ac, pos, child1, SLP);
                }
                return;
            }
        }

        ac.comps++;
        if (ac.array[child1] > ac.array[child2])
        {
            //Ensure child1 is the smallest for easy programming
            int tmp = child1;
            child1 = child2;
            child2 = tmp;
        }

        ac.comps++;
        if (max && (ac.array[child2] > ac.array[pos]))
        {
            swap(ac, pos, child2, SLP);
            percdwn(ac, child2, max, len);
        } else if (!max && (ac.array[child1] < ac.array[pos]))
        {
            swap(ac, pos, child1, SLP);
            percdwn(ac, child1, max, len);
        }
    }

    private static void maxheapsort(final ArrayController ac)
    {
        maxheapifyrec(ac, 0, true);
        for (int i = ac.length - 1; i > 0; i--)
        {
            swap(ac, 0, i, SLP);
            percdwn(ac, 0, true, i);
        }
    }

    @Override
    public String name()
    {
        return "Max Heap Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        maxheapsort(ac);
    }
}
