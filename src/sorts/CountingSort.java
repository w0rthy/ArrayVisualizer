/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.Analysis.analyze;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.tempStores;
import static array.visualizer.Writes.write;

import java.util.Arrays;

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

public class CountingSort {
    public static void countingSort(int[] array, int length) throws Exception {
    	int max = analyze(array, length, 0, 0, true, false);
    	int[] output = Arrays.copyOf(array, length);
        int[] counts = new int[max+1];
        for(int i = 0; i < length; i++){
            write(counts, array[i], counts[array[i]] + 1, 1, false, true);
            marked.set(1, i);
        }
        for (int i = 1; i <= max; i++) {
        	write(counts, i, counts[i] + counts[i - 1], 1, true, true);
        }
        for(int i = length - 1; i >= 0; i--){
            write(counts, array[i], counts[array[i]] - 1, 0, false, true);
            write(output, counts[array[i]], array[i], 0, false, true);
            tempStores--;
        }
        //Usually counting sort returns a sorted copy of the input array. The visualization pretends that happens here.
        for(int i = length - 1; i >= 0; i--) {
        	write(array, i, output[i], 1, false, false);
        	marked.set(1, i);
        }
    }
}