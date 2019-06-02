package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.write;

/*
Copyright (c) rosettacode.org
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.3
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
*/

public class CycleSort {
	
	public static void cycleSort(int[] a) {
	    for (int cycleStart = 0; cycleStart < a.length - 1; cycleStart++) {
	      int val = a[cycleStart];
	      /*
	        Count the number of values that are smaller 
	        than val since cycleStart
	      */
	      int pos = cycleStart;
	      marked.set(3, pos);
	      for (int i = cycleStart + 1; i < a.length; i++) {
	    	marked.set(2, i);
	      	sleep(0.01);
	    	if (compare(a[i], val) == -1) {
	      	  pos++;
	          marked.set(1, pos);
	          sleep(0.01);
	        }
	      	else {
	      		
	      	}
	      }
	
	      // there aren't any
	      if (pos == cycleStart) {
	    	marked.set(1, pos);
	        continue;
	      }
	
	      // Skip duplicates
	      while (val == a[pos]) {
	        pos++;
	        marked.set(1, pos);
	      }
	
	      // Put val into final position
	      int tmp = a[pos];
	      write(a, pos, val, 0.02, true, false);
	      val = tmp;
	
	      /*
	        Repeat as long as we can find values to swap
	        otherwise start new cycle
	      */
	      while (pos != cycleStart) {
	        pos = cycleStart;
	        for (int i = cycleStart + 1; i < a.length; i++) {
	          marked.set(2, i);
	          sleep(0.01);
	          if (compare(a[i], val) == -1) {
	        	pos++;
	            marked.set(1, pos);
	            sleep(0.01);
	          }
	          else {
	        	  
	          }
	        }
	
	        while (val == a[pos]) {
	          pos++;
	          marked.set(1, pos);
	        }
	        
	        tmp = a[pos];
	        write(a, pos, val, 0.02, true, false);
	        val = tmp;
	      }
	    }
	}
}