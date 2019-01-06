/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import static array.visualizer.ArrayVisualizer.*;

/**
 *
 * @author S630690
 */
public class BitonicSort {
    public static void bitonicSort(){
        bitonicMerge(0,array.length,true);
    }
    
    public static void bitonicMerge(int start, int end, boolean dir){
        int mid = (start+end)/2;
        if(start==mid)
            return;
        bitonicMerge(start, mid, true);
        bitonicMerge(mid, end, false);
        
        int low = start;
        int high = end;
        
        if(dir)
            for(int i = 0; i < end-start; i++);
    }
}
