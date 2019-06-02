/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.mockWrite;
import static array.visualizer.Writes.swap;
import static array.visualizer.Writes.write;

import java.util.ArrayList;
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

/*
MIT License

Copyright (c) 2017 Rodney Shaghoulian

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

public class QuickSorts {
	
    public static void quickSort(int[] a, int p, int r) throws InterruptedException
    {
        if(p<r)
        {
            int q=partition(a,p,r);
            quickSort(a,p,q);
            quickSort(a,q+1,r);
        }
    }
    
    private static int partition(int[] a, int p, int r) throws InterruptedException {    	
    	int[] part = Arrays.copyOfRange(a, p, r);
        //int x = a[findAverage(a,p,r)];
    	int rand = ((int) (Math.random() * part.length));
        int x = part[rand];
    	int i = p ;
        int j = r ;

        marked.set(3, rand + p);
        
        while (true) {
            while ( i< r && compare(a[i], x) == -1){
                i++;
                marked.set(1, i);
                sleep(0.5);
            }
            while (j>p && compare(a[j], x) == 1){
                j--;
                marked.set(2, j);
                sleep(0.5);
            }

            if (i < j) {
            	if(i == rand + p || j == rand + p) {
            		marked.set(3, x);
            	}
                swap(a, i, j, 1, true);
            }
            else {
                return j;
            }
        }
    }
    
    /*
     *This example of a basic Dual-Pivot Quicksort may be found here, written by Sebastian Wild (Sebastian on StackOverflow):
     *https://cs.stackexchange.com/questions/24092/dual-pivot-quicksort-reference-implementation
     */
    public static void dualPivot(int[] a, int left, int right)
    {
        if (right > left)
        {
            if (compare(a[left], a[right]) == 1) {
                swap(a, left, right, 1, true);
            }
            
            int p = a[left];
            int q = a[right];
            
            int l = left + 1;
            int g = right - 1;
            int k = l;

            while (k <= g)
            {
            	sleep(.1);
            	if (compare(a[k], p) == -1) {
                	swap(a, k, l, 1, true);
                	marked.set(2, -5);
                    l++;
                    marked.set(4, l);
                }
                else if (compare(a[k], q) >= 0) {
                    while (compare(a[g], q) == 1 && k < g) {
                    	g--;
                    	marked.set(3, g);
                    	sleep(0.2);
                    }
                    
                    swap(a, k, g, 1, true);
                    marked.set(2, -5);
                    g--;
                    marked.set(3, g);
                    
                    if (compare(a[k], p) == -1) {
                        swap(a, k, l, 0.2, true);
                        marked.set(2, -5);
                        ++l;
                        marked.set(4, l);
                    }
                }
                ++k;
                marked.set(1, k);
                sleep(0.2);
            }
            --l;
            ++g;
            swap(a, left, l, 1, true);
            marked.set(2, -5);
            swap(a, right, g, 1, true);
            marked.set(2, -5);
            marked.set(3, -5);
            marked.set(4, -5);

            dualPivot(a, left, l - 1);
            dualPivot(a, l + 1, g - 1);
            dualPivot(a, g + 1, right);
        }
    }
    
 // Author: Rodney Shaghoulian
 // Github: github.com/RodneyShag
    
    public static void stableQuickSort(int [] array) {
        stableQuSort(array, 0, array.length - 1);
    }
    
    private static void stableQuSort(int [] array, int start, int end) {
        if (start < end) {
            int pivotIndex = stablePartition(array, start, end);
            stableQuSort(array, start, pivotIndex - 1);
            stableQuSort(array, pivotIndex + 1, end);
        }
    }
    
    /* Partition/Quicksort "Stable Sort" version using O(n) space */
    private static int stablePartition(int[] array, int start, int end) {
        
    	int pivotValue = array[start]; //poor pivot choice
    	marked.set(3, start);
        ArrayList<Integer> leftList  = new ArrayList<>();
        ArrayList<Integer> rightList = new ArrayList<>();

        for (int i = start + 1 ; i <= end; i++) {
        	marked.set(1, i);
            if (compare(array[i], pivotValue) == -1) {
            	leftList.add(array[i]);
            } else {
                rightList.add(array[i]);
            }
            mockWrite(array, 0.25);
        }
        
        /* Recreate array */
        copy(leftList, array, start);
        int newPivotIndex = start + leftList.size();
        write(array, newPivotIndex, pivotValue, 0.25, false, false);
        marked.set(1, newPivotIndex);
        copy(rightList, array, newPivotIndex + 1);

        return newPivotIndex;
    }
    
    private static void copy(ArrayList<Integer> list, int [] array, int startIndex) {
        for (int num : list) {
        	write(array, startIndex++, num, 0.25, false, false);
        	marked.set(1, startIndex);
        }
    }
}