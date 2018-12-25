package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

public class BogoSort {
    public static boolean bogoIsSorted(int[] arr){
        for(int i = 1; i < arr.length; i++){
            comps++;
            aa++;
            marked.set(1, i);
            marked.set(2,i-1);
            sleep(1);
            if(arr[i]<arr[i-1])
                return false;
        }
        return true;
    }
    
    public static void bogoSort(){
        while(!bogoIsSorted(array)){
            for(int i = 0; i < array.length; i++){
                swap(array, i, (int)(Math.random()*array.length));
                sleep(1);
            }
        }
    }
    
    public static void bogobogoSort(){
        
    }
}
