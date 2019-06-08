/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.Analysis.analyze;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.multiDimWrite;
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

public class GravitySort {
    public static void gravitySort(int[] array, int length) throws Exception {
        int max = analyze(array, length, 0, 0.25, true, true);
        int[][] abacus = new int[length][max];
        for(int i = 0; i < length; i++){
            for(int j = 0; j < array[i]; j++) {
                multiDimWrite(abacus, i, abacus[0].length - j - 1, 1, 0, true, true);
        	}
        }
        //apply gravity
        for(int i = 0; i < abacus[0].length; i++){
            for(int j = 0; j < abacus.length; j++){
                if(abacus[j][i]==1){
                    //Drop it
                    int droppos = j;
                    while(droppos+1 < abacus.length && abacus[droppos][i] == 1)
                        droppos++;
                    if(abacus[droppos][i]==0){
                    	multiDimWrite(abacus, j, i, 0, 0, true, true);
                    	multiDimWrite(abacus, droppos, i, 1, 0, true, true);
                    }
                }
            }
            
            int count = 0;
            for(int x = 0; x < abacus.length; x++){
                count = 0;
                for(int y = 0; y < abacus[0].length; y++)
                    count += abacus[x][y];
                write(array, x, count, 0.025, true, false);
            }
            marked.set(2, length - i - 1);
            sleep(0.05);
        }
    }
}