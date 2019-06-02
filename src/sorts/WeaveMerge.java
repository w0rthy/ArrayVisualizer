/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.multiSwap;
import static array.visualizer.Writes.swap;

import java.util.logging.Level;
import java.util.logging.Logger;

import array.visualizer.ArrayVisualizer;

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

public class WeaveMerge {
	private static void weaveInsert(int[] arr, int start, int end) {
		int pos;
	       for(int j = start; j < end; j++){
	           pos = j;
	           marked.set(1, j);
	           marked.set(2, -5);
	           while(pos>start && compare(arr[pos], arr[pos-1]) < 1) {
	                   swap(arr, pos, pos-1, 0.2, true);
	                   pos--;
	           }
	       }
	}
	
	private static void weaveMerge(int[] arr,int min,int max,int mid){
		try {
			//radixLSDsortnd(2, min, max);


			int i=1;
			int target = (mid-min);
			while(i<=target){
				multiSwap(arr, mid+i, min+i*2-1, 0.05, true);
				i++;
			}
			weaveInsert(arr, min, max + 1);
			
			
			//sleep(100);

		} catch (Exception ex) {
			Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void weaveMergeSort(int[] array,int min,int max){
		if(max-min==0){//only one element.
			//no swap
		}
		else if(max-min==1){//only two elements and swaps them
			if(compare(array[min], array[max]) == 1) {
				swap(array,min,max,.01,true);
			}
		}
		else{
			int mid=((int) Math.floor((min+max)/2));//The midpoint

			weaveMergeSort(array,min,mid);//sort the left side
			weaveMergeSort(array,mid+1,max);//sort the right side
			weaveMerge(array,min,max,mid);//combines them
		}
	}
}