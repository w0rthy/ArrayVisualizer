/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.utils;

import static array.visualizer.ArrayVisualizer.*;

/**
 *
 * @author S630690
 */
public class Searches {
    public static void linearSearch(int find) throws Exception {
        for(int i = 0; i < array.length; i++){
            aa++;
            comps++;
            if(array[i]==find)
                break;
            marked.set(0, i);
            sleep(5);
        }
        sleep(1000);
    }
    
    public static void binarySearch(int find) throws Exception {
        int at = array.length/2;
        int change = array.length/4;
        while(array[at]!=find && change > 0){
            marked.set(0, array[at]);
            comps+=2;
            aa++;
            Thread.sleep(1000);
            if(array[at]<find)
                at += change;
            else
                at -= change;
            change /= 2;
        }
        sleep(1000);
    }
}
