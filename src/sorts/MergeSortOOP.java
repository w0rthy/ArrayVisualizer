/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.write;
import static sorts.InsertionSorts.partialBinaryInsert;

/**
/*
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

public class MergeSortOOP {
	public static void mergeSortOOP(int[] array, int length, boolean binary)throws Exception {
		int start = 0;
		int end = length;
		int mid = (end+start)/2;
		mergeOOP(array,start,mid,end,binary);
	}

	private static void mergeOOP(int[] array,int start, int mid, int end, boolean binary)throws Exception{
		if(start==mid)
			return;
		mergeOOP(array, start, (mid+start)/2, mid, binary);
		mergeOOP(array, mid, (mid+end)/2, end, binary);

		int[] tmp = new int[end-start];

		if(end - start < 32 && binary) {
			return;
		}
		else if(end - start == 32 && binary) {
			partialBinaryInsert(array, start, end, 0.25);
		}
		else {

			int low = start;
			int high = mid;
			for(int nxt = 0; nxt < tmp.length; nxt++){
				if(low >= mid && high >= end)
					break;
				if(low < mid && high >= end){
					write(tmp, nxt, array[low++], 0, false, true);
				}
				else if(low >= mid && high < end){
					write(tmp, nxt, array[high++], 0, false, true);
				}
				else if(compare(array[low], array[high]) == -1){
					write(tmp, nxt, array[low++], 0, false, true);
				}
				else{
					write(tmp, nxt, array[high++], 0, false, true);
				}
				marked.set(1,low);
				marked.set(2, high);
				sleep(1);
			}
			//System.arraycopy(tmp, 0, array, start, tmp.length);
			marked.set(2, -5);
			for(int i = 0; i < tmp.length; i++){
				write(array, start + i, tmp[i], 1, false, false);
				marked.set(1, start + i);
			}
		}
    }
        
	/**
	 *  Iterative merge sort algorithm --- as a static method

	 *  @author:  Sartaj Sahni, ported to Java by Timothy Rolfe

	 *  @see  Data Structures, Algorithms, and Applications in C++,
	 *        pp. 680-81 (the original in C++ has a memory leak, but
	 *        that is not a problem in Java due to garbage collection)

	 *  Minor revision by Timothy Rolfe (tjr) --- saves a comparison
	 */
	
	 //retrieved from http://penguin.ewu.edu/cscd300/Topic/AdvSorting/MergeSorts/MergeIter.html
       private static void merge (int[] c, int[] d, int lt, int md, int rt, boolean activeSound)
       {// Merge c[lt:md] and c[md+1:rt] to d[lt:rt]
          int i = lt,       // cursor for first segment
              j = md+1,     // cursor for second
              k = lt;       // cursor for result

       // merge until i or j exits its segment
          while ( (i <= md) && (j <= rt) ) {
        	  if (compare(c[i], c[j]) <= 0) {
            	 if(activeSound) {
            		 write(d, k++, c[i++], 0.5, false, true);
            		 marked.set(1, i);
            		 marked.set(2, j);
            	 }
            	 else {
            		write(d, k++, c[i++], 0.5, false, false);
            		marked.set(1, k-1);
            		marked.set(2, -5);
            	 }
             }
             else {
            	 if(activeSound) {
            		 write(d, k++, c[j++], 0.5, false, true);
            		 marked.set(1, j);
            		 marked.set(2, i);
            	 }
            	 else {
            		 write(d, k++, c[j++], 0.5, false, false);
            		 marked.set(1, k-1);
            		 marked.set(2, -5);
            	 }
             }
          	 sleep(0.5);
          }
       // take care of left overs --- tjr code:  only one while loop actually runs
          while ( i <= md ) {
              write(d, k++, c[i++], 1, true, !activeSound);
          }
          while ( j <= rt ) {
        	  write(d, k++, c[j++], 1, true, !activeSound);
          }
          
       } // end merge()

    /**
     * Perform one pass through the two arrays, invoking Merge() above
     */
       private static void mergePass (int x[], int y[], int s, int n, boolean activeSound)
       {// Merge adjacent segments of size s.
          int i = 0;
          
        	  while (i <= n - 2 * s)
              {//Merge two adjacent segments of size s
            	 merge (x, y, i, i+s-1, i+2*s-1, activeSound);
                 i = i + 2*s;
              }
           // fewer than 2s elements remain
              if (i + s < n) {
                 merge (x, y, i, i+s-1, n-1, activeSound);
              }
              else
                 for (int j = i; j <= n-1; j++) {
                  write(y, j, x[j], 1, false, !activeSound); // copy last segment to y
                   marked.set(1, j);
                   marked.set(2, -5);
                 }  
          
          
       }// end mergePass()

    /**
     * Entry point for merge sort
     */
       public static void stableSort (int a[], int n)
       {// Sort a[0:n-1] using merge sort.
          int   s = 16;      // segment size
          int[] b = new int [n];
          
          for(int i = 0; i <= n - 16; i += 16) {
        	  partialBinaryInsert(a, i, i + 16, 0.35);
          }
          
          while (s < n)
          {
        	 mergePass (a, b, s, n, true); // merge from a to b
             s += s;                 // double the segment size
             mergePass (b, a, s, n, false); // merge from b to a
             s += s;                 // again, double the segment size
          } // end while
          // in C/C++, return the scratch array b by free/delete  --- tjr
          // end mergeSort
       }// end MergeArray class
}