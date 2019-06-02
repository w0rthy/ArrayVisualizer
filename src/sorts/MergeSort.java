/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.sleep;
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

public class MergeSort {
	private static void merge(int[] array, int min,int max,int mid){
		try {
			//radixLSDsortnd(2, min, max);


			int i=min;
			while(i<=mid){
				if(compare(array[i], array[mid+1]) == 1){
					swap(array,i,mid+1,0.1,true);
					push(array,mid+1,max);
				}
				i++;
				sleep(0.2);
			}		

		} catch (Exception ex) {
			Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void push(int[] array, int s,int e){

		for(int i=s;i<e;i++){
			if(compare(array[i], array[i+1]) == 1){
				swap(array,i,i+1,0.1,true);
			}
		}


	}

	public static void mergeSort(int[] array, int min,int max){
		if(max-min==0){//only one element.
			//no swap
		}
		else if(max-min==1){//only two elements and swaps them
			if(compare(array[min], array[max]) == 1) {
				swap(array,min,max,0.01,true);
			}
		}
		else{
			int mid=((int) Math.floor((min+max)/2));//The midpoint

			mergeSort(array,min,mid);//sort the left side
			mergeSort(array,mid+1,max);//sort the right side
			merge(array,min,max,mid);//combines them
		}
	}
}