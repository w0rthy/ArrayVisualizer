/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

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
                    swap(array, j, j+1, j%40/39);
            }
            for(int j = array.length-i-1; j > 1; j--){
                comps++;
                if(array[j]<array[j-1])
                    swap(array, j, j-1, j%40/39);
            }
            i++;
        }
    }
}
