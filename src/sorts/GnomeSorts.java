package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

public class GnomeSorts {

	//Code retrieved from http://www.algostructure.com/sorting/gnomesort.php
	public static void gnomeSort(int[] array)
	{
	    for (int i = 1; i < array.length;)
	    {
	        if (compare(array[i], array[i-1]) >= 0)
	        {
	            i++;
	            marked.set(1, i);
	            sleep(0.2);
	        }
	        else
	        {
	            swap(array, i, i-1, 0, true);
	            sleep(0.1);
	            marked.set(2, -5);
	            if (i > 1) {
	            	i--;
	            	marked.set(1, i);
	            	sleep(0.1);
	            }
	        }
	    }
	}

	//Taken from https://en.wikipedia.org/wiki/Gnome_sort
	private static void smartGnomeSort(int[] a, int upperBound) {
		int pos = upperBound;
		
		while(pos > 0 && compare(a[pos - 1], a[pos]) == 1) {
			swap(a, pos - 1, pos, 0, true);
			sleep(.2);
			pos--;
		}
	}

	public static void smartGnome(int[] array)
	{
	    for(int i = 1; i < array.length; i++) {
	    	smartGnomeSort(array, i);
	    }
	}
}