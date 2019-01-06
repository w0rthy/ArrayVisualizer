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
public class CocktailShaker {
    public static void cocktailShakerSort(){
        int i = 0;
        while(i<array.length/2){
            for(int j = i; j < array.length-i-1; j++){
                comps++;
                if(array[j]>array[j+1])
                    swap(array, j, j+1, 0.022);
            }
            for(int j = array.length-i-1; j > i; j--){
                comps++;
                if(array[j]<array[j-1])
                    swap(array, j, j-1, 0.022);
            }
            i++;
        }
    }
}
