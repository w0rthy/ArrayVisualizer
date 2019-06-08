/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.Analysis.analyze;
import static array.visualizer.ArrayVisualizer.clearmarked;
import static array.visualizer.ArrayVisualizer.getDigit;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.fancyTranscribe;
import static array.visualizer.Writes.mockWrite;
import static array.visualizer.Writes.multiSwap;
import static array.visualizer.Writes.write;

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
public class RadixLSD {
    @SuppressWarnings("unchecked")
	public static void radixLSDsort(int[] a, int length, int radix)throws Exception {
        clearmarked();
        int highestpower = analyze(a, length, radix, 0, false, false);
        ArrayList<Integer>[] registers = new ArrayList[radix];
        for(int i = 0; i < radix; i++)
            registers[i] = new ArrayList<Integer>();
        for(int p = 0; p <= highestpower; p++){
            for(int i = 0; i < length; i++){
                marked.set(1, i);
                registers[getDigit(a[i],p,radix)].add(a[i]);
                mockWrite(length, 1);
            }
            //transcribe(registers,array);
            fancyTranscribe(a, length, registers);
        }
    }
    
    public static void inPlaceRadixLSDSort(int[] array, int length, int radix)throws Exception{
        int pos = 0;
        int[] vregs = new int[radix-1];
        int maxpower = analyze(array, length, radix, 0.01, false, true);
        for(int p = 0; p <= maxpower; p++){
            for(int i = 0; i < vregs.length; i++) {
                write(vregs, i, length - 1, 0, false, true);
            }
            pos = 0;
            for(int i = 0; i < length; i++){
                int digit = getDigit(array[pos], p, radix);
                if(digit==0) {
                    pos++;
                    marked.set(0, pos);
                } else {
                    for(int j = 0; j<vregs.length;j++)
                        marked.set(j+1,vregs[j]);
                    multiSwap(array,pos,vregs[digit-1],0.0025,false);
                    for(int j = digit-1; j > 0; j--) {
                    	write(vregs, j - 1, vregs[j - 1] - 1, 0, false, true);
                    }
                }
            }
        }
    }
}