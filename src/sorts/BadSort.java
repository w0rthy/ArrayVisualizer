package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

/*
 *This example of an O(n^3) sorting algorithm may be found here, written by James Jensen (StriplingWarrior on StackOverflow):
 *https://stackoverflow.com/questions/27389344/is-there-a-sorting-algorithm-with-a-worst-case-time-complexity-of-n3
 */
 
public class BadSort {
	
	public static void badSort(int[] arr) {
	    for (int i = 0; i < arr.length; i++)
	    {
	        int shortest = i;
	        marked.set(3, shortest);
	        sleep(0.1);
	        for (int j = i; j < arr.length; j++)
	        {
	        	marked.set(1, j);
	        	sleep(0.1);
	            boolean isShortest = true;
	            for (int k = j + 1; k < arr.length; k++)
	            {
	            	marked.set(2, k);
	            	sleep(0.1);
	                if (compare(arr[j], arr[k]) == 1)
	                {
	                    isShortest = false;
	                    break;
	                }
	            }
	            if(isShortest)
	            {
	                shortest = j;
	                break;
	            }
	        }
	        swap(arr, i, shortest, 1, true);
	    }
	}
}