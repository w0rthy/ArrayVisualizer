package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.Writes.swap;
import static array.visualizer.Writes.write;

//Refactored from C++ code written by Manish Bhojasia, found here:
//https://www.sanfoundry.com/cpp-program-implement-weak-heap/
public class WeakHeapSort {
	
	private static int GETFLAG(int[] r, int x) {
		return ((r[(x) >> 3] >> ((x) & 7)) & 1);
	}
	
	private static void TOGGLEFLAG(int[] r, int x) { 
		int flag = r[(x) >> 3]; 
		flag ^= 1 << ((x) & 7);
		write(r, (x) >> 3, flag, 0, false, true);
	}

	/*
	 * Merge Weak Heap
	 */
	private static void WeakHeapMerge(int[] array, int[] r, int i, int j)
	{
	    if (compare(array[i], array[j]) == -1)
	    {
	        TOGGLEFLAG(r, j);
	        swap(array, i, j, 0.5, true);
	    }
	}
	/*
	 * Weak Heap Sort
	 */
	public static void weakHeapSort(int[] array, int length)
	{
	    int n = length;
	    if (n > 1)
	    {
	        int i, j, x, y, Gparent;
	        int s = (n + 7) / 8;
	        int[] r = new int[s];
	        for (i = 0; i < n / 8; ++i) {
	        	write(r, i, 0, 0, false, true);
	        }
	        for (i = n - 1; i > 0; --i)
	        {
	            j = i;
	            while ((j & 1) == GETFLAG(r, j >> 1))
	                j >>= 1;
	            Gparent = j >> 1;
	            WeakHeapMerge(array, r, Gparent, i);
	        }
	        for (i = n - 1; i >= 2; --i)
	        {
	            swap(array, 0, i, 0.5, true);
	            x = 1;
	            while ((y = 2 * x + GETFLAG(r, x)) < i)
	                x = y;
	            while (x > 0)
	            {
	                WeakHeapMerge(array, r, 0, x);
	                x >>= 1;
	            }
	        }
	        swap(array, 0, 1, 0.5, true);
	    }
	}
}