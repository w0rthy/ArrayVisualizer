package array.visualizer;

import java.util.ArrayList;

public class ArrayController
{
    public final int[] array;
    public final int length;
    public final ArrayList<Integer> marked;
    public long aa;
    public long comps;

    public ArrayController(int length)
    {
        array = new int[length];
        this.length = length;
        marked = new ArrayList<>();
        aa = 0;
        comps = 0;
    }

    public void clearMarked()
    {
        for (int i = 0; i < length; i++)
        {
            marked.set(i, -5);
        }
    }

    public int get(int index)
    {
        aa++;
        return array[index];
    }

    public int compare(int a, int b)
    {
        comps++;
        return Integer.compare(array[a], array[b]);
    }
}
