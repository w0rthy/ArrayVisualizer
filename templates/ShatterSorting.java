package templates;

import java.util.ArrayList;

import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
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
 *
 */

public abstract class ShatterSorting extends Sort {
    protected ShatterSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    private void shatterPartition(int[] array, int length, int num) {
        int shatters = (int) Math.ceil(length / (double) num);
        
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] registers = new ArrayList[shatters];
        
        for(int i = 0; i < shatters; i++) {
            registers[i] = new ArrayList<>();
        }
        
        for(int i = 0; i < length; i++){
            registers[array[i] / num].add(array[i]);
            Highlights.markArray(1, i);
            
            Writes.mockWrite(length, array[i] / num, array[i], 0.5);
        }
        
        Writes.transcribe(array, registers, 0, true, false);
    }
    
    protected void shatterSort(int[] array, int length, int num) {
        int shatters = (int) Math.ceil(length / (double) num);
        
        shatterPartition(array, length, num);
        
        int[] tmp = new int[num];
        for(int i = 0; i < shatters; i++) {
            for(int j = 0; j < num; j++) {
                if(i * num + j >= length)
                    Writes.write(tmp, j, -1, 0.5, false, true);
                else
                    Writes.write(tmp, j, array[i * num + j], 0.5, false, true);
                
                Highlights.markArray(2, i * num + j);
            }
            
            Highlights.clearMark(2);
            
            for(int j = 0; j < tmp.length; j++) {
                int tmpj = tmp[j];
                
                if(i * num + (tmpj % num) >= length || tmpj == -1) {
                    break;
                }
                
                Writes.write(array, i * num + (tmpj % num), tmpj, 1, false, false);
                Highlights.markArray(1, i * num + (tmpj % num));
            }
            
            Highlights.clearMark(1);
        }
    }
    
    protected void simpleShatterSort(int[] array, int length, int num, int rate) {
        for(int i = num; i > 1; i = i / rate) {
            shatterPartition(array, length, i);
        }
        shatterPartition(array, length, 1);
    }
}