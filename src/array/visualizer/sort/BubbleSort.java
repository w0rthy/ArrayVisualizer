/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

/**
 *
 * @author S630690
 */
public class BubbleSort {
    public static void bubbleSort() throws Exception{
        for(int i = array.length-1; i > 0; i--){
            for(int j = 0; j < i; j++){
                sleep(0.005);
                if(array[j]>array[j+1]){
                    comps++;
                    swap(array, j, j+1,0.01);
                }else{
                    marked.set(1,j+1);
                    marked.set(2,-5);
                }
            }
            //marked.set(0,i);
        }
    }
}
