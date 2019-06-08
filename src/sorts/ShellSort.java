package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.write;

//Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps

public class ShellSort {
	public static void shellSort(int[] array, int length, int start)
    {
        int incs[] = {1391376, 463792, 198768, 86961, 33936,
                            13776, 4592, 1968, 861, 336,
                            112, 48, 21, 7, 3, 1};

        for (int k = start; k < 16; k++)
        {
            for (int h = incs[k], i = h; i < length; i++)
            {
                int v = array[i];
                int j = i;
                
                marked.set(1, j);
            	marked.set(2, j-h);
            	sleep(0.2);
            	
                while (j >= h && compare(array[j-h], v) == 1)
                {
                	marked.set(1, j);
                	marked.set(2, j-h);
                	write(array, j, array[j - h], 0.5, false, false);
                    j -= h;
                }
                write(array, j, v, 0.5, true, false);
            }
        }
    }
	
	public static void partShellSort(int[] array, int lo, int hi)
    {
        int incs[] = {48, 21, 7, 3, 1};

        for (int k = 0; k < 16; k++)
        {
            for (int h = incs[k], i = h + lo; i < hi; i++)
            {
                int v = array[i];
                int j = i;

                while (j >= h && compare(array[j-h], v) == 1)
                {
                	marked.set(1, j);
                	write(array, j, array[j - h], 1, true, false);
                    j -= h;
                }
                write(array, j, v, 0.5, true, false);
            }
        }
    }
}