/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.write;

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

public class InsertionSorts {
	
	public static void insertionSort(int[] array) {
        int pos;
        int current;
        for(int i = 1; i < array.length; i++){
            current = array[i];
            pos = i - 1;
            //marked.set(0, i);
            while(pos>=0&&compare(array[pos], current) > 0){
            	write(array, pos + 1, array[pos], 0.015, true, false);
                pos--;
            }
            write(array, pos + 1, current, 0.015, true, false);
        }
    }
    
    public static void partialInsert(int[] array, int start, int end, double sleep) {
        int pos;
        int current;
        for(int i = start; i < end; i++){
            current = array[i];
            pos = i - 1;
            //marked.set(0, i);
            while(pos>=0&&compare(array[pos], current) > 0){
            	write(array, pos + 1, array[pos], sleep, true, false);
                pos--;
            }
            write(array, pos + 1, current, sleep, true, false);
        }
    }
    
    public static void binaryInsertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int num = array[i];

            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                marked.set(1, lo);
                marked.set(2, mid);
                marked.set(3, hi);
                sleep(0.05);
                if (compare(num,array[mid]) <= 0) {
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo

            marked.set(1, -5);
            marked.set(3, -5);
            int j = i - 1;
            while (j >= lo)
            {
            	write(array, j + 1, array[j], 0.015, true, false);
                j--;
            }
            write(array, lo, num, 0.015, true, false);
            marked.set(1, -5);
            marked.set(2, -5);
        }
    }
    
    public static void partialBinaryInsert(int[] array, int start, int end) {
        for (int i = start; i < end; i++) {
            int num = array[i];

            int lo = start, hi = i;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                marked.set(2, mid);
                sleep(0.05);
                if (compare(num,array[mid]) <= 0) {
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo

            int j = i - 1;
            while (j >= lo)
            {
            	write(array, j + 1, array[j], 0.015, true, false);
                j--;
            }
            write(array, lo, num, 0.015, true, false);
            marked.set(1, -5);
            marked.set(2, -5);
        }
    }
    
    public static void partialBinaryInsert(int[] array, int start, int end, double sleep) {
        for (int i = start; i < end; i++) {
            int num = array[i];

            int lo = start, hi = i;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                marked.set(2, mid);
                sleep(sleep);
                if (compare(num,array[mid]) <= 0) {
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo

            int j = i - 1;
            while (j >= lo)
            {
            	write(array, j + 1, array[j], sleep, true, false);
                j--;
            }
            write(array, lo, num, sleep, true, false);
            marked.set(1, -5);
            marked.set(2, -5);
        }
    }
}