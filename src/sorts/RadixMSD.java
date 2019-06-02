/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.Analysis.analyze;
import static array.visualizer.ArrayVisualizer.clearmarked;
import static array.visualizer.ArrayVisualizer.getDigit;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.tempStores;
import static array.visualizer.Writes.mockWrite;
import static array.visualizer.Writes.transcribe;

import java.util.ArrayList;

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
public class RadixMSD {

    public static void radixMSDSort(int[] array, int radix) throws Exception {
        clearmarked();
        int highestpower = analyze(array, radix, 0, false, false);
        int[] tmp = new int[array.length];
        System.arraycopy(array, 0, tmp, 0, array.length);
        tempStores += array.length;
        radixMSDRec(array,0,array.length,radix,highestpower);
    }
    
    @SuppressWarnings("unchecked")
	private static void radixMSDRec(int[] array, int min, int max, int radix, int pow)throws Exception{
        if(min >= max || pow < 0)
            return;
        marked.set(2,max);
        marked.set(3, min);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int i = min; i < max; i++){
            marked.set(1,i);
            registers[getDigit(array[i], pow, radix)].add(array[i]);
            mockWrite(array, 0.65);
        }
        marked.set(2, -5);
        marked.set(3, -5);
        transcribe(array,registers,0,true,min,true);
        
        int sum = 0;
        for(int i = 0; i < registers.length; i++){
            radixMSDRec(array, sum+min, sum+min+registers[i].size(), radix, pow-1);
            sum+=registers[i].size();
            registers[i].clear();
            tempStores += registers[i].size();
        }
    }
}