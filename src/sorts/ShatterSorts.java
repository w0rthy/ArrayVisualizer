/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.mockWrite;
import static array.visualizer.Writes.transcribe;
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
public class ShatterSorts {
    @SuppressWarnings("unchecked")
	private static void shatterPartition(int[] array, int num) throws Exception {
        int shatters = (int)Math.ceil(array.length/(double)num);
        ArrayList<Integer>[] registers = new ArrayList[shatters];
        for(int i = 0; i < shatters; i++)
            registers[i] = new ArrayList<Integer>();
        for(int i = 0; i < array.length; i++){
            registers[array[i]/num].add(array[i]);
            marked.set(1, i);
            mockWrite(array, 0.5);
        }
        transcribe(array,registers,0,false,0,true);
    }
    
    public static void shatterSort(int[] array, int num) throws Exception {
        int shatters = (int)Math.ceil(array.length/(double)num);
        shatterPartition(array, num);
        int[] tmp = new int[num];
        for(int i = 0; i < shatters; i++){
            for(int j = 0; j < num; j++){
                if(i*num+j>=array.length)
                    write(tmp, j, -1, 0.5, false, true);
                else
                	write(tmp, j, array[i * num + j], 0.5, false, true);
                marked.set(2, i*num+j);
            }
            for(int j = 0; j < tmp.length; j++){
                int tmpj = tmp[j];
                if(i*num+(tmpj%num)>=array.length || tmpj == -1)
                    break;
                write(array, i * num + (tmpj % num), tmpj, 1, false, false);
                marked.set(1, i*num+(tmpj%num));
            }
        }
    }
    
    public static void simpleShatterSort(int[] array, int num, int rate) throws Exception {
        for(int i = num; i > 1; i = i/rate)
            shatterPartition(array, i);
        shatterPartition(array, 1);
    }
}