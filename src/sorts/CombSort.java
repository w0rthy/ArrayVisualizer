package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

import static sorts.InsertionSorts.partialInsert;

/*
The MIT License (MIT)

Copyright (c) 2012 Daniel Imms, http://www.growingwiththeweb.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

public class CombSort {
	public static void combSort(int[] array, int length, boolean hybrid)
	{
	    double shrink = 1.3;
	
	    boolean swapped = false;
	    int gap = length;
	
	    while ((gap > 1) || swapped)
	    {
	    	marked.set(2, -5);
	    	
	        if (gap > 1) {
	            gap = (int) ((float)gap / shrink);
	        }
	
	        swapped = false;
	
	        for (int i = 0; (gap + i) < length; ++i)
	        {
	        	if(hybrid && (gap <= Math.min(8, length * 0.03125))) {
	        		gap = 0;
	        		partialInsert(array, 0, length, 0.5);
	        		break;
	        	}
	            if (compare(array[i], array[i + gap]) == 1)
	            {
	                swap(array, i, i+gap, 1, true);
	                swapped = true;
	            }
	            marked.set(1, i);
	            marked.set(2, i + gap);
	            sleep(0.5);
	            marked.set(1, -5);
	        }
	    }
	}
}