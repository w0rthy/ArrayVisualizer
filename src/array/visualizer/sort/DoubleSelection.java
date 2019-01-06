/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

/**
 *
 * @author S630690
 */
public class DoubleSelection implements Sort {
    public static void doubleSelectionSort(final ArrayController ac) {
        
        int left = 0;
        int right = ac.length-1;
        int smallest = 0;
        int biggest = 0;
        while(left<=right){
            for(int i = left; i <= right; i++){
                ac.marked.set(1, i);
                if(ac.array[i]>ac.array[biggest])
                    biggest = i;
                if(ac.array[i]<ac.array[smallest])
                    smallest = i;
                ac.comps+=2;
                sleep(0.02);
            }
            if(biggest==left)
                biggest = smallest;
            swap(ac, left, smallest, 0.01);
            swap(ac, right, biggest, 0.01);
            left++;
            right--;
            smallest = left;
            biggest = right;
        }
    }

    @Override
    public String name()
    {
        return "Double Selection Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        doubleSelectionSort(ac);
    }
}
