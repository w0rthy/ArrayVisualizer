/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

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
public class SelectionSorts {  
    public static void selectionSort(int[] array, int length) throws Exception {
        for (int i = 0; i < length - 1; i++) {
            int lowestindex = i;
            for (int j = i + 1; j < length; j++) {
            	marked.set(2, j);
            	sleep(0.1);
                if (compare(array[j], array[lowestindex]) == -1){
                    lowestindex = j;
                    marked.set(1, lowestindex);
                    sleep(0.1);
                }
            }
            swap(array, i, lowestindex, 0.2, true);
        }
    }
    
    public static void doubleSelectionSort(int[] arr, int length) {
        int left = 0;
        int right = length-1;
        int smallest = 0;
        int biggest = 0;
        while(left<=right){
            for(int i = left; i <= right; i++){
            	marked.set(3, i);
                if(compare(arr[i], arr[biggest]) == 1) {
                    biggest = i;
                    marked.set(1, biggest);
                    sleep(0.1);
                }
                if(compare(arr[i], arr[smallest]) == -1) {
                    smallest = i;
                    marked.set(2, smallest);
                    sleep(0.1);
                }
                sleep(0.1);
            }
            if(biggest==left)
                biggest = smallest;
            swap(arr, left, smallest, .2, true);
            swap(arr, right, biggest, .2, true);
            left++;
            right--;
            smallest = left;
            biggest = right;
        }
    }
}