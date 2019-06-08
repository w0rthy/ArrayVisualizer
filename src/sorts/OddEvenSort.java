package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

/*
 * This version of Odd-Even Sort was taken from here, written by Rachit Belwariar:
 * https://www.geeksforgeeks.org/odd-even-sort-brick-sort/
 */

public class OddEvenSort {

	public static void oddEvenSort(int[] array, int length)
	{
	    boolean sorted = false;
	
	    while (!sorted)
	    {
	        sorted = true;
	
	        for (int i = 1; i < length-1; i += 2)
	        {
	            if(compare(array[i], array[i+1]) == 1)
	            {
	                swap(array, i, i+1, .2, true);
	                sorted = false;
	            }
	            marked.set(1, i);
	            sleep(.1);
	        }
	
	        for (int i = 0; i < length-1; i += 2)
	        {
	            if(compare(array[i], array[i+1]) == 1)
	            {
	            	swap(array, i, i+1, .2, true);
	                sorted = false;
	            }
	            marked.set(2, i);
	            sleep(.1);
	        }
	    }
	}
}