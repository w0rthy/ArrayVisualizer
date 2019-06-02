/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.calcReal;
import static array.visualizer.ArrayVisualizer.realTimer;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.ArrayVisualizer.tempStores;
import static array.visualizer.Writes.write;
import static sorts.InsertionSorts.partialInsert;

import java.util.ArrayList;
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
public class TimeSort {
    public static void timeSort(int[] array, int magnitude) throws Exception {
        final int A = magnitude;
        next = 0;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        final int[] tmp = array.clone();
        tempStores += array.length;
        for(int i = 0; i < array.length; i++){
            final int c = i;
            threads.add(new Thread(){
                public void run() {
                    int a = tmp[c];
                    try {
                        sleep(a*A);
                        if(calcReal) realTimer += A;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    report(array, a);
                }
            });
        }
        for(Thread t : threads)
            t.start();
        Thread.sleep(array.length * A);
        partialInsert(array,0,array.length,0.2);
        sleep(2);
    }
    static volatile int next = 0;
    private static synchronized void report(int[] array, int a){
        write(array, next, a, 0, true, false);
        next++;
    }
}