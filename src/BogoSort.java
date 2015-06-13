package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

public class BogoSort {
    public static boolean bogoIsSorted(int[] arr){
        for(int i = 1; i < arr.length; i++)
            if(arr[i]<arr[i-1])
                return false;
        return true;
    }
    
    public static void bogoSort(){
        while(!bogoIsSorted(array)){
            for(int i = 0; i < array.length; i++)
                swap(array, i, (int)(Math.random()*array.length));
            //sleep(100.0);
        }
    }
    
    public static void bogobogoSort(){
        
    }
}
